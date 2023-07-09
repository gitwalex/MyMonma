package com.gerwalex.mymonma.workers

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.sqlite.db.SupportSQLiteOpenHelper
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.Operation
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.gerwalex.mymonma.database.room.DB
import com.gerwalex.mymonma.ext.dataStore
import com.gerwalex.mymonma.preferences.PreferenceKey
import java.util.Date
import java.util.concurrent.TimeUnit


class MaintenanceWorker(context: Context, params: WorkerParameters) : CoroutineWorker(
    context.applicationContext,
    params
) {
   private val context = context.applicationContext

    private val sqLiteOpenHelper: SupportSQLiteOpenHelper = DB.get().openHelper


    override suspend fun doWork(): Result {
        return try {
            RegelmTrxWorker.doWork(context)
            val database = sqLiteOpenHelper.writableDatabase
            database.execSQL("analyze")
            database.execSQL("vacuum")
            context.dataStore.edit {
                it[PreferenceKey.LastMaintenance] = Date().time
            }
            Result.success()

        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }

    companion object {

        private val tag: String = MaintenanceWorker::class.java.name
        fun enqueueMaintenanceWorker(context: Context): Operation {
            val maintenanceWork = PeriodicWorkRequestBuilder<MaintenanceWorker>(24, TimeUnit.HOURS)
                .build()
            return WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                tag,
//                ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
                ExistingPeriodicWorkPolicy.KEEP,
                maintenanceWork
            )
        }

        fun cancel(context: Context) {
            WorkManager.getInstance(context).cancelAllWorkByTag(tag)
        }
    }

}