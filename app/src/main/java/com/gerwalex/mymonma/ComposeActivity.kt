package com.gerwalex.mymonma

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.gerwalex.mymonma.ui.AppTheme
import com.gerwalex.mymonma.ui.navigation.MyNavHost
import com.gerwalex.mymonma.ui.screens.Destination
import com.maltaisn.calcdialog.CalcDialog
import java.math.BigDecimal
import java.text.NumberFormat
import kotlin.math.pow

class ComposeActivity : AppCompatActivity(), CalcDialog.CalcDialogCallback {

    private lateinit var navController: NavHostController
    private val viewModel by viewModels<MonMaViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            navController = rememberNavController()
            AppTheme {
                Surface {
                    Scaffold(modifier = Modifier.fillMaxSize()) {
                        Box(modifier = Modifier.padding(it)) {
                            MyNavHost(navController = navController, viewModel = viewModel) {
                                navigateTo(it)
                            }
                        }
                    }
                }
            }
        }
    }

    fun navigateTo(destination: Destination) {
        destination.navigate(navController)

    }

    override fun onValueEntered(requestCode: Int, value: BigDecimal?) {
        if (requestCode == CalcResultRequest) {
            val currency = NumberFormat.getCurrencyInstance()
            val digits = BigDecimal(10.0.pow(currency.maximumFractionDigits.toDouble()))
            val result = Bundle().apply {
                putLong(CalcResult, value?.multiply(digits)?.toLong() ?: 0L)
            }
            supportFragmentManager.setFragmentResult(CalcResult, result)
        }
    }

    companion object {
        const val CalcResultRequest = 1
        const val CalcResult = "CalcResult"
        const val AcoountId = "AcoountId"

    }
}
