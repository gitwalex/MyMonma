package com.gerwalex.monmang.database.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.gerwalex.mymonma.database.tables.ImportAccount
import java.sql.Date

@Entity(
    foreignKeys = [ForeignKey(
        entity = ImportAccount::class,
        parentColumns = ["id"],
        childColumns = ["accountid"],
        onDelete = ForeignKey.RESTRICT,
        onUpdate = ForeignKey.CASCADE,
    )
    ]
)
data class ImportNewTrx(

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var btag: Date = Date(System.currentTimeMillis()),
    @ColumnInfo(index = true)
    var accountid: Long,
    var partnername: String? = null,
    var amount: Long = 0L,

    var bonus: Long = 0,
    var memo: String? = null,

    var vormerkung: Boolean = false,
    var bankverbindung: String? = null,
) {
    @Ignore
    var cnt: Long = 0L
    fun getImportTrx(): ImportTrx {
        return ImportTrx(
            btag = btag,
            accountid = accountid,
            partnername = partnername,
            amount = amount,
            bonus = bonus,
            memo = memo,
            vormerkung = vormerkung,
            bankverbindung = bankverbindung
        )
    }
}
