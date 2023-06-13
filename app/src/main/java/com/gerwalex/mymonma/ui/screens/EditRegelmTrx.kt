package com.gerwalex.mymonma.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import com.gerwalex.mymonma.MonMaViewModel
import com.gerwalex.mymonma.database.room.DB
import com.gerwalex.mymonma.database.views.CashTrxView
import com.gerwalex.mymonma.ui.navigation.Destination
import com.gerwalex.mymonma.ui.navigation.Up
import kotlinx.coroutines.launch

@Composable
fun AddRegelmTrxScreen(viewModel: MonMaViewModel, navigateTo: (Destination) -> Unit) {
    val scope = rememberCoroutineScope()
    viewModel.account?.id?.let { accountid ->
        val list = ArrayList<CashTrxView>().apply {
            add(CashTrxView(accountid = accountid))
        }
        EditCashTrxScreen(list = list) { save ->
            scope.launch {
                if (save) {
                    DB.dao.insertCashTrxView(list)
                }
                navigateTo(Up)

            }
        }
    }
}


@Composable
fun EditRegelmTrxScreen(viewModel: MonMaViewModel, navigateTo: (Destination) -> Unit) {
    val scope = rememberCoroutineScope()
    viewModel.cashTrxId?.let {
        val list by DB.dao.getCashTrx(it).collectAsState(emptyList())
        if (list.isNotEmpty()) {
            EditCashTrxScreen(list = list) { save ->
                scope.launch {
                    if (save) {
                        DB.dao.insertCashTrxView(list)
                    }
                    navigateTo(Up)

                }
            }

        }
    }
}
