package com.gerwalex.mymonma.workers

import android.content.Context
import android.text.format.DateUtils
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.database.room.DB.dao
import com.gerwalex.mymonma.database.room.MyConverter
import com.gerwalex.mymonma.ext.createNotification
import com.gerwalex.mymonma.main.App
import java.sql.Date

class RegelmTrxWorker(private val context: Context) {


    suspend fun doWork() {
        val anzahlTage = 3
        val date = Date(System.currentTimeMillis() + DateUtils.DAY_IN_MILLIS * anzahlTage)
        dao.getNextRegelmTrx(date).collect { list ->
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
            }
            context.createNotification(msgId, title, text, messages.toString())
        }
    }

    companion object {
        val msgId = R.id.notifiy_exec_regelmBuchung
    }
}