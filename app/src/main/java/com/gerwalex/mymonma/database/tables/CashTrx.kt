package com.gerwalex.mymonma.database.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.gerwalex.monmang.database.tables.ImportTrx
import java.sql.Date

@Entity(
    foreignKeys = [ForeignKey(
        entity = Account::class,
        parentColumns = ["id"],
        childColumns = ["accountid"],
        onDelete = ForeignKey.RESTRICT,
        onUpdate = ForeignKey.CASCADE,

        ), ForeignKey(
        entity = Cat::class,
        parentColumns = ["id"],
        childColumns = ["catid"],
        onDelete = ForeignKey.RESTRICT,
        onUpdate = ForeignKey.CASCADE,

        ), ForeignKey(
        entity = CashTrx::class,
        parentColumns = ["id"],
        childColumns = ["transferid"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE,

        ), ForeignKey(
        entity = Partnerstamm::class,
        parentColumns = ["id"],
        childColumns = ["partnerid"],
        onDelete = ForeignKey.RESTRICT,
        onUpdate = ForeignKey.CASCADE,

        )]
)

data class CashTrx(

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
    val importTrxID: Long? = null
) {
    @Ignore
    var partnername: String? = null

    @Ignore
    var catclassid: Long? = null

    constructor(importTrx: ImportTrx) : this(accountid = importTrx.accountid) {
        btag = importTrx.btag
        amount = importTrx.amount
        memo = importTrx.memo

    }

    /**
     * Gegenbuchung zur Buchung.
     * Übernahme aller Daten 1:1, Tausch accountid <-> catid,
     * transferid entspricht id des Umsatzes, id ist null
     */
    fun toGegenbuchung(): CashTrx {
        return CashTrx(
            btag = btag,
            catid = accountid,
            accountid = catid,
            partnerid = partnerid,
            amount = -amount,
            memo = memo,
            transferid = id,
            isUmbuchung = true,
        )
    }

}

