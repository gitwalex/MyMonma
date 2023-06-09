package com.gerwalex.mymonma.database.tables

import android.database.Cursor
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.gerwalex.mymonma.database.ObservableTableRowNew
import java.sql.Date

@Entity(
    foreignKeys = [ForeignKey(
        entity = Account::class,
        parentColumns = ["id"],
        childColumns = ["accountid"],
        onDelete = ForeignKey.RESTRICT,
        onUpdate = ForeignKey.CASCADE,
        deferred = true
    ), ForeignKey(
        entity = Cat::class,
        parentColumns = ["id"],
        childColumns = ["catid"],
        onDelete = ForeignKey.RESTRICT,
        onUpdate = ForeignKey.CASCADE,
        deferred = true
    ), ForeignKey(
        entity = Partnerstamm::class,
        parentColumns = ["id"],
        childColumns = ["partnerid"],
        onDelete = ForeignKey.RESTRICT,
        onUpdate = ForeignKey.CASCADE,
        deferred = true
    )]
)

data class CashTrx(

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    val btag: Date = Date(System.currentTimeMillis()),
    @ColumnInfo(index = true)
    var accountid: Long = -1,
    @ColumnInfo(index = true)
    var catid: Long = 0,
    @ColumnInfo(index = true)
    var partnerid: Long = 0,
    var amount: Long = 0,
    var memo: String? = null,
    var transferid: Long? = null
) : ObservableTableRowNew() {
    @Ignore
    var accountname: String? = null

    @Ignore
    var partnername: String? = null

    @Ignore
    var catname: String? = null

    @Ignore
    var imported: Boolean = false

    @Ignore
    var saldo: Long? = 0

    @Ignore
    constructor(c: Cursor) : this() {
        fillContent(c)
        id = getAsLong("id")
        accountid = getAsLong("accountid")
        catid = getAsLong("catid")
        partnerid = getAsLong("partnerid")
        amount = getAsLong("amount")
        memo = getAsString("memo")
        transferid = getAsLong("transferid")
    }
}

