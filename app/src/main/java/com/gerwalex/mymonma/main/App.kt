package com.gerwalex.mymonma.main

import android.app.Application
import android.os.Build
import android.preference.PreferenceManager
import androidx.annotation.RequiresApi
import cat.ereza.customactivityoncrash.config.CaocConfig
import com.gerwalex.mymonma.BuildConfig
import com.gerwalex.mymonma.database.room.DB
import com.gerwalex.mymonma.workers.MaintenanceWorker
import kotlinx.coroutines.*
import java.util.*

class App : Application() {

    private val prefs by lazy {
        PreferenceManager.getDefaultSharedPreferences(this)
    }

    override fun onCreate() {
        super.onCreate()
        DB.createInstance(this)
        MaintenanceWorker.enqueueMaintenanceWorker(this)
        CaocConfig.Builder.create()
            .backgroundMode(CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM)
            .enabled(BuildConfig.DEBUG)
            .showErrorDetails(true)
            .trackActivities(true)
            .logErrorOnRestart(true)
            .apply()

    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
    }

    companion object {
        const val LowImportantChannelID = "LowImportantChannelID"
        const val DebugChannelID = "DebugChannelID"
        const val HighImportantChannelID = "HighImportantChannelID"
    }


}