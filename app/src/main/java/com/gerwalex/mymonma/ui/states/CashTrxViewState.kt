package com.gerwalex.mymonma.ui.states

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.gerwalex.mymonma.database.views.CashTrxView

class CashTrxViewState(trx: CashTrxView) {
    var memo by mutableStateOf(trx.memo)
    var gesamtsumme by mutableStateOf(trx.amount)
    val splitlist = mutableStateListOf<CashTrxView>()
    var splitsumme by mutableStateOf(trx.amount)
    val differenz by derivedStateOf { gesamtsumme - splitsumme }
}

@Composable
fun rememberCashTrxViewState(
    trx: CashTrxView
) = remember { CashTrxViewState(trx) }