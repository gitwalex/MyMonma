package com.gerwalex.mymonma.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.gerwalex.mymonma.MonMaViewModel
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.database.room.DB.dao
import com.gerwalex.mymonma.database.tables.AutoCompletePartnerView
import com.gerwalex.mymonma.database.tables.CashTrx
import com.gerwalex.mymonma.ui.navigation.Destination
import com.gerwalex.mymonma.ui.navigation.TopToolBar
import com.gerwalex.mymonma.ui.navigation.Up


@Composable
fun EditCashTrxScreen(viewModel: MonMaViewModel, navigateTo: (Destination) -> Unit) {

    val cashTrx by viewModel.cashTrx.observeAsState()
    cashTrx?.let { v ->
        val trx by dao.getCashTrx(v.id!!).collectAsState(initial = CashTrx())
        Scaffold(
            topBar = {
                TopToolBar(
                    stringResource(id = R.string.umsatzBearbeiten),
                    actions = {
                        IconButton(onClick = {
                            Log.d("EditCashTrxScreen", "EditCashTrxScreen: ")
                        }) {
                            Icons.Filled.Save
                        }
                    }
                ) {
                    navigateTo(Up)
                }
            },
        ) {
            Box(modifier = Modifier.padding(it))
            {
                Row {
                    AutoCompletePartnerView(filter = v.partnername) {

                    }
                }
                Log.d("EditCashTrxScreen", "EditCashTrxScreen: $trx")

            }

        }

    }
}


