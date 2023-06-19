package com.gerwalex.mymonma.importer

import com.gerwalex.monmang.database.tables.ImportNewTrx
import com.gerwalex.mymonma.database.tables.ImportAccount
import java.io.*
import java.nio.charset.StandardCharsets
import java.sql.Date
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Comdirect : ImportClass {

    private val df: DateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    @Throws(IOException::class)
    override fun readCashTrx(account: ImportAccount, file: File): List<ImportNewTrx> {
        val list: MutableList<ImportNewTrx> = ArrayList()
        try {
            BufferedReader(
                InputStreamReader(
                    FileInputStream(file),
                    StandardCharsets.ISO_8859_1
                )
            ).use {
                it.forEachLine { line ->
                    val split = line
                        .replace("\"".toRegex(), "")
                        .split(";")
                        .toTypedArray()
                    if (split.size > 5 && !split[0].startsWith("Buchungstag")) {
                        val umsatz = ImportNewTrx(accountid = account.id!!)
                        setAmount(umsatz, split[4])
                        setBtag(umsatz, split[0])
                        fillUmsatz(umsatz, split[3])
                        list.add(umsatz)
                    }
                }
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        list.reverse()
        return list
    }

    private fun fillUmsatz(umsatz: ImportNewTrx, field: String) {
        var temp = field.split("Ref.")
        if (temp.size > 1) {
            // Referenz in temp[1]
        }

        temp = temp[0].split("Buchungstext:")
        if (temp.size > 1) {
            umsatz.memo = temp[1]
        }
        temp = temp[0].split("/")
        if (temp.size > 1) {
            umsatz.bankverbindung = temp[1]
            // Wenn die Bankverbindung vorhanden ist, befndet sich
        }
        temp = temp[0].split(":")
        if (temp.size > 1) {
            umsatz.partnername = temp[1]
        }
        if (umsatz.vormerkung) {
            // Bei einer Vormaerkung ist der Partnername im Meno vor dem ">"
            umsatz.memo?.let {
                temp = it.split(">")
                umsatz.partnername = temp[0]
                if (temp.size > 1) {
                    umsatz.memo = temp[1]
                }
            }
        }
    }

    private fun setBtag(umsatz: ImportNewTrx, field: String) {
        var time: Long? = null
        try {
            time = df.parse(field)?.time
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        if (time == null) {
            time = System.currentTimeMillis()
            umsatz.vormerkung = true
        }
        umsatz.btag = Date(time)
    }

    private fun setAmount(umsatz: ImportNewTrx, field: String) {
        umsatz.amount = field
            .replace("[,.]".toRegex(), "")
            .toLong()
    }
}