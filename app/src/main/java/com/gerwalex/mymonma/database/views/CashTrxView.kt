package com.gerwalex.mymonma.database.views

import android.database.Cursor
import com.gerwalex.mymonma.database.ObservableTableRowNew
import java.math.BigDecimal
import java.sql.Date

data class CashTrxView(
    var id: Long? = null,
    var btag: Date = Date(System.currentTimeMillis()),
    var account: String = "Unknown",
    var cat: String = "Unknown",
    var partner: String = "Unknown",
    var amount: BigDecimal = BigDecimal(0),
    var memo: String? = null,
    var transferid: Long? = null,
    var newTrx: Boolean = true,
    var imported: Boolean = false,
    var saldo: BigDecimal = BigDecimal(0)
) : ObservableTableRowNew() {
    constructor(c: Cursor) : this(null) {
        fillContent(c)
        id = getAsLongOrNull("_id")
        btag = getAsDate("btag")!!

    }
}
