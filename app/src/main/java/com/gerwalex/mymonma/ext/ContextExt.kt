package com.gerwalex.mymonma.ext

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.gerwalex.mymonma.ComposeActivity
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.main.App

fun Context.getActivity(): AppCompatActivity? = when (this) {
    is AppCompatActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}

/**
 * Check for Permission
 */
fun Context.hasPermissions(vararg permissions: String) = permissions.all { permission ->
    ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}

@SuppressLint("MissingPermission")
fun Context.createNotification(title: String, text: String?) {
    if (hasPermissions((Manifest.permission.POST_NOTIFICATIONS))) {
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

        NotificationManagerCompat
            .from(this)
            .notify(R.id.notifiy_exec_Import, builder.build())

    }
}
