package com.gerwalex.monmang.importer

import android.content.Context
import android.text.format.DateUtils
import android.util.Log
import com.gerwalex.mymonma.database.room.DB
import com.gerwalex.mymonma.database.tables.WPKurs
import com.gerwalex.mymonma.database.tables.WPStamm
import com.gerwalex.mymonma.main.App
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.*
import java.sql.Date
import java.util.*

class FinanztreffOnline(private val context: Context) {

    private val messages = ArrayList<String>()
    private val dao = DB.dao

    @Throws(IOException::class)
    fun doImport(messageList: MutableList<String>): Int {
        //        0:WPName, 2:WKN, 9:Datum TT.MM., 11:Kurs
        val files = App
            .getAppDownloadDir(context)
            .listFiles { _, name -> name.startsWith(prefix) }

        files?.let {
            CoroutineScope(Dispatchers.IO).launch {
                val kursList: MutableList<WPKurs> = ArrayList()
                for (file in files) {
                    Log.d("gerwalex", "Start Import Finanztreff")
                    val content: List<String> = loadFile(file)
                    content.forEach { line ->
                        val split = line
                            .trim { it <= ' ' }
                            .split(";")
                            .toTypedArray()
                        if (split.size >= 11) {
                            getWPStammId(wpname = split[0], wpkenn = split[2])?.let { wpid ->
                                try {
                                    val btag = getBtag(split[9])
                                    val amount = getAmount(split[11])
                                    val kurs = WPKurs(btag = btag, wpid = wpid, kurs = amount)
                                    kursList.add(kurs)
                                } catch (e: NumberFormatException) {
                                    e.printStackTrace()
                                    val msg = String.format("Konnte Zeile  '$line' nicht parsen")
                                    messages.add(msg)
                                    Log.d("gerwalex", msg)
                                }
                            }
                        }
                    }
                    file.delete()
                }
                if (kursList.size > 0) {
                    dao.insertKurs(kursList)
                    val yesterday = Date(System.currentTimeMillis() - DateUtils.DAY_IN_MILLIS)
//                    val oldValue = dao.getWPMarktwert(yesterday)
//                    val newValue = dao.getWPMarktwert()
//                    val diff = newValue - oldValue
//                    val percent = if (newValue == 0L) 0f else (diff.toFloat() / newValue) * 100
//                    val msg = (context.getString(
//                        R.string.eventWPKursDownload,
//                        MyConverter.convertCurrency(newValue.toFloat()),
//                        MyConverter.convertCurrency(diff.toFloat()),
//                        percent
//                    ))
//                    val title = context.getString(R.string.event_download_kurse)
//                    context.createNotification(title, msg)
                    messageList.addAll(messages)
                }
            }
        }
        return files?.size ?: 0
    }

    private fun getAmount(s: String): Long {
        val `val` = s.replace(".", "")
        val value = `val`
            .replace(",", ".")
            .toDouble()
        return value /* Buchung.currencyUnits)*/.toLong()
    }

    private fun getBtag(s: String): Date {
        val cal = GregorianCalendar.getInstance(Locale.getDefault())
        val kursdatum = s
            .split(".")
            .toTypedArray()
        val day = kursdatum[0].toInt()
        val month = kursdatum[1].toInt()
        cal[Calendar.DAY_OF_MONTH] = day
        cal[Calendar.MONTH] = month - 1
        val btag = Date(
            cal
                .time
                .time
        )
        if (btag.time > System.currentTimeMillis()) {
            val tmpCal = Calendar.getInstance()
            tmpCal.add(Calendar.YEAR, -1)
            btag.time = btag.time - tmpCal.timeInMillis
        }
        return btag
    }

    private fun loadFile(file: File): List<String> {
        FileInputStream(file).use { inStream ->
            val buffer = BufferedReader(InputStreamReader(inStream))
            val list = ArrayList<String>()
            buffer.readLine()
            buffer.forEachLine { line ->
                list.add(line)
            }
            return list
        }
    }

    companion object {

        const val prefix = "portfolio_Master_"
    }

    private suspend fun getWPStammId(wpname: String, wpkenn: String): Long? {
        if (wpname.isNotEmpty()) {
            val wpstamm =
                dao.getWPStammdaten(wpkenn).first()?.let {
                    val msg =
                        "WPStamm geändert WKN: $wpkenn Name alt: ${it.wpname}, name neu: $wpname"
                    it.wpname = wpname
                    it.update()
                    messages.add(msg)
                    Log.d("gerwalex", msg)

                } ?: let {
                    WPStamm(wpname, wpkenn).apply {
                        insert()
                    }
                }
            //return wpstamm.i        }
        }
        return null
    }
}