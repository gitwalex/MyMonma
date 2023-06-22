package com.gerwalex.mymonma.database.tables

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gerwalex.mymonma.enums.ReportTyp
import java.sql.Date

@Entity
data class ReportBasisDaten(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var typ: ReportTyp = ReportTyp.Geldfluss,
    var name: String,
    var von: Date,
    var bis: Date,
    var verglVon: Date? = null,
    var verglBis: Date? = null,
    var description: String? = null,
)