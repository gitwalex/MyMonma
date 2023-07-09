package com.gerwalex.mymonma.workers

import android.content.Context
import android.text.format.DateUtils
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.database.room.DB.Companion.dao
import com.gerwalex.mymonma.database.room.MyConverter
import com.gerwalex.mymonma.ext.createNotification
import com.gerwalex.mymonma.ext.dataStore
import com.gerwalex.mymonma.main.App
import com.gerwalex.mymonma.preferences.PreferenceKey
import java.sql.Date

object RegelmTrxWorker {
    private val msgId = R.id.notifiy_exec_regelmBuchung


    suspend fun doWork(context: Context) {
        try {
            context.dataStore.data.collect {

                val anzahlTage = it[PreferenceKey.AnzahlTage] ?: 3
                val date = Date(System.currentTimeMillis() + DateUtils.DAY_IN_MILLIS * anzahlTage)
                dao.getNextRegelmTrx(date).also { list ->
                    if (list.isNotEmpty()) {
                        val messages = StringBuilder()
                        list.forEach { trx ->
                            dao.execute(trx)
                            messages
                                .append(trx.partnername)
                                .append(": ")
                                .append(MyConverter.convertToCurrency(trx.amount))
                                .append(App.linefeed)

                        }
                        val title = context.getString(R.string.event_dauerauftrag)
                        val text = context.getString(
                            R.string.execute_regelTrx_anzahl,
                            list.size
                        )
                        context.createNotification(msgId, title, text, messages.toString())
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}