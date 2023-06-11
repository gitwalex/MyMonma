package com.gerwalex.mymonma.database.views

import androidx.room.Ignore
import com.gerwalex.mymonma.database.tables.CashTrx
import java.sql.Date

data class CashTrxView(
    var id: Long? = null,
    var btag: Date = Date(System.currentTimeMillis()),
    var accountid: Long = -1,
    var catid: Long = -1,
    var partnerid: Long = 0,
    var amount: Long = 0,
    var memo: String? = null,
    var transferid: Long? = null,
    var accountname: String = "",
    var partnername: String = "",
    var catname: String = "",
    var catclassid: Long = -1,
    var imported: Boolean = false,
    var saldo: Long? = 0,
) {
    @Ignore
    val cashtrx = CashTrx(
        id = id,
        btag = btag,
        accountid = accountid,
        catid = catid,
        partnerid = partnerid,
        amount = amount,
        memo = memo,
        transferid = transferid,
    )


    /**
     * Gegenbuchung zur Buchung.
     * Übernahme aller Daten 1:1, Tausch accountid <-> catid,
     * transferid entspricht id des Umsatzes, id ist null
     */
    @Ignore
    val gegenbuchung = CashTrx(
        btag = btag,
        catid = accountid,
        accountid = catid,
        partnerid = partnerid,
        amount = -amount,
        memo = memo,
        transferid = id,
    )
}
