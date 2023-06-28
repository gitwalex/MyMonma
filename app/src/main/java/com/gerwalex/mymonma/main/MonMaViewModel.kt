package com.gerwalex.mymonma.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.gerwalex.mymonma.database.room.DB.dao
import com.gerwalex.mymonma.database.room.DB.wpdao
import com.gerwalex.mymonma.database.tables.Cat
import com.gerwalex.mymonma.database.views.CashTrxView
import com.gerwalex.mymonma.database.views.WPStammView
import kotlinx.coroutines.flow.Flow

class MonMaViewModel(application: Application) : AndroidViewModel(application) {
    val gesamtvermoegen = dao.getGesamtVermoegen()
    val accountlist = dao.getAccountlist()
    val depotlist = wpdao.getDepotList()
    var regelmTrxId: Long = 0
    var wpstamm: WPStammView? = null

    fun getCashTrxList(accountid: Long): Flow<List<CashTrxView>> {
        return dao.getCashTrxList(accountid)
    }

    fun getAccount(accountid: Long): Flow<Cat> {
        return dao.getAccountData(accountid)
    }
}
