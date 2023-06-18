package com.gerwalex.mymonma.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.gerwalex.mymonma.importer.FinanztreffOnline
import com.gerwalex.mymonma.importer.TrxImporter

class FileImportWorker(private val context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(
        context, workerParams
    ) {


    override suspend fun doWork(): Result {
        var result: Result
        val messages: MutableList<String> = ArrayList()
        try {
            FinanztreffOnline(context).doImport(messages)
            TrxImporter(context).executeImport()
            result = Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            result = Result.retry()
        }
        return result
    }
}