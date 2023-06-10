package com.gerwalex.mymonma.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.gerwalex.mymonma.MonMaViewModel
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.database.room.DB.dao
import com.gerwalex.mymonma.database.tables.AutoCompleteCatView
import com.gerwalex.mymonma.database.tables.AutoCompletePartnerView
import com.gerwalex.mymonma.ui.AppTheme
import com.gerwalex.mymonma.ui.content.AmountEditView
import com.gerwalex.mymonma.ui.content.DatePickerView
import com.gerwalex.mymonma.ui.content.QuerySearch
import com.gerwalex.mymonma.ui.navigation.Destination
import com.gerwalex.mymonma.ui.navigation.TopToolBar
import com.gerwalex.mymonma.ui.navigation.Up
import kotlinx.coroutines.launch


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
    val scope = rememberCoroutineScope()
    if (list.isNotEmpty()) {
        val trx = list[0]
        var amount by remember { mutableStateOf(trx.amount) }
        Scaffold(
            topBar = {
                TopToolBar(
                    stringResource(id = R.string.umsatzBearbeiten),
                    actions = {
                        IconButton(onClick = {
                            scope.launch { dao.insertCashTrxView(list) }
                            navigateTo(Up)
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Save,
                                contentDescription = stringResource(id = R.string.save)
                            )

                        }
                    }
                ) {
                    navigateTo(Up)
                }
            },
        ) {
            Column(modifier = Modifier.padding(it))
            {
                Row(
                    modifier = Modifier,
                ) {

                    DatePickerView(date = trx.btag) { date ->
                        trx.btag = date
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    AmountEditView(value = trx.amount) {
                        trx.amount = it
                    }
                }
                if (LocalInspectionMode.current) {
                    QuerySearch(query = "Partner", label = "label", onQueryChanged = {})
                } else {
                    AutoCompletePartnerView(filter = trx.partnername) { partner ->
                        trx.partnername = partner.name
                        trx.partnerid = partner.id ?: -1
                    }
                }
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = trx.memo ?: "",
                    minLines = 3,
                    onValueChange = { text -> trx.memo = text },
                    label = { Text(text = stringResource(id = R.string.memo)) },
                )

                if (LocalInspectionMode.current) {
                    QuerySearch(query = "Kategorie", label = "kategorie", onQueryChanged = {})
                } else {
                    AutoCompleteCatView(filter = trx.catname) { cat ->
                        trx.catid = cat.id ?: -1L
                    }
                }
                TextButton(onClick = { /*TODO*/ }) {
                    Text(text = stringResource(id = R.string.splitten))

                }

            }

        }

    }
}

@Composable
fun SplitLine(trx: CashTrxView, onAmountChanged: (Long) -> Unit) {
    Row(modifier = Modifier) {

        if (LocalInspectionMode.current) {
            QuerySearch(query = "Kategorie", label = "kategorie", onQueryChanged = {})
        } else {
            AutoCompleteCatView(filter = trx.catname) { cat ->
                trx.catid = cat.id ?: -1L
            }
            Spacer(modifier = Modifier.weight(1f))
            AmountEditView(value = trx.amount) {
                trx.amount = it
                onAmountChanged(it)
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



