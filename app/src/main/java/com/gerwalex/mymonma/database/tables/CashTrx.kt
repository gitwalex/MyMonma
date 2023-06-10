package com.gerwalex.mymonma.database.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.gerwalex.mymonma.ui.screens.CashTrxView
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
    var btag: Date = Date(System.currentTimeMillis()),
    @ColumnInfo(index = true)
    var accountid: Long = -1,
    @ColumnInfo(index = true)
    var catid: Long = 0,
    @ColumnInfo(index = true)
    var partnerid: Long = 0,
    var amount: Long = 0,
    var memo: String? = null,
    var transferid: Long? = null
) {
    constructor(cashTrxView: CashTrxView) : this(cashTrxView.id) {
        btag = cashTrxView.btag
        accountid = cashTrxView.accountid
        catid = cashTrxView.catid
        partnerid = cashTrxView.partnerid
        amount = cashTrxView.amount
        memo = cashTrxView.memo
        transferid = cashTrxView.transferid
    }
}

