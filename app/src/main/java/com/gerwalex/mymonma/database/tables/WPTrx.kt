package com.gerwalex.mymonma.database.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.sql.Date

@Entity(
    foreignKeys = [ForeignKey(
        entity = CashTrx::class,
        parentColumns = ["id"],
        childColumns = ["id"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    ), ForeignKey(
        entity = WPTrx::class,
        parentColumns = ["id"],
        childColumns = ["paketid"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
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
        entity = WPStamm::class,
        parentColumns = ["id"],
        childColumns = ["wpid"],
        onDelete = ForeignKey.RESTRICT,
        onUpdate = ForeignKey.CASCADE
    )]
)

data class WPTrx(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var btag: Date = Date(System.currentTimeMillis()),
    @ColumnInfo(index = true)
    var accountid: Long = -1,
    @ColumnInfo(index = true)
    var wpid: Long = -1,
    @ColumnInfo(index = true)
    var catid: Long = 0,
    @ColumnInfo(index = true)
    var paketid: Long? = null,
    var kurs: Long = 0L,
    var menge: Long? = null,
    var ertrag: Long = 0,
    var einstand: Long? = null,
    var zinszahl: Long? = null,
    var haltedauer: Long? = null,
    @Ignore
    var amount: Long = 0,
    @Ignore
    var abgeltungssteuer: Long = 0,

    ) {
    val ausmachenderBetrag: Long
        get() {
            return amount + abgeltungssteuer
        }

}