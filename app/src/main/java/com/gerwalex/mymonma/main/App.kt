package com.gerwalex.mymonma.main

import android.app.Application
import android.os.Build
import android.preference.PreferenceManager
import androidx.annotation.RequiresApi
import com.gerwalex.mymonma.database.room.DB
import kotlinx.coroutines.*
import java.util.*

class App : Application() {

    private val prefs by lazy {
        PreferenceManager.getDefaultSharedPreferences(this)
    }

    override fun onCreate() {
        super.onCreate()
        DB.createInstance(this)
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