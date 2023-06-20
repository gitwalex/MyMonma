package com.gerwalex.mymonma.database.views

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.gerwalex.mymonma.enums.Intervall
import java.sql.Date

@DatabaseView(
    """    
        SELECT a.* ,
        p.name as partnername, acc.name as accountname, c.name as catname 
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

    var partnername: String? = null,
    var accountname: String? = null,

    var catname: String? = null,


    ) {
    @Ignore
    val intervall: Intervall = Intervall.values()[intervallid]

    @Ignore
    val intervallname = intervall.intervallNameTextResID

    fun toCashTrx(): CashTrxView {
        return CashTrxView(
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
     * Liefert den nächsten Ausführungstermin
     */
    fun nextBtag(): Date {
        return intervall.getNextBtag(btag)
    }

}