package com.gerwalex.mymonma.ext

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.preference.PreferenceManager
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.await
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.database.room.DB
import com.gerwalex.mymonma.database.room.MyConverter
import com.gerwalex.mymonma.ext.FileExt.addToZip
import com.gerwalex.mymonma.main.App
import com.gerwalex.mymonma.main.ComposeActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File


fun Context.getActivity(): AppCompatActivity? = when (this) {
    is AppCompatActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
val Context.preferences: SharedPreferences
    get() = PreferenceManager.getDefaultSharedPreferences(this)

/**
 * Check for Permission
 */
fun Context.hasPermissions(vararg permissions: String) = permissions.all { permission ->
    ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}

@SuppressLint("MissingPermission")
fun Context.createNotification(
    @IdRes id: Int,
    title: String,
    text: String?,
    messages: String? = null
) {
    if (hasPermissions((Manifest.permission.POST_NOTIFICATIONS))) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_UPDATE_CURRENT or Intent.FLAG_RECEIVER_FOREGROUND or PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT or Intent.FLAG_RECEIVER_FOREGROUND
        }

        val next = Intent(this, ComposeActivity::class.java)
        val pending =
            PendingIntent.getActivity(this, R.id.notification_request, next, flags)
        val builder = NotificationCompat
            .Builder(this, App.NotificationChannelID)
            .setContentIntent(pending)
            .setAutoCancel(true)
            .setContentTitle(title)
            .setContentText(text)

            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSmallIcon(R.mipmap.euro_symbol)
        messages?.let {
            builder.setStyle(
                NotificationCompat
                    .BigTextStyle()
                    .bigText(messages)
            )
        }
        notificationManager.notify(id, builder.build())

    } else {
        Log.d("ContextExt", "createNotification: Permission missing")
    }
}

/**
 * Erstellt Backup der Datenbank dbname als zip im DBVerzeichnis
 * @return backupfile
 */
suspend fun Context.backup(dbname: String): File? {
    return withContext(Dispatchers.IO) {
        getDatabasePath(DB.DBNAME).parent?.let { db ->
            File(db).listFiles()?.also { files ->
                val targetname = "${dbname}_${MyConverter.currentTimeStamp}.zip"
                File(filesDir, targetname).addToZip(*files)
            }

        }
        null
    }
}

suspend fun Context.restore(file: File, dbname: String) {
    try {
        withContext(Dispatchers.IO) {
            getDatabasePath(dbname).parent?.let { dir ->
                file.unzipTo(dir)
            }
        }
    } catch (e: Exception) {
        Log.e("Decompress", "unzip", e)
    }
}

/**
 * Prüft auf einen Job im Workmanager
 * @param workName Name of Work
 * @return WorkInfo.State state of Work or null, when not found
 */
suspend fun Context.getUniqueWorkInfoState(workName: String): WorkInfo.State? {
    val workManager = WorkManager.getInstance(this)
    val workInfos = workManager.getWorkInfosForUniqueWork(workName).await()
    val result = if (workInfos.size == 1) {
        // for (workInfo in workInfos) {
        val workInfo = workInfos[0]
        Log.d("WorkManager", "workInfo.state=${workInfo.state}, id=${workInfo.id}")
        when (workInfo.state) {
            WorkInfo.State.ENQUEUED -> Log.d("WorkManager", "$workName is enqueued and alive")
            WorkInfo.State.RUNNING -> Log.d("WorkManager", "$workName is running and alive")
            WorkInfo.State.SUCCEEDED -> Log.d("WorkManager", "$workName has succeded")
            WorkInfo.State.FAILED -> Log.d("WorkManager", "$workName has failed")
            WorkInfo.State.BLOCKED -> Log.d("WorkManager", "$workName is blocked and Alive")
            WorkInfo.State.CANCELLED -> Log.d("WorkManager", "$workName is cancelled")
        }
        workInfo.state
    } else {
        null
    }
    return result
}






