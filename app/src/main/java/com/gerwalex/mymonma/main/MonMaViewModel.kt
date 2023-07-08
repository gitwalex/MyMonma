package com.gerwalex.mymonma.main

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.gerwalex.mymonma.database.data.GesamtVermoegen
import com.gerwalex.mymonma.database.room.DB.dao
import com.gerwalex.mymonma.database.room.DB.wpdao
import com.gerwalex.mymonma.database.tables.Cat
import com.gerwalex.mymonma.database.views.AccountCashView
import com.gerwalex.mymonma.database.views.AccountDepotView
import com.gerwalex.mymonma.database.views.CashTrxView
import com.gerwalex.mymonma.ext.dataStore
import com.gerwalex.mymonma.workers.KursDownloadWorker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MonMaViewModel(application: Application) : AndroidViewModel(application) {
    private val kursDownloadWorkObserver by lazy {
        WorkProgressObserver(application.applicationContext)
    }
    val dataStore: DataStore<Preferences>
    val gesamtvermoegen = MutableStateFlow(GesamtVermoegen())
    val accountlist = MutableStateFlow<List<AccountCashView>>(emptyList())
    val depotlist = MutableStateFlow<List<AccountDepotView>>(emptyList())

    fun getCashTrxList(accountid: Long): Flow<List<CashTrxView>> {
        return dao.getCashTrxList(accountid)
    }

    fun getAccount(accountid: Long): Flow<Cat> {

        return dao.getAccountData(accountid)
    }

    override fun onCleared() {
        super.onCleared()
        kursDownloadWorkObserver.onCleared()
    }

    fun startDownloadKurse(): MutableStateFlow<WorkProgressObserver.WorkProgressState> {
        getApplication<Application>().applicationContext.apply {
            KursDownloadWorker.submit(this).also { workId ->
                viewModelScope.launch {
                    kursDownloadWorkObserver.observe(workId)
                }
            }
        }
        return kursDownloadWorkObserver.workState

    }


    init {
        dataStore = application.dataStore
        viewModelScope.launch {
            dao.getGesamtVermoegen().collect {
                gesamtvermoegen
                    .emit(it)
            }
        }
        viewModelScope.launch {
            dao.getAccountlist().collect {
                accountlist.emit(it)
            }
        }
        viewModelScope.launch {
            wpdao.getDepotList().collect {
                depotlist.emit(it)
            }
        }
    }


    companion object {
        const val KEY_PROGRESS = "Active"

    }
}

