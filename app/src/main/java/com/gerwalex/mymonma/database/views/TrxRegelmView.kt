package com.gerwalex.mymonma.database.views

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.gerwalex.mymonma.enums.Intervall
import java.sql.Date

data class TrxRegelmView(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var btag: Date = Date(System.currentTimeMillis()),
    @ColumnInfo(index = true)
    var accountid: Long = -1,
    @ColumnInfo(index = true)
    var catid: Long = 0,
    @ColumnInfo(index = true)
    var partnerid: Long = 0,
    var amount: Long = 0,
    var memo: String? = null,
    @ColumnInfo(index = true)
    var transferid: Long? = null,
    var isUmbuchung: Boolean? = false,

    var cnt: Int = -1,// Anzahl der durchzufuehrenden Buchungen Default -1: unendlich
    var intervallid: Int = 0, //monatlich
    var last: Date? = null,
    var isUltimo: Boolean = false,

    var partnername: String? = null,
    var accountname: String? = null,

    var catname: String? = null,


    ) {
    val intervallname: Int
        get() = Intervall.values()[intervallid].intervallNameTextResID
}