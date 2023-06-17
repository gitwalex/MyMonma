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
import androidx.preference.PreferenceManager
import com.gerwalex.mymonma.ComposeActivity
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.main.App

fun Context.getActivity(): AppCompatActivity? = when (this) {
    is AppCompatActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}

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
