package com.gerwalex.mymonma.database.tables

import android.database.Cursor
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.gerwalex.mymonma.Kontotyp
import com.gerwalex.mymonma.database.ObservableTableRowNew
import java.sql.Date

data class Account(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var name: String = "Unknown",
    var inhaber: String? = null,
    var currency: String? = null,
    var iban: String? = null,// IBAN, Kartnenummer, Kontonummer
    var blz: String? = null,
    var bezeichnung: String? = null, //Verwendungszweck

    var creditlimit: Long? = 0,
    var verrechnungskonto: Int? = null,
    var kontotyp: Kontotyp = Kontotyp.Giro,
    var openDate: Date? = Date(System.currentTimeMillis()),

    var bankname: String? = null,
    var bic: String? = null,
    var openamount: Long = 0,

    var ausgeblendet: Boolean = false,

    var cnt: Int = 0,

    val supercatid: Long = 0,

    val isObercat: Boolean = false,

    val isVerrechnungskontoNeeded: Boolean = false,
) : ObservableTableRowNew() {


    @Ignore
    constructor(c: Cursor) : this(id = null) {
        fillContent(c)
        id = getAsLong("id")
        name = getAsString("name")!!
        inhaber = getAsString("inhaber")
        currency = getAsString("currency")
        iban = getAsString("iban")
        bic = getAsString("bic")
        blz = getAsString("blz")
        bezeichnung = getAsString("subnumber")
        creditlimit = getAsLong("creditlimit")
        verrechnungskonto = getAsIntOrNull("verrechnungskonto")
        openDate = getAsDate("openDate")
        openamount = getAsLong("openamount")
        bankname = getAsString("bankname")
        val obercatid = getAsLong("obercatid")
        saldo = getAsLong("saldo")
        cnt = getAsInt("cnt")
        ausgeblendet = getAsBoolean("ausgeblendet")
    }

    @Ignore
    var saldo: Long = 0


}