package com.gerwalex.mymonma.database.views

import androidx.room.DatabaseView
import com.gerwalex.mymonma.database.tables.CashTrx
import com.gerwalex.mymonma.database.tables.TrxRegelm
import java.sql.Date

@DatabaseView(
    "select a.*, " +
            "p.name as partnername, acc.name as accountname, c.name as catname, " +
            "c.catclassid, 0 as imported, 0 as saldo " +
            "from CashTrx a " +
            "left join Partnerstamm p on p.id = partnerid " +
            "left join Cat acc on   acc.id = accountid " +
            "left join Cat c on c.id = catid "
)
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
    var isUmbuchung: Boolean = false,
    var importTrxID: Long? = null,
) {
    fun toCashTrx(): CashTrx {
        return CashTrx(
            id = id,
            btag = btag,
            accountid = accountid,
            catid = catid,
            partnerid = partnerid,
            amount = amount,
            memo = memo,
            transferid = transferid,

            )
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

    companion object {
        fun fromRegelmTrx(trx: TrxRegelm) {

        }
    }
}
