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
import com.gerwalex.mymonma.ui.AppTheme
import com.gerwalex.mymonma.ui.content.AmountEditView
import com.gerwalex.mymonma.ui.content.DatePickerView
import com.gerwalex.mymonma.ui.navigation.Destination
import com.gerwalex.mymonma.ui.navigation.TopToolBar
import com.gerwalex.mymonma.ui.navigation.Up


@Composable
fun AddCashTrxScreen(viewModel: MonMaViewModel, navigateTo: (Destination) -> Unit) {
    viewModel.account?.id?.let { accountid ->
        ArrayList<CashTrxView>().apply {
            add(CashTrxView(accountid = accountid))
            EditCashTrxScreen(list = this, navigateTo = navigateTo)
        }

    }
}

@Composable
fun EditCashTrxScreen(viewModel: MonMaViewModel, navigateTo: (Destination) -> Unit) {
    viewModel.cashTrxId?.let {
        val list by dao.getCashTrx(it).collectAsState(emptyList())
        if (list.isNotEmpty()) {
            EditCashTrxScreen(list = list, navigateTo = navigateTo)
        }

    }
}

@Composable
fun EditCashTrxScreen(
    list: List<CashTrxView>,
    navigateTo: (Destination) -> Unit
) {
    if (list.isNotEmpty()) {
        val trx = list[0]
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
                    AutoCompletePartnerView(filter = trx.partnername) {

                    }
                }

                Log.d("EditCashTrxScreen", "EditCashTrxScreen: $trx")

            }

        }

    }
}

@Preview(name = "Light", uiMode = UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun EditCashTrxPreview() {
    ArrayList<CashTrxView>().apply {
        add(CashTrxView())
        AppTheme {
            Surface {
                EditCashTrxScreen(list = this) {

                }
            }

        }
    }
}



