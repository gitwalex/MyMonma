package com.gerwalex.mymonma.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.gerwalex.mymonma.MonMaViewModel
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.database.room.DB.dao
import com.gerwalex.mymonma.database.tables.AutoCompletePartnerView
import com.gerwalex.mymonma.database.tables.CashTrx
import com.gerwalex.mymonma.ui.AppTheme
import com.gerwalex.mymonma.ui.content.AmountEditView
import com.gerwalex.mymonma.ui.content.DatePickerView
import com.gerwalex.mymonma.ui.navigation.Destination
import com.gerwalex.mymonma.ui.navigation.TopToolBar
import com.gerwalex.mymonma.ui.navigation.Up


@Composable
fun EditCashTrxScreen(viewModel: MonMaViewModel, navigateTo: (Destination) -> Unit) {

    val cashTrxView by viewModel.cashTrx.collectAsState()
    cashTrxView.id?.let {
        val trx by dao.getCashTrx(it).collectAsState(initial = CashTrx())
        EditCashTrxScreen(cashTrxView = cashTrxView, trx = trx, navigateTo = navigateTo)
    } ?: run {
        val trx = CashTrx(accountid = viewModel.account.value?.id!!)
        EditCashTrxScreen(trx = trx, navigateTo = navigateTo)

    }
}

@Composable
fun EditCashTrxScreen(
    trx: CashTrx,
    cashTrxView: CashTrxView? = null, // null bei neuen Umsaetzen
    navigateTo: (Destination) -> Unit
) {
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
        Column(modifier = Modifier.padding(it))
        {
            Row {
                DatePickerView(date = trx.btag) {

                }
                AmountEditView(value = trx.amount) {}
            }
            Row {
                AutoCompletePartnerView(filter = cashTrxView?.partnername ?: "") {

                }
            }

            Log.d("EditCashTrxScreen", "EditCashTrxScreen: $trx")

        }

    }

}

@Preview(name = "Light", uiMode = UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun EditCashTrxPreview() {
    val cashTrxView = CashTrxView()
    val trx = CashTrx()
    AppTheme {
        Surface {
            EditCashTrxScreen(trx = trx, cashTrxView = cashTrxView) {

            }
        }

    }
}



