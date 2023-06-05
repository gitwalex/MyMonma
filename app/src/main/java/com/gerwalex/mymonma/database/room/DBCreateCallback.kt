package com.gerwalex.mymonma.database.room

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

internal class DBCreateCallback(context: Context) : RoomDatabase.Callback() {

    private val context: Context = context.applicationContext
    private fun createReportBasisdaten(database: SupportSQLiteDatabase) {
        val sql =
            "insert into ReportExcludedCats  (reportid, catID) " + "select '-1' as reportid, _id as catID " +
                    "from Type_Cat where ausgeblendet and catclassid " + "IN (15,16)"
        database.execSQL(sql)
    }

    /**
     * Liest die Cats und Clases aus den csv-File, belegt Partnerstammdaten mit Text 'nicht
     * angegeben' und _id = 0
     *
     * @param db database
     */
    private fun createStammdaten(db: SupportSQLiteDatabase) {
        try {
            Log.d("gerwalex", "Lade csv-daten")
            var `in`: InputStream
            db.beginTransaction()
            val am = context.resources.assets
            `in` = am.open("partnerstamm.csv")
            loadCSVFile(`in`, db, "Partnerstamm")
            db.setTransactionSuccessful()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            db.endTransaction()
        }
    }

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        try {
            db.beginTransaction()
            createStammdaten(db)
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.endTransaction()
        }
    }

    override fun onOpen(db: SupportSQLiteDatabase) {
    }

    /**
     * Laedt ein CSV in eine Tabelle.
     *
     * @param in        Eingabe
     * @param tablename Tabellenname
     * @param database  database
     */
    fun loadCSVFile(`in`: InputStream, database: SupportSQLiteDatabase, tablename: String) {
        val cv = ContentValues()
        try {
            Log.d("gerwalex", "Lade Tabelle $tablename")
            val buffer = BufferedReader(InputStreamReader(`in`))
            val colnames = buffer.readLine().split(";".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            var line: String?
            while (buffer.readLine().also { line = it } != null) {
                line?.let { current ->
                    val csvcolumns: Array<String?> =
                        current.split(";".toRegex()).dropLastWhile { it.isEmpty() }
                            .toTypedArray()
                    for (i in csvcolumns.indices) {
                        if (csvcolumns[i] != null) {
                            csvcolumns[i] = csvcolumns[i]!!.trim { it <= ' ' }
                            cv.put(colnames[i], csvcolumns[i])
                        }
                    }
                }
                database.insert(tablename, SQLiteDatabase.CONFLICT_NONE, cv)
                cv.clear()
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            Log.d("gerwalex", "Werte: $cv")
        } finally {
            try {
                `in`.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

}
