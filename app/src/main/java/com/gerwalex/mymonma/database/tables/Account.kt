package com.gerwalex.mymonma.database.tables

import android.database.Cursor
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.gerwalex.mymonma.Kontotyp
import com.gerwalex.mymonma.database.ObservableTableRowNew
import java.math.BigDecimal
import java.sql.Date

@Entity(
    foreignKeys = [ForeignKey(
        entity = CatClass::class,
        parentColumns = ["id"],
        childColumns = ["catid"],
        onDelete = ForeignKey.RESTRICT,
        onUpdate = ForeignKey.CASCADE,
        deferred = true
    )]
)
data class Account(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    @ColumnInfo(index = true)
    var catid: Long = 0,
    var name: String = "Unknown",
    var inhaber: String? = null,
    var currency: String? = null,
    var iban: String? = null,// IBAN, Kartnenummer, Kontonummer
    var blz: String? = null,
    var bezeichnung: String? = null, //Verwendungszweck

    var creditlimit: BigDecimal? = BigDecimal.ZERO,
    var verrechnungskonto: Long? = null,
    var kontotyp: Kontotyp = Kontotyp.Giro,
    var openDate: Date? = Date(System.currentTimeMillis()),

    var bankname: String? = null,
    var bic: String? = null,
    var openamount: BigDecimal = BigDecimal.ZERO,
    @Ignore
    var ausgeblendet: Boolean = false,
    @Ignore

    var cnt: Int = 0,
    @Ignore

    val supercatid: Long = 0,
    @Ignore

    val isObercat: Boolean = false,
    @Ignore

    val isVerrechnungskontoNeeded: Boolean = false,
) : ObservableTableRowNew() {


    @Ignore
    constructor(c: Cursor) : this(id = null) {
        fillContent(c)
        id = getAsLong("id")
        catid = getAsLong("catid")
        name = getAsString("name")!!
        inhaber = getAsString("inhaber")
        currency = getAsString("currency")
        iban = getAsString("iban")
        bic = getAsString("bic")
        blz = getAsString("blz")
        bezeichnung = getAsString("subnumber")
        creditlimit = getAsBigDecimalOrNull("creditlimit")
        verrechnungskonto = getAsLongOrNull("verrechnungskonto")
        openDate = getAsDate("openDate")
        openamount = getAsBigDecimal("openamount")
        bankname = getAsString("bankname")
        saldo = getAsLong("saldo")
        cnt = getAsInt("cnt")
    }

    @Ignore
    var saldo: Long = 0


}