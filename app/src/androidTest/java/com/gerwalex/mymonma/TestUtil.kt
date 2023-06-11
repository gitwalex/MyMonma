package com.gerwalex.mymonma

import com.gerwalex.mymonma.database.tables.Cat
import com.gerwalex.mymonma.database.views.CashTrxView

object TestData {

    fun createSimpleCashTrx(): CashTrxView {
        return CashTrxView(
            accountid = 2,
            catid = 10009,
            partnerid = 0,
            partnername = "eine neuer Partner",
            amount = 10000L,
            memo = "my Memo",


            )
    }

    fun createSplittCashTrx(): List<CashTrxView> {
        val main = CashTrxView(
            accountid = 2,
            catid = Cat.SPLITBUCHUNGCATID,
            partnerid = 0,
            partnername = "eine weiterer neuer Partner",
            amount = 10000L,
            memo = "my Memo",
        )
        val line1 = CashTrxView(
            accountid = 2,
            catid = 11,
            partnerid = 0,
            partnername = "eine weiterer neuer Partner",
            amount = 30000L,
            memo = "umbuchung",
        )
        val line2 = CashTrxView(
            accountid = 2,
            catid = 10009,
            partnerid = 0,
            partnername = "eine weiterer neuer Partner",
            amount = 70000L,
            memo = "umbuchung",
        )
        return listOf(main, line1, line2)


    }
}