package com.gerwalex.mymonma.database.views

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.gerwalex.mymonma.database.room.DB
import com.gerwalex.mymonma.database.tables.CashTrx
import com.gerwalex.mymonma.database.tables.TrxRegelm
import com.gerwalex.mymonma.enums.Intervall
import java.sql.Date

@DatabaseView(
    """    
        SELECT a.* ,
        p.name as partnername, acc.name as accountname, c.name as catname,
        c.catclassid
        from TrxRegelm a 
        left join Partnerstamm p on p.id = partnerid    
        left join Cat acc on   acc.id = accountid 
        left join Cat c on c.id = catid
"""

)
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

    var partnername: String = "",
    var accountname: String = "",
    var catname: String = "",
    var catclassid: Long = 0,


    ) {
    @Ignore
    var gegenbuchung: CashTrx? = null

    @Ignore
    val intervall: Intervall = Intervall.values()[intervallid]

    @Ignore
    val intervallname = intervall.intervallNameTextResID

    val trxRegelm: TrxRegelm
        get() {
            return TrxRegelm(
                id = id,
                btag = btag,
                accountid = accountid,
                catid = catid,
                partnerid = partnerid,
                amount = amount,
                memo = memo,
                transferid = transferid,
                intervallid = intervall.ordinal
            )
        }
    val cashTrxView: CashTrxView
        get() {
            return CashTrxView(
                id = id,
                btag = btag,
                accountid = accountid,
                catid = catid,
                partnerid = partnerid,
                amount = amount,
                memo = memo,
                transferid = transferid,
                partnername = partnername,
                catname = catname,
                accountname = accountname,
                catclassid = catclassid,
            )
        }
    val cashTrx: CashTrx
        get() {
            return cashTrxView.cashTrx
        }

    /**
     * Liefert den nächsten Ausführungstermin
     */
    fun nextBtag(): Date {
        return intervall.getNextBtag(btag)
    }

    companion object {
        suspend fun insert(intervall: Intervall, accountid: Long, list: List<CashTrxView>) {
            ArrayList<TrxRegelm>().apply {
                list.forEach { cashTrx ->
                    add(TrxRegelm(intervall, accountid, cashTrx))
                }
                DB.dao.insert(this)
            }

        }

        suspend fun insert(list: List<TrxRegelmView>) {
            ArrayList<TrxRegelm>().apply {
                list.forEach { trx ->
                    add(trx.trxRegelm)
                }
                DB.dao.insert(this)
            }

        }
    }

}