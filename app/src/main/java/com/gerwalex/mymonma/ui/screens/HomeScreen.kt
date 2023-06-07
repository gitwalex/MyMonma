package com.gerwalex.mymonma.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.gerwalex.mymonma.MonMaViewModel
import com.gerwalex.mymonma.database.tables.AutoCompletePartnerView
import com.gerwalex.mymonma.ui.content.AmountEditView
import com.gerwalex.mymonma.ui.navigation.Destination
import java.math.BigDecimal

@Composable
fun HomeScreen(viewModel: MonMaViewModel, navigatTo: (Destination) -> Unit) {
    Column {


//        AutoCompletePartnerView("") {
//            Log.d("HomeScreen", "HomeScreen: selected = $it")
//        }
        AutoCompletePartnerView("Alex") {
            Log.d("HomeScreen", "HomeScreen: selected = $it")
        }
        AmountEditView(value = BigDecimal("100.23")) {

        }
    }
}
