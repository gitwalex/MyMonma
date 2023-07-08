package com.gerwalex.mymonma.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.database.room.DB.dao
import com.gerwalex.mymonma.database.views.AccountCashView
import com.gerwalex.mymonma.database.views.CashTrxView
import com.gerwalex.mymonma.database.views.TrxRegelmView
import com.gerwalex.mymonma.enums.IntervallSpinner
import com.gerwalex.mymonma.ext.rememberState
import com.gerwalex.mymonma.main.MonMaViewModel
import com.gerwalex.mymonma.ui.navigation.Destination
import com.gerwalex.mymonma.ui.navigation.TopToolBar
import com.gerwalex.mymonma.ui.navigation.Up
import com.gerwalex.mymonma.ui.spinner.AccountCashSpinner
import com.gerwalex.mymonma.ui.states.rememberCashTrxViewState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun AddRegelmTrxScreen(
    viewModel: MonMaViewModel,
    navigateTo: (Destination) -> Unit
) {
    var accounts by rememberState { listOf<AccountCashView>() }
    LaunchedEffect(Unit) {
        accounts = viewModel.accountlist.first()

    }
    if (accounts.isNotEmpty()) {
        val list = ArrayList<TrxRegelmView>().apply {
            add(TrxRegelmView(accountid = accounts[0].id))
        }
        EditRegelmTrxScaffold(regelmtrx = list, accounts = accounts, navigateTo)

    }
}


@Composable
fun EditRegelmTrxScreen(trxid: Long, viewModel: MonMaViewModel, navigateTo: (Destination) -> Unit) {
    var accounts by rememberState { listOf<AccountCashView>() }
    var list by rememberState { listOf<TrxRegelmView>() }
    LaunchedEffect(key1 = trxid) {
        list = dao.getTrxRegelm(trxid)
        accounts = viewModel.accountlist.first()
    }
    if (list.isNotEmpty()) {
        EditRegelmTrxScaffold(regelmtrx = list, accounts, navigateTo)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditRegelmTrxScaffold(
    regelmtrx: List<TrxRegelmView>,
    accounts: List<AccountCashView>,
    navigateTo: (Destination) -> Unit

) {
    val scope = rememberCoroutineScope()
    val list by rememberState(regelmtrx) {
        ArrayList<CashTrxView>().apply {
            regelmtrx.forEach {
                add(it.cashTrxView)
            }
        }
    }
    if (list.isNotEmpty()) {
        val cashTrxState = rememberCashTrxViewState(trx = list[0])
        val snackbarHostState = remember { SnackbarHostState() }
        val keyboardController = LocalSoftwareKeyboardController.current
        var intervall by rememberState { regelmtrx[0].intervall }
        var account by rememberState { regelmtrx[0].accountid }
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
                            modifier = Modifier.scale(1.5f),
                            enabled = cashTrxState.differenz == 0L,
                            onClick = {
                                scope.launch {
                                    TrxRegelmView.insert(intervall, account, list)
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
            Column(modifier = Modifier.padding(padding)) {
                IntervallSpinner(
                    intervall = intervall,
                    selected = { intervall = it })
                AccountCashSpinner(
                    accountid = account,
                    accounts = accounts,
                    selected = { account = it.id })
                EditCashTrxScreen(list = list, cashTrxState = cashTrxState)
            }
        }
    }
}

@Preview(name = "Light", uiMode = UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun EditRegelmtrxPreview() {
    val regelmtrx = ArrayList<TrxRegelmView>().apply {
        add(
            TrxRegelmView(
                accountid = 2,
                amount = 2500_00
            )
        )
    }
    EditRegelmTrxScaffold(regelmtrx = regelmtrx, ArrayList(), navigateTo = {})

}