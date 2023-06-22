package com.gerwalex.mymonma

import com.gerwalex.mymonma.database.tables.CashTrx
import com.gerwalex.mymonma.database.tables.Cat
import com.gerwalex.mymonma.database.views.CashTrxView

object TestData {

    /**
     * Barverfügung 100,00 mit neuem Partner
     */
    fun createSimpleCashTrx(acountid: Long): CashTrx {
        return CashTrx(
            accountid = acountid,
            catid = 10009,
            partnerid = 0,
            partnername = "ein neuer Partner",
            amount = 100_00L,
            memo = "my Memo",


            )
    }

    /**
     * SplitUmsatz 1000,00 mit neuem Partner
     * Umbuchung 300,00 auf accountid2
     * Bar 700,00
     */

    fun createSplittCashTrx(accountid1: Long, accountid2: Long): List<CashTrxView> {
        val main = CashTrxView(
            accountid = accountid1,
            catid = Cat.SPLITBUCHUNGCATID,
            partnerid = 0,
            partnername = "ein weiterer neuer Partner",
            amount = 1000_00L,
            memo = "my Memo",
        )
        val line1 = main.copy(
            accountid = accountid1,
            catid = accountid2,
            memo = "Umbuchung",
            amount = 300_00L,
            isUmbuchung = false
        )
        val line2 = main.copy(
            catid = 10009,
            memo = "bar",
            amount = 700_00L,
        )
        val umbuchung = main.copy(
            accountid = accountid2,
            catid = accountid1,
            memo = "Umbuchung",
            amount = -300_00L,
            isUmbuchung = true
        )
        line1.gegenbuchung = umbuchung
        return listOf(main, line1, line2)
    }
}