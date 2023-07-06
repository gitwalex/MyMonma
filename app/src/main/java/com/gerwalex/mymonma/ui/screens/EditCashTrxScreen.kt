package com.gerwalex.mymonma.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.database.room.DB.dao
import com.gerwalex.mymonma.database.tables.AutoCompletePartnerView
import com.gerwalex.mymonma.database.tables.Cat.Companion.NOCATID
import com.gerwalex.mymonma.database.tables.Cat.Companion.SPLITBUCHUNGCATID
import com.gerwalex.mymonma.database.tables.CatClass
import com.gerwalex.mymonma.database.tables.Partnerstamm.Companion.Undefined
import com.gerwalex.mymonma.database.views.AutoCompleteCatView
import com.gerwalex.mymonma.database.views.CashTrxView
import com.gerwalex.mymonma.ext.rememberState
import com.gerwalex.mymonma.main.MonMaViewModel
import com.gerwalex.mymonma.ui.AppTheme
import com.gerwalex.mymonma.ui.content.AmountEditView
import com.gerwalex.mymonma.ui.content.AmountView
import com.gerwalex.mymonma.ui.content.DatePickerView
import com.gerwalex.mymonma.ui.content.QuerySearch
import com.gerwalex.mymonma.ui.navigation.Destination
import com.gerwalex.mymonma.ui.navigation.TopToolBar
import com.gerwalex.mymonma.ui.navigation.Up
import com.gerwalex.mymonma.ui.states.CashTrxViewState
import com.gerwalex.mymonma.ui.states.rememberCashTrxViewState
import com.gerwalex.mymonma.ui.subscreens.DifferenzView
import com.gerwalex.mymonma.ui.subscreens.SplitLine
import kotlinx.coroutines.launch


@Composable
fun AddCashTrxScreen(
    accountid: Long,
    viewModel: MonMaViewModel,
    navigateTo: (Destination) -> Unit
) {
    val list = ArrayList<CashTrxView>().apply {
        add(CashTrxView(accountid = accountid))
    }
    EditCashTrxScaffold(list = list, navigateTo)
}


@Composable
fun EditCashTrxScreen(trxid: Long, viewModel: MonMaViewModel, navigateTo: (Destination) -> Unit) {
    var list by rememberState {
        listOf<CashTrxView>()
    }
    LaunchedEffect(trxid) {
        list = dao.getCashTrx(trxid)
    }
    if (list.isNotEmpty()) {
        EditCashTrxScaffold(list = list, navigateTo)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditCashTrxScaffold(
    list: List<CashTrxView>,
    navigateTo: (Destination) -> Unit

) {
    val scope = rememberCoroutineScope()
    val cashTrxState = rememberCashTrxViewState(trx = list[0])
    val snackbarHostState = remember { SnackbarHostState() }
    val keyboardController = LocalSoftwareKeyboardController.current
    DisposableEffect(Unit) {
        onDispose {
            keyboardController?.hide()
        }
    }
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopToolBar(
                stringResource(list[0].id?.let { R.string.umsatzBearbeiten }
                    ?: R.string.umsatzNeu),
                actions = {
                    IconButton(
                        enabled = cashTrxState.differenz == 0L,
                        onClick = {
                            scope.launch {
                                ArrayList<CashTrxView>().apply {
                                    add(list[0])
                                    addAll(cashTrxState.splitlist)
                                    CashTrxView.insert(this)
                                }
                                navigateTo(Up)

                            }
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
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            EditCashTrxScreen(list = list, cashTrxState = cashTrxState)
        }
    }
}

/**
 * @param list: CashTrx als liste
 * @param onFinished: true: Speichern erwünscht (keine Fehler mehr), false -> backPressed
 *
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditCashTrxScreen(
    list: List<CashTrxView>,
    cashTrxState: CashTrxViewState,
) {
    if (list.isNotEmpty()) {
        val keyboardController = LocalSoftwareKeyboardController.current
        DisposableEffect(key1 = Unit) {
            onDispose {
                keyboardController?.hide()
            }
        }
        val lazyColumnState = rememberLazyListState()
        val scope = rememberCoroutineScope()
        val mainTrx = list[0]
        LaunchedEffect(key1 = mainTrx) {
            cashTrxState.splitlist.apply {
                addAll(list.filter { it.transferid != null })
            }

        }
        Column(
            modifier = Modifier.padding(8.dp)
        )
        {
            Row(
                modifier = Modifier,
            ) {
                var btag by rememberState { mainTrx.btag }
                DatePickerView(
                    date = btag,
                ) { date ->
                    btag = date
                    mainTrx.btag = date
                }
                Spacer(modifier = Modifier.weight(1f))
                AmountEditView(value = cashTrxState.gesamtsumme) {
                    cashTrxState.gesamtsumme = it
                    mainTrx.amount = cashTrxState.gesamtsumme
                    if (cashTrxState.splitlist.size == 0) {
                        cashTrxState.splitsumme =
                            cashTrxState.gesamtsumme // Trx ist nicht gesplitted ->

                    }
                }
            }
            LazyColumn(state = lazyColumnState) {
                item {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = cashTrxState.memo ?: "",
                        minLines = 3,
                        onValueChange = { text ->
                            cashTrxState.memo = text
                            mainTrx.memo = text
                        },
                        label = { Text(text = stringResource(id = R.string.memo)) },
                    )
                }
                item {
                    if (LocalInspectionMode.current) {
                        QuerySearch(query = "Partner", label = "label", onQueryChanged = {})
                    } else {
                        AutoCompletePartnerView(filter = mainTrx.partnername) { partner ->
                            mainTrx.partnername = partner.name
                            mainTrx.partnerid = partner.id ?: Undefined
                        }
                    }
                }

                if (cashTrxState.splitlist.isEmpty()) {
                    item {
                        if (LocalInspectionMode.current) {
                            QuerySearch(
                                query = "Kategorie",
                                label = "kategorie",
                                onQueryChanged = {})
                        } else {
                            AutoCompleteCatView(filter = mainTrx.catname) { cat ->
                                cat.id?.let {
                                    mainTrx.catname = cat.name
                                    mainTrx.catclassid = cat.catclassid
                                    mainTrx.catid = it
                                } ?: run {
                                    mainTrx.catname = ""
                                    mainTrx.catclassid = CatClass.InternalCatClass
                                    mainTrx.catid = NOCATID
                                }
                            }
                        }
                        TextButton(onClick = {
                            cashTrxState.splitlist.add(mainTrx.copy(id = null))
                            mainTrx.catid = SPLITBUCHUNGCATID
                            mainTrx.catclassid = CatClass.InternalCatClass
                        }) {
                            Text(text = stringResource(id = R.string.splitten))
                        }
                    }
                } else {
                    item {
                        Column {
                            Divider(modifier = Modifier.padding(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stringResource(id = R.string.splittbuchung),
                                    style = MaterialTheme.typography.headlineSmall
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                IconButton(onClick = {
                                    cashTrxState.splitlist.add(
                                        mainTrx.copy(
                                            id = null,
                                            catid = NOCATID,
                                            amount = cashTrxState.differenz,
                                            catname = ""
                                        )
                                    )
                                    cashTrxState.splitsumme += cashTrxState.differenz
                                    scope.launch {
                                        lazyColumnState.animateScrollToItem(cashTrxState.splitlist.size - 1)
                                    }
                                }) {
                                    Icon(imageVector = Icons.Default.Add, "")
                                }

                            }
                        }
                    }
                    items(cashTrxState.splitlist) {
                        SplitLine(trx = it, onChanged = {
                            cashTrxState.splitsumme =
                                calculateSplitsumme(cashTrxState.splitlist)
                        })
                    }
                    item {
                        Column {
                            if (cashTrxState.differenz != 0L) {
                                DifferenzView(cashTrxState.differenz, onClick = {
                                    cashTrxState.gesamtsumme = cashTrxState.splitsumme
                                    mainTrx.amount = cashTrxState.gesamtsumme
                                })
                            }
                            // Summenzeile
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                    text = stringResource(id = R.string.summe),
                                    style = MaterialTheme.typography.labelSmall
                                )
                                AmountView(value = cashTrxState.splitsumme)

                            }
                        }
                    }
                }

            }
        }
    }
}


fun calculateSplitsumme(splitlist: List<CashTrxView>): Long {
    var amount = 0L
    splitlist.forEach { trx ->
        amount += trx.amount
    }
    return amount
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
            EditCashTrxScaffold(list = list) {

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
            EditCashTrxScaffold(list = list) {

            }
        }

    }
}




