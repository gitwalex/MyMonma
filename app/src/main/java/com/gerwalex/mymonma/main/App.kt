package com.gerwalex.mymonma.main

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi
import cat.ereza.customactivityoncrash.config.CaocConfig
import com.gerwalex.mymonma.BuildConfig
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.database.room.DB
import com.gerwalex.mymonma.ext.preferences
import com.gerwalex.mymonma.ext.set
import com.gerwalex.mymonma.workers.KursDownloadWorker
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
        KursDownloadWorker.enqueueKursDownloadWorker(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }
        preferences.set("user", "alexwinkler")
        preferences.set("pw", "38303830")
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        // Create the Low Importance Notification channel
        // Create the High Importance Notification channel
        val audioAttributes: AudioAttributes =
            AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(
                    AudioAttributes.USAGE_NOTIFICATION
                ).build()
        val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val name = getString(R.string.high_importance_channel_name)
        val channelID = NotificationChannelID
        NotificationChannel(channelID, name, NotificationManager.IMPORTANCE_HIGH).run {
            description = getString(R.string.high_importance_channel_description)
            enableVibration(true)
            setSound(sound, audioAttributes)
            // Register the channel with the system
            notificationManager.createNotificationChannel(this)
        }

    }

    companion object {
        val NotificationChannelID = "MyMonMa Channel"
        val linefeed = System.getProperty("line.separator")
        private val backupDirName = "backup"
        private val downloadDirName = "download"
        private val importDirName = "import"

        fun getAppBackupDir(context: Context): File {
            val backupDir = File(context.filesDir, backupDirName)
            backupDir.mkdirs()
            return backupDir
        }

        fun getAppDownloadDir(context: Context): File {
            val downloadDir = File(context.filesDir, downloadDirName)
            downloadDir.mkdirs()
            return downloadDir
        }

        fun getAppImportDir(context: Context): File {
            val importDir = File(context.filesDir, importDirName)
            importDir.mkdirs()
            return importDir
        }

        const val LowImportantChannelID = "LowImportantChannelID"
        const val DebugChannelID = "DebugChannelID"
        const val HighImportantChannelID = "HighImportantChannelID"
    }


}