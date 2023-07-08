package com.gerwalex.mymonma.main

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.gerwalex.mymonma.database.room.MyConverter.NACHKOMMA
import com.gerwalex.mymonma.ext.FileExt.copy
import com.gerwalex.mymonma.ext.registerActivityForResult
import com.gerwalex.mymonma.ext.registerforPermissionRequest
import com.gerwalex.mymonma.ui.AppTheme
import com.gerwalex.mymonma.ui.navigation.Destination
import com.gerwalex.mymonma.ui.navigation.DownloadKurse
import com.gerwalex.mymonma.ui.navigation.ImportCashTrx
import com.gerwalex.mymonma.ui.navigation.InProgress
import com.gerwalex.mymonma.ui.navigation.MyNavHost
import com.gerwalex.mymonma.ui.navigation.NotInProgress
import com.gerwalex.mymonma.workers.FileImportWorker
import com.gerwalex.mymonma.workers.KursDownloadWorker
import com.maltaisn.calcdialog.CalcDialog
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.math.BigDecimal
import java.text.NumberFormat
import kotlin.math.pow

class ComposeActivity : AppCompatActivity(), CalcDialog.CalcDialogCallback {

    private var inProgress: Boolean = false
    private lateinit var navController: NavHostController
    private val viewModel by viewModels<MonMaViewModel>()
    private val notificationPermissionLauncher = registerforPermissionRequest()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermissionLauncher.launchSinglePermission(
                Manifest.permission.POST_NOTIFICATIONS
            ) {
            }
        }
//        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            navController = rememberNavController()
            var isProgressIndicatorVisible by remember { mutableStateOf(inProgress) }
            AppTheme {
                Surface {
                    Scaffold(modifier = Modifier.fillMaxSize()) {
                        Box(
                            modifier = Modifier
                                .padding(it)
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            MyNavHost(navController = navController, viewModel = viewModel) {
                                when (it) {
                                    InProgress -> isProgressIndicatorVisible = true
                                    NotInProgress -> isProgressIndicatorVisible = false
                                    else -> navigateTo(it)

                                }
                            }
                            if (isProgressIndicatorVisible) {
                                CircularProgressIndicator(strokeWidth = 8.dp, color = Color.Green)
                            }
                        }
                    }
                }
            }
        }
    }

    fun navigateTo(destination: Destination) {
        when (destination) {
            DownloadKurse -> KursDownloadWorker.submit(this)
            ImportCashTrx -> performFileSearch()
            else -> destination.navigate(navController)
        }
    }

    override fun onValueEntered(requestCode: Int, value: BigDecimal?) {
        when (requestCode) {
            CalcAmountResultRequest -> {
                val currency = NumberFormat.getCurrencyInstance()
                val digits = BigDecimal(10.0.pow(currency.maximumFractionDigits.toDouble()))
                val result = Bundle().apply {
                    putLong(CalcAmountResult, value?.multiply(digits)?.toLong() ?: 0L)
                }
                supportFragmentManager.setFragmentResult(CalcAmountResult, result)
            }

            CalcMengeResultRequest -> {
                val result = Bundle().apply {
                    putLong(CalcMengeResult, value?.multiply(BigDecimal(NACHKOMMA))?.toLong() ?: 0L)
                }
                supportFragmentManager.setFragmentResult(CalcMengeResult, result)
            }

        }
    }

    private val activityLauncher = registerActivityForResult { result ->
        if (result.resultCode == RESULT_OK) {
            // There are no request codes
            result.data?.data?.let { data ->
                try {
                    contentResolver
                        .openInputStream(data)
                        .use { selectedFile ->
                            if (selectedFile != null) {
                                val appDownloadDir = App.getAppDownloadDir(this@ComposeActivity)
                                val filePath = appDownloadDir.absolutePath + "/" +
                                        getFileDisplayName(this@ComposeActivity, data)
                                val selectedFileOutPutStream: OutputStream =
                                    FileOutputStream(filePath)
                                selectedFile.copy(selectedFileOutPutStream)
                                val request = OneTimeWorkRequest
                                    .Builder(FileImportWorker::class.java)
                                    .build()
                                WorkManager
                                    .getInstance(this@ComposeActivity)
                                    .enqueue(request)
                            }
                        }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                //                    DocumentsContract.deleteDocument(getContentResolver(), resultData.getData());
            }
        }
    }

    private fun getFileDisplayName(context: Context, uri: Uri): String? {
        var displayName: String? = null
        context.contentResolver?.query(uri, null, null, null, null, null)?.use { c ->
            if (c.moveToFirst()) {
                val col = c.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME)
                displayName = c.getString(col)
                Log.d("gerwalex", String.format("Display Name {%1s}", displayName))
            }
        }
        return displayName
    }

    private fun performFileSearch() {
        val message = "Import"
        val type = "*/*"
        val mimeTypes = arrayOf("text/csv", "text/comma-separated-values")
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.flags =
            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
        intent.type = type
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        activityLauncher.launch(Intent.createChooser(intent, message))
    }


    companion object {
        const val CalcAmountResultRequest = 1
        const val CalcAmountResult = "CalcAmountResult"
        const val CalcMengeResultRequest = 2
        const val CalcMengeResult = "CalcMengeResult"
        const val AcoountId = "AcoountId"

    }
}
