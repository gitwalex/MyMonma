package com.gerwalex.mymonma.database.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = CatClass::class,
        parentColumns = ["id"],
        childColumns = ["catclassid"],
        onDelete = ForeignKey.RESTRICT,
        onUpdate = ForeignKey.CASCADE,
    ), ForeignKey(
        entity = Cat::class,
        parentColumns = ["id"],
        childColumns = ["obercatid"],
        onDelete = ForeignKey.RESTRICT,
        onUpdate = ForeignKey.CASCADE,
    ), ForeignKey(
        entity = Cat::class,
        parentColumns = ["id"],
        childColumns = ["supercatid"],
        onDelete = ForeignKey.RESTRICT,
        onUpdate = ForeignKey.CASCADE,

        )]
)
data class Cat(

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var name: String = "",
    var description: String? = null,

    @ColumnInfo(index = true)
    var obercatid: Long = 0,

    @ColumnInfo(index = true)
    var supercatid: Long = 0,

    @ColumnInfo(index = true)
    var catclassid: Long = 0,
    var incomecat: Boolean? = null,
    var ausgeblendet: Boolean = false,

    ) {

    companion object {

        const val AbgeltSteuerCatid: Long = 2500L
        const val CASHKONTOCATID = 1001L
        const val GiroCATID = 1003L
        const val CreditCardCATID = 1004L
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

