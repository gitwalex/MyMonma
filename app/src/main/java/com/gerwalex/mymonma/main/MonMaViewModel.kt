package com.gerwalex.mymonma.main

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.gerwalex.mymonma.database.data.GesamtVermoegen
import com.gerwalex.mymonma.database.room.DB.Companion.dao
import com.gerwalex.mymonma.database.room.DB.Companion.wpdao
import com.gerwalex.mymonma.database.views.AccountCashView
import com.gerwalex.mymonma.database.views.AccountDepotView
import com.gerwalex.mymonma.ext.dataStore
import com.gerwalex.mymonma.workers.KursDownloadWorker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MonMaViewModel(application: Application) : AndroidViewModel(application) {
    val dataStore: DataStore<Preferences>
    val gesamtvermoegen = MutableStateFlow(GesamtVermoegen())
    val accountlist = MutableStateFlow<List<AccountCashView>>(emptyList())
    val depotlist = MutableStateFlow<List<AccountDepotView>>(emptyList())


    override fun onCleared() {
        super.onCleared()
        WorkProgressObserver.onCleared()
    }

    fun startDownloadKurse(): MutableStateFlow<WorkProgressObserver.WorkProgressState> {
        getApplication<Application>().applicationContext.apply {
            val workmanager = WorkManager.getInstance(this)
            KursDownloadWorker.submit(this).also { workId ->
                viewModelScope.launch {
                    WorkProgressObserver.observe(workmanager, workId)
                }
            }
        }
        return WorkProgressObserver.workState

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

