package com.gerwalex.monmang.database.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import com.gerwalex.mymonma.database.tables.CashTrx
import com.gerwalex.mymonma.database.tables.ImportAccount
import java.sql.Date

@Entity(
    foreignKeys = [ForeignKey(
        entity = ImportAccount::class,
        parentColumns = ["id"],
        childColumns = ["accountid"],
        onDelete = ForeignKey.RESTRICT,
        onUpdate = ForeignKey.CASCADE,
    ), ForeignKey(
        entity = CashTrx::class,
        parentColumns = ["id"],
        childColumns = ["umsatzid"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE,
    )],
    indices = [Index(value = ["umsatzid"], unique = true)]
)
data class ImportTrx(

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var btag: Date = Date(System.currentTimeMillis()),
    @ColumnInfo(index = true)
    var accountid: Long,
    var partnername: String?,
    var amount: Long = 0L,
    var bonus: Long = 0,
    var memo: String? = null,
    var newTrx: Boolean = true,
    var vormerkung: Boolean = false,

    /**
     * ID des dazugehoerigen Umsatzes
     */
    var umsatzid: Long? = null,
    var bankverbindung: String? = null,

    /**
     * Enthaelt ggfs. den zugehörigen CashUmsatz.
     */
) {
    @Ignore
    var cashTrans: CashTrx? = null

    fun compareTo(other: ImportTrx): Int {
        var result = btag.compareTo(other.btag)
        if (result == 0) {
            result = (amount - other.amount).toInt()
        }
        if (result == 0) {
            result = partnername!!.compareTo(other.partnername!!)
        }
        return result
    }


}