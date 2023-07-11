package com.gerwalex.mymonma.main

import android.util.Log
import androidx.lifecycle.Observer
import androidx.work.WorkInfo
import androidx.work.WorkManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import java.util.UUID

object WorkProgressObserver {
    private lateinit var workmanager: WorkManager
    private var lastWorkId: UUID? = null
    private val workInfoObserver = Observer { workInfo: WorkInfo? ->
        onWorkInfoChanged(workInfo)
    }
    val workState = MutableStateFlow(WorkProgressState())

    private fun onWorkInfoChanged(workInfo: WorkInfo?) {
        workInfo?.let {
            Log.d("MonMaViewModel", "onWorkInfoChanged: $workInfo")
            val progress = workInfo.progress.getInt(MonMaViewModel.KEY_PROGRESS, 0)
            workState.value = workState.value.copy(
                progress = progress,
                state = workInfo.state
            )
        }
    }

    private fun removeObserverFromLastWork() {
        Log.d("WorkProgressObserver", "removeObserverFromLastWork called")
        lastWorkId?.let { workId: UUID ->
            workmanager
                .getWorkInfoByIdLiveData(workId)
                .removeObserver(workInfoObserver)
        }
    }

    suspend fun observe(workmanager: WorkManager, workId: UUID) {
        this.workmanager = workmanager
        withContext(Dispatchers.Main) {
            removeObserverFromLastWork()
            workmanager
                .getWorkInfoByIdLiveData(workId)
                .observeForever(workInfoObserver)
        }
    }

    fun onCleared() {
        removeObserverFromLastWork()
    }

    data class WorkProgressState(
        val state: WorkInfo.State = WorkInfo.State.ENQUEUED,
        val progress: Int = -1,
    )
}