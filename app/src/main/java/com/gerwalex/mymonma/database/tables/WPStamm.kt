package com.gerwalex.mymonma.database.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.gerwalex.mymonma.database.room.DB.dao
import com.gerwalex.mymonma.enums.WPTyp

@Entity(
    foreignKeys = [ForeignKey(
        entity = Partnerstamm::class,
        parentColumns = ["id"],
        childColumns = ["partnerid"],
        onDelete = ForeignKey.RESTRICT,
        onUpdate = ForeignKey.CASCADE,

        )]
)
data class WPStamm(

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    @ColumnInfo(index = true)
    var partnerid: Long = 0,
    var wpkenn: String? = "",
    var isin: String? = "",
    var wptyp: WPTyp = WPTyp.Aktie,
    var risiko: Int = 2,
    var beobachten: Boolean = true,
    var estEarning: Long = 0,
) {
    @Ignore
    var wpname: String? = null

    constructor(wpkenn: String, wpname: String) : this(wpkenn = wpkenn) {
        this.wpname = wpname
    }

    suspend fun insert(): WPStamm {
        id = dao.insert(this)
        return this
    }

    suspend fun update() {
        dao.update(this)
    }
}
