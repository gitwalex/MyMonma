package com.gerwalex.mymonma.ui.screens

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.database.room.DB.dao
import com.gerwalex.mymonma.database.views.CashTrxView
import com.gerwalex.mymonma.database.views.TrxRegelmView
import com.gerwalex.mymonma.enums.IntervallSpinner
import com.gerwalex.mymonma.ext.rememberState
import com.gerwalex.mymonma.main.MonMaViewModel
import com.gerwalex.mymonma.ui.navigation.Destination
import com.gerwalex.mymonma.ui.navigation.TopToolBar
import com.gerwalex.mymonma.ui.navigation.Up
import com.gerwalex.mymonma.ui.states.rememberCashTrxViewState
import kotlinx.coroutines.launch

@Composable
fun AddRegelmTrxScreen(
    accountid: Long,
    viewModel: MonMaViewModel,
    navigateTo: (Destination) -> Unit
) {
    val scope = rememberCoroutineScope()
    val list = ArrayList<TrxRegelmView>().apply {
        add(TrxRegelmView(accountid = accountid))
    }
    EditRegelmTrxScaffold(regelmtrxlist = list) { save ->
        scope.launch {
            save?.let {
                TODO()
            }
            navigateTo(Up)

        }
    }
}


@Composable
fun EditRegelmTrxScreen(trxid: Long, viewModel: MonMaViewModel, navigateTo: (Destination) -> Unit) {
    val scope = rememberCoroutineScope()
    val list by dao.getTrxRegelm(trxid).collectAsState(initial = emptyList())
    if (list.isNotEmpty()) {
        EditRegelmTrxScaffold(regelmtrxlist = list) { save ->
            scope.launch {
                save?.let {
                    TODO()
                }
                navigateTo(Up)

            }
        }

    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditRegelmTrxScaffold(
    regelmtrxlist: List<TrxRegelmView>,
    onFinished: (List<TrxRegelmView>?) -> Unit
) {
    var list by rememberState { ArrayList<CashTrxView>() }
    LaunchedEffect(key1 = regelmtrxlist) {
        val cashTrx = ArrayList<CashTrxView>()
        regelmtrxlist.forEach {
            cashTrx.add(it.cashTrxView)
        }
        list = cashTrx
    }
    if (list.isNotEmpty()) {

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
                            }) {
                            Icon(
                                imageVector = Icons.Filled.Save,
                                contentDescription = stringResource(id = R.string.save)
                            )
                        }
                    }
                ) {
                    onFinished(null)
                }
            },
        ) { padding ->
            Column(modifier = Modifier.padding(padding)) {
                IntervallSpinner(intervall = regelmtrxlist[0].intervall, selected = {})
                EditCashTrxScreen(list = list, cashTrxState = cashTrxState)
            }
        }
    }
}

