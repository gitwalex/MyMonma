package com.gerwalex.mymonma.importer

import android.text.TextUtils
import com.gerwalex.monmang.database.tables.ImportNewTrx
import com.gerwalex.mymonma.database.tables.ImportAccount
import java.io.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AmazonKK : ImportClass {

    private val df: DateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    @Throws(IOException::class)
    override fun readCashTrx(account: ImportAccount, file: File): List<ImportNewTrx> {
        val list: MutableList<ImportNewTrx> = ArrayList()
        try {
            FileInputStream(file).use { stream ->
                val buffer = BufferedReader(InputStreamReader(stream))
                // erste zwei Zeilen ueberlesen
                buffer.readLine()
                buffer.readLine()
                buffer.forEachLine { line ->
                    val split = line
                        .replace("\"", "")
                        .trim { it <= ' ' }
                        .split(";")
                        .toTypedArray()
                    val amount = getAmount(split[8])
                    if (amount != 0L) {
                        val partner = getPartner(split[3])
                        val btag: Date?
                        val vormerkung: Boolean
                        if (!TextUtils.isEmpty(split[2])) {
                            btag = df.parse(split[2])
                            vormerkung = false
                        } else {
                            btag = df.parse(split[1])
                            vormerkung = true
                        }
                        btag?.let {
                            val umsatz = ImportNewTrx(
                                accountid = account.id!!,
                                btag = java.sql.Date(btag.time),
                                partnername = partner,
                                amount = amount
                            )
                            umsatz.vormerkung = vormerkung
                            if (split.size > 9) {
                                umsatz.bonus += if (TextUtils.isEmpty(split[9])) 0 else split[9].toLong()
                            }
                            if (split.size > 10) {
                                umsatz.bonus += if (TextUtils.isEmpty(split[10])) 0 else split[10].toLong()
                            }
                            list.add(umsatz)

                        }
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            throw IOException(e)
        }
        list.reverse()
        return list
    }

    private fun getPartner(partner: String): String {
        return if (TextUtils.isEmpty(partner)) "Umbuchung" else partner
    }

    private fun getAmount(value: String): Long {
        return if (TextUtils.isEmpty(value)) 0L
        else -value
            .replace("[,.]".toRegex(), "")
            .toLong()
    }
}