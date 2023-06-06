package com.gerwalex.mymonma.database.tables

import android.database.Cursor
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.gerwalex.mymonma.database.ObservableTableRowNew

@Entity(
    foreignKeys = [ForeignKey(
        entity = CatClass::class,
        parentColumns = ["id"],
        childColumns = ["catclassid"],
        onDelete = ForeignKey.RESTRICT,
        onUpdate = ForeignKey.CASCADE,
        deferred = true
    ), ForeignKey(
        entity = Cat::class,
        parentColumns = ["id"],
        childColumns = ["obercatid"],
        onDelete = ForeignKey.RESTRICT,
        onUpdate = ForeignKey.CASCADE,
        deferred = true
    ), ForeignKey(
        entity = Cat::class,
        parentColumns = ["id"],
        childColumns = ["supercatid"],
        onDelete = ForeignKey.RESTRICT,
        onUpdate = ForeignKey.CASCADE,
        deferred = true
    )]
)
data class Cat(

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var name: String? = null,
    var description: String? = null,

    @ColumnInfo(index = true)
    var obercatid: Long = 0,

    @ColumnInfo(index = true)
    var supercatid: Long = 0,

    @ColumnInfo(index = true)
    var catclassid: Long = 0,
    var incomecat: Boolean? = null,
    var ausgeblendet: Boolean = false,

    /**
     * Summe der aufgelaufenen CashTrx-Betraege
     */
    var saldo: Long = 0,

    /**
     * Antahl der CashTrx zu dieser catid
     */
    var cnt: Long = 0,
) : ObservableTableRowNew() {

    @Ignore
    constructor(c: Cursor) : this(null) {
        fillContent(c)
        id = getAsLong("id")
        name = getAsString("name")
        description = getAsString("description")
        obercatid = getAsLong("obercatid")
        supercatid = getAsLong("supercatid")
        catclassid = getAsLong("catclassid")
        incomecat = getAsBoolean("incomecat")
        ausgeblendet = getAsBoolean("ausgeblendet")
        saldo = getAsLong("saldo")
        cnt = getAsLong("cnt")
    }

    companion object {

        const val CASHKONTOCATID = 1001L
        const val CATID = "CATID"
        const val DEPOTCATID = 1002L
        const val EROEFFNUNGCAT = 9000L
        const val MAXACCOUNTNUMBER: Long = 899
        const val MINACCOUNTNUMBER: Long = 1
        const val NOCATID: Long = 0
        const val SPLITBUCHUNGCATID: Long = 1000L
        const val KONTOCLASS: Long = 2
    }
}