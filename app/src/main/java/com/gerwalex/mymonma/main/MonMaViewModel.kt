package com.gerwalex.mymonma.main

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.AndroidViewModel
import com.gerwalex.mymonma.database.data.GesamtVermoegen
import com.gerwalex.mymonma.database.room.DB.dao
import com.gerwalex.mymonma.database.room.DB.wpdao
import com.gerwalex.mymonma.database.tables.Cat
import com.gerwalex.mymonma.database.views.AccountCashView
import com.gerwalex.mymonma.database.views.AccountDepotView
import com.gerwalex.mymonma.database.views.CashTrxView
import com.gerwalex.mymonma.database.views.WPStammView
import com.gerwalex.mymonma.ext.dataStore
import kotlinx.coroutines.flow.Flow

class MonMaViewModel(application: Application) : AndroidViewModel(application) {
    val dataStore: DataStore<Preferences>
    val gesamtvermoegen: Flow<GesamtVermoegen>
        get() {
            return dao.getGesamtVermoegen()
        }
    val accountlist: Flow<List<AccountCashView>>
        get() {
            return dao.getAccountlist()
        }
    val depotlist: Flow<List<AccountDepotView>>
        get() {
            return wpdao.getDepotList()
        }
    var wpstamm: WPStammView? = null

    fun getCashTrxList(accountid: Long): Flow<List<CashTrxView>> {
        return dao.getCashTrxList(accountid)
    }

    fun getAccount(accountid: Long): Flow<Cat> {
        return dao.getAccountData(accountid)
    }


    init {
        dataStore = application.dataStore
    }
}
