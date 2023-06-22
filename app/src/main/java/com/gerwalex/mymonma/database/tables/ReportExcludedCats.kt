package com.gerwalex.mymonma.database.tables

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = ReportBasisDaten::class,
        parentColumns = ["id"],
        childColumns = ["reportid"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    ), ForeignKey(
        entity = Cat::class,
        parentColumns = ["id"],
        childColumns = ["catid"],
        onDelete = ForeignKey.RESTRICT,
        onUpdate = ForeignKey.CASCADE
    )]
)

data class ReportExcludedCats(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var reportid: Long,
    var catid: Long,
)