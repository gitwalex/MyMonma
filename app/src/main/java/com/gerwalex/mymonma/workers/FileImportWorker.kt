package com.gerwalex.mymonma.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.gerwalex.monmang.importer.FinanztreffOnline

class FileImportWorker(private val context: Context, workerParams: WorkerParameters) : Worker(
    context, workerParams
) {


    override fun doWork(): Result {
        var result: Result
        val messages: MutableList<String> = ArrayList()
        try {
            FinanztreffOnline(context).doImport(messages)
            result = Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            result = Result.retry()
        }
        return result
    }
}