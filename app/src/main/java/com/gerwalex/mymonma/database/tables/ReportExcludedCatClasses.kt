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
        entity = CatClass::class,
        parentColumns = ["id"],
        childColumns = ["catclassid"],
        onDelete = ForeignKey.RESTRICT,
        onUpdate = ForeignKey.CASCADE
    )]
)

data class ReportExcludedCatClasses(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var reportid: Long,
    var catclassid: Long,

    )