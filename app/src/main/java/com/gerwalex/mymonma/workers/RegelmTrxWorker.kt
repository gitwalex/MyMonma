package com.gerwalex.mymonma.workers

import android.content.Context
import android.text.format.DateUtils
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.database.room.DB.dao
import com.gerwalex.mymonma.database.room.MyConverter
import com.gerwalex.mymonma.ext.createNotification
import com.gerwalex.mymonma.ext.preferences
import com.gerwalex.mymonma.main.App
import com.gerwalex.mymonma.main.PreferenceKey
import java.sql.Date

object RegelmTrxWorker {
    private val msgId = R.id.notifiy_exec_regelmBuchung


    suspend fun doWork(context: Context) {
        val anzahlTage = context.preferences.getInt(PreferenceKey.AnzahlTage, 3)
        val date = Date(System.currentTimeMillis() + DateUtils.DAY_IN_MILLIS * anzahlTage)
        dao.getNextRegelmTrx(date).also { list ->
            if (list.isNotEmpty()) {
                val title = context.getString(R.string.event_dauerauftrag)
                val text = context.getString(
                    R.string.execute_regelTrx_anzahl,
                    list.size
                )
                val messages = StringBuilder()
                list.forEach { trx ->
                    messages
                        .append(trx.partnername)
                        .append(": ")
                        .append(MyConverter.convertToCurrency(trx.amount))
                        .append(App.linefeed)
                    dao.execute(trx)

                }
                context.createNotification(msgId, title, text, messages.toString())
            }
        }
    }

}