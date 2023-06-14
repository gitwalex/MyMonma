package com.gerwalex.mymonma.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gerwalex.mymonma.MonMaViewModel
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.database.room.DB.dao
import com.gerwalex.mymonma.database.tables.AutoCompleteCatView
import com.gerwalex.mymonma.database.tables.AutoCompletePartnerView
import com.gerwalex.mymonma.database.tables.Cat.Companion.SPLITBUCHUNGCATID
import com.gerwalex.mymonma.database.views.CashTrxView
import com.gerwalex.mymonma.ui.AppTheme
import com.gerwalex.mymonma.ui.content.AmountEditView
import com.gerwalex.mymonma.ui.content.DatePickerView
import com.gerwalex.mymonma.ui.content.QuerySearch
import com.gerwalex.mymonma.ui.navigation.Destination
import com.gerwalex.mymonma.ui.navigation.TopToolBar
import com.gerwalex.mymonma.ui.navigation.Up
import com.gerwalex.mymonma.ui.subscreens.Splitlist
import kotlinx.coroutines.launch


@Composable
fun AddCashTrxScreen(viewModel: MonMaViewModel, navigateTo: (Destination) -> Unit) {
    val scope = rememberCoroutineScope()
    viewModel.account?.id?.let { accountid ->
        val list = ArrayList<CashTrxView>().apply {
            add(CashTrxView(accountid = accountid))
        }
        EditCashTrxScreen(list = list) { save ->
            scope.launch {
                if (save) {
                    dao.insertCashTrxView(list)
                }
                navigateTo(Up)

            }
        }
    }
}


@Composable
fun EditCashTrxScreen(viewModel: MonMaViewModel, navigateTo: (Destination) -> Unit) {
    val scope = rememberCoroutineScope()
    viewModel.cashTrxId?.let {
        val list by dao.getCashTrx(it).collectAsState(emptyList())
        if (list.isNotEmpty()) {
            EditCashTrxScreen(list = list) { save ->
                scope.launch {
                    if (save) {
                        dao.insertCashTrxView(list)
                    }
                    navigateTo(Up)

                }
            }

        }
    }
}

/**
 * @param list: CashTrx als liste
 * @param onFinished: true: Speichern erwünscht (keine Fehler mehr), false -> backPressed
 *
 */
@Composable
fun EditCashTrxScreen(
    list: List<CashTrxView>,
    onFinished: (save: Boolean) -> Unit
) {
    var spliterror by remember { mutableStateOf(false) }
    if (list.isNotEmpty()) {
        val trx = list[0]
        var gesamtsumme by remember { mutableStateOf(trx.amount) }
        val splitlist = remember(key1 = list) { mutableStateListOf<CashTrxView>() }
        splitlist.addAll(list.filter { it.transferid != null })
        // Differenz zwischen Splitbuchungen und trx.amount
        val snackbarHostState = remember { SnackbarHostState() }
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            topBar = {
                TopToolBar(
                    stringResource(trx.id?.let { R.string.umsatzBearbeiten }
                        ?: R.string.umsatzNeu),
                    actions = {
                        IconButton(
                            enabled = !spliterror,
                            onClick = {
                                onFinished(true)
                            }) {
                            Icon(
                                imageVector = Icons.Filled.Save,
                                contentDescription = stringResource(id = R.string.save)
                            )
                        }
                    }
                ) {
                    onFinished(false)
                }
            },
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(8.dp)

            )
            {
                Row(
                    modifier = Modifier,
                ) {
                    DatePickerView(
                        date = trx.btag,
                    ) { date ->
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
                        trx.partnerid = partner.id ?: 0
                    }
                }
                OutlinedTextField(
//                    modifier = Modifier.fillMaxWidth(),
                    value = trx.memo ?: "",
                    minLines = 3,
                    onValueChange = { text -> trx.memo = text },
                    label = { Text(text = stringResource(id = R.string.memo)) },
                )
                if (splitlist.isEmpty()) {
                    if (LocalInspectionMode.current) {
                        QuerySearch(
                            query = "Kategorie",
                            label = "kategorie",
                            onQueryChanged = {})
                    } else {
                        AutoCompleteCatView(filter = trx.catname) { cat ->
                            trx.catid = cat.id?.let {
                                trx.catname = cat.name
                                trx.catclassid = cat.catclassid
                                it
                            } ?: let {
                                trx.catname = ""
                                trx.catclassid = 1
                                0L
                            }
                            Log.d("EditCashTrxScreen", "selected=$cat ")
                        }
                    }
                    TextButton(onClick = {
                        splitlist.add(trx.copy(id = null))
                        trx.catid = SPLITBUCHUNGCATID
                    }) {
                        Text(text = stringResource(id = R.string.splitten))
                    }
                } else {
                    Splitlist(trx, splitlist = splitlist) {
                        spliterror = it
                        gesamtsumme = trx.amount
                    }
                }
            }


        }

    }
}

@Preview(name = "Light", uiMode = UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun EditCashTrxPreview() {
    val list = ArrayList<CashTrxView>().apply {
        add(CashTrxView(memo = "memo"))
    }
    AppTheme {
        Surface {
            EditCashTrxScreen(list = list) {

            }
        }

    }
}

@Preview(name = "Light", uiMode = UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun EditSplittedCashTrxPreview() {
    val list = ArrayList<CashTrxView>().apply {
        add(CashTrxView(memo = "memo"))
        add(CashTrxView(transferid = 1))
        add(CashTrxView(transferid = 1))
        add(CashTrxView(transferid = 1))
    }
    AppTheme {
        Surface {
            EditCashTrxScreen(list = list) {

            }
        }

    }
}




