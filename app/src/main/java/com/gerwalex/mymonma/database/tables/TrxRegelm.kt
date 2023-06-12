package com.gerwalex.mymonma.database.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.sql.Date

@Entity(
    foreignKeys = [ForeignKey(
        entity = TrxRegelm::class,
        parentColumns = ["id"],
        childColumns = ["transferid"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE,
        deferred = true
    ), ForeignKey(
        entity = Account::class,
        parentColumns = ["id"],
        childColumns = ["accountid"],
        onDelete = ForeignKey.RESTRICT,
        onUpdate = ForeignKey.CASCADE
    ), ForeignKey(
        entity = Cat::class,
        parentColumns = ["id"],
        childColumns = ["catid"],
        onDelete = ForeignKey.RESTRICT,
        onUpdate = ForeignKey.CASCADE
    ), ForeignKey(
        entity = Partnerstamm::class,
        parentColumns = ["id"],
        childColumns = ["partnerid"],
        onDelete = ForeignKey.RESTRICT,
        onUpdate = ForeignKey.CASCADE
    )]
)
data class TrxRegelm(
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

    @Ignore
    var accountname: String? = null,

    @Ignore
    var catname: String? = null,


    )