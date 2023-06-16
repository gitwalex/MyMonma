package com.gerwalex.mymonma.workers

import android.content.Context
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.work.*
import com.gerwalex.mymonma.BuildConfig
import com.gerwalex.mymonma.database.room.FileUtils
import com.gerwalex.mymonma.ext.preferences
import com.gerwalex.mymonma.importer.FinanztreffApi
import com.gerwalex.mymonma.importer.FinanztreffOnline
import com.gerwalex.mymonma.main.App
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import java.sql.Date
import java.util.*


class KursDownloadWorkerNew(private val context: Context, params: WorkerParameters) :
    CoroutineWorker(
        context, params
    ) {
    private val headers = HashMap<String, String>().apply {
        put("Content-Type", "application/json")
    }
    var gson = GsonBuilder()
        .setLenient()
        .create()
    private val provideClient = OkHttpClient.Builder().apply {
        if (BuildConfig.DEBUG) {
            addInterceptor(HttpLoggingInterceptor().apply {
                setLevel(HttpLoggingInterceptor.Level.BODY)
            })
        }

    }
    private val messageSenderApi = Retrofit.Builder()
        .baseUrl(BuildConfig.Finanztreff_Base_Url)
        .client(provideClient.build())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(FinanztreffApi::class.java)


    private val messages: MutableList<String> = ArrayList()
    override suspend fun doWork(): Result {
        val weekday = GregorianCalendar
            .getInstance()
            .get(Calendar.DAY_OF_WEEK)
        return when (weekday) {
            Calendar.SUNDAY, Calendar.SATURDAY -> {
                Result.success()
            }

            else -> {
                withContext(Dispatchers.IO) {
                    executeDownload()
                }
            }
        }
    }

    @WorkerThread
    suspend fun executeDownload(): Result {
        try {
            val user = context.preferences.getString("user", "")!!
            val pw = context.preferences.getString("pw", "")!!
            messageSenderApi.getUserLogin(user, pw)
            delay(1000)
            val download = messageSenderApi.requestKurse(headers).awaitResponse()
            Log.d("KursDownloadWorker", "executeDownload: $download")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Result.success()
    }

    @Throws(IOException::class)
    private fun loadKurseAsCSV(list: List<String>) {
        if (list.size > 0) {
            val df = Date(System.currentTimeMillis())
            val fn = FinanztreffOnline.prefix + df + ".csv"
            val file = File(App.getAppDownloadDir(context), fn)
            if (FileUtils.writeFile(file, list)) {
                messages.add("Download file $fn")
                FinanztreffOnline(context)
                    .doImport(messages)
            }
        }
    }


    companion object {

        private const val LOGIN_URL = "https://alt.finanztreff.de/anmeldung.htn?"

        @JvmStatic
        fun submit(context: Context) {
            val constraints = Constraints
                .Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val request = OneTimeWorkRequest
                .Builder(KursDownloadWorkerNew::class.java)
                .setConstraints(constraints)
                .build()
            WorkManager
                .getInstance(context)
                .enqueue(request)
        }

        fun cancel(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork("KursDownload-1")
            WorkManager.getInstance(context).cancelUniqueWork("KursDownload-2")
        }

    }
}