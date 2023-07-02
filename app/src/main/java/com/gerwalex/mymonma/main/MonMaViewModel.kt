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
import com.gerwalex.mymonma.ext.dataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class MonMaViewModel(application: Application) : AndroidViewModel(application) {
    private val collector: Job
    val dataStore: DataStore<Preferences>
    val gesamtvermoegen = MutableSharedFlow<GesamtVermoegen>(1)
    val accountlist = MutableSharedFlow<List<AccountCashView>>(1)
    val depotlist = MutableSharedFlow<List<AccountDepotView>>(1)

    fun getCashTrxList(accountid: Long): Flow<List<CashTrxView>> {
        return dao.getCashTrxList(accountid)
    }

    fun getAccount(accountid: Long): Flow<Cat> {
        return dao.getAccountData(accountid)
    }

    override fun onCleared() {
        super.onCleared()
        collector.cancel()
    }


    init {
        dataStore = application.dataStore
        collector = CoroutineScope(Dispatchers.IO).launch {
            launch {
                dao.getGesamtVermoegen().collect {
                    gesamtvermoegen
                        .emit(it)
                }
            }
            launch {
                dao.getAccountlist().collect {
                    accountlist.emit(it)
                }
            }
            launch {
                wpdao.getDepotList().collect {
                    depotlist.emit(it)
                }
            }
        }

    }
}

