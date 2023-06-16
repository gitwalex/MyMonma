package com.gerwalex.mymonma.main

import android.app.Application
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import cat.ereza.customactivityoncrash.config.CaocConfig
import com.gerwalex.mymonma.BuildConfig
import com.gerwalex.mymonma.database.room.DB
import com.gerwalex.mymonma.workers.MaintenanceWorker
import kotlinx.coroutines.*
import java.io.File
import java.util.*

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        CaocConfig.Builder.create()
            .backgroundMode(CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM)
            .enabled(BuildConfig.DEBUG)
            .showErrorDetails(true)
            .trackActivities(true)
            .logErrorOnRestart(true)
            .apply()

        DB.createInstance(this)
        MaintenanceWorker.enqueueMaintenanceWorker(this)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
    }

    companion object {
        val NotificationChannelID = "MonMa Next Generation"
        val linefeed = System.getProperty("line.separator")
        private val backupDirName = "backup"
        private val downloadDirName = "download"
        private val importDirName = "import"

        fun getAppBackupDir(context: Context): File {
            val fileDir = context.getExternalFilesDir(null)
            val backupDir = File(fileDir, backupDirName)
            backupDir.mkdirs()
            return backupDir
        }

        fun getAppDownloadDir(context: Context): File {
            val fileDir = context.getExternalFilesDir(null)
            val downloadDir = File(fileDir, downloadDirName)
            downloadDir.mkdirs()
            return downloadDir
        }

        fun getAppImportDir(context: Context): File {
            val fileDir = context.getExternalFilesDir(null)
            val importDir = File(fileDir, importDirName)
            importDir.mkdirs()
            return importDir
        }

        const val LowImportantChannelID = "LowImportantChannelID"
        const val DebugChannelID = "DebugChannelID"
        const val HighImportantChannelID = "HighImportantChannelID"
    }


}