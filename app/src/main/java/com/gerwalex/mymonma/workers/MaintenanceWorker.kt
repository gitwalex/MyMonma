package com.gerwalex.mymonma.workers

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.sqlite.db.SupportSQLiteOpenHelper
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.Operation
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
            enqueueMaintenanceWorker(context, TimeUnit.DAYS.toMillis(1))
            Result.success()

        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }

    companion object {

        val tag: String = MaintenanceWorker::class.java.name
        fun enqueueMaintenanceWorker(
            context: Context,
            delay: Long = TimeUnit.HOURS.toMillis(1)
        ): Operation {
            val maintenanceWork =
                OneTimeWorkRequest.Builder(MaintenanceWorker::class.java)
                    .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                    .build()
            return WorkManager.getInstance(context).enqueueUniqueWork(
                tag,
                ExistingWorkPolicy.REPLACE,
                maintenanceWork
            )
        }

        fun cancel(context: Context) {
            WorkManager.getInstance(context).cancelAllWorkByTag(tag)
        }
    }

}