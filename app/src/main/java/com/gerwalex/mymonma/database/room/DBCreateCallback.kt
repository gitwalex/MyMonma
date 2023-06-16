package com.gerwalex.mymonma.database.room

import android.content.Context
import android.util.Log
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import java.io.InputStream

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
        Log.d("gerwalex", "Lade csv-daten")
        var `in`: InputStream
        val am = context.resources.assets
        `in` = am.open("init_partnerstamm.csv")
        FileUtils.loadCSVFile(`in`, db, "partnerstamm")
        `in` = am.open("init_catclass.csv")
        FileUtils.loadCSVFile(`in`, db, "catclass")
        `in` = am.open("init_cat.csv")
        FileUtils.loadCSVFile(`in`, db, "cat")
        `in` = am.open("init_wpstamm.csv")
        FileUtils.loadCSVFile(`in`, db, "WPStamm")
        db.setTransactionSuccessful()
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
        db.execSQL("analyze")

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

}
