package com.gerwalex.mymonma.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import com.gerwalex.mymonma.database.room.DB
import com.gerwalex.mymonma.database.views.CashTrxView
import com.gerwalex.mymonma.main.MonMaViewModel
import com.gerwalex.mymonma.ui.navigation.Destination
import com.gerwalex.mymonma.ui.navigation.Up
import kotlinx.coroutines.launch

@Composable
fun AddRegelmTrxScreen(viewModel: MonMaViewModel, navigateTo: (Destination) -> Unit) {
    val scope = rememberCoroutineScope()
    val accountid = rememberSaveable { viewModel.accountid }
    val list = ArrayList<CashTrxView>().apply {
        add(CashTrxView(accountid = accountid))
    }
    EditCashTrxScreen(list = list) { save ->
        scope.launch {
            save?.let {
                TODO()
            }
            navigateTo(Up)

        }
    }
}


@Composable
fun EditRegelmTrxScreen(viewModel: MonMaViewModel, navigateTo: (Destination) -> Unit) {
    val scope = rememberCoroutineScope()
    viewModel.cashTrxId.let {
        val list by DB.dao.getCashTrx(it).collectAsState(emptyList())
        if (list.isNotEmpty()) {
            EditCashTrxScreen(list = list) { save ->
                scope.launch {
                    save?.let {
                        TODO()
                    }
                    navigateTo(Up)

                }
            }

        }
    }
}
