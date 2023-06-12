package com.gerwalex.mymonma

import com.gerwalex.mymonma.database.tables.Cat
import com.gerwalex.mymonma.database.views.CashTrxView

object TestData {

    fun createSimpleCashTrx(acountid: Long): CashTrxView {
        return CashTrxView(
            accountid = acountid,
            catid = 10009,
            partnerid = 0,
            partnername = "eine neuer Partner",
            amount = 10000L,
            memo = "my Memo",


            )
    }

    fun createSplittCashTrx(acountid: Long): List<CashTrxView> {
        val main = CashTrxView(
            accountid = acountid,
            catid = Cat.SPLITBUCHUNGCATID,
            partnerid = 0,
            partnername = "ein weiterer neuer Partner",
            amount = 10000L,
            memo = "my Memo",
        )
        val line1 = main.copy(
            catid = 11,
            memo = "Umbuchung",
            amount = 30000L,
            catclassid = 2
        )
        val line2 = main.copy(
            catid = 10009,
            memo = "bar",
            amount = 70000L,
        )
        return listOf(main, line1, line2)


    }
}