package com.gerwalex.mymonma.database.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = Account::class,
        parentColumns = ["id"],
        childColumns = ["verrechnungskonto"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE,
        deferred = true
    )]
)
data class ImportAccount(

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var name: String,
    var description: String? = null,
    var typ: Int = 0,
    @ColumnInfo(index = true)
    var verrechnungskonto: Long,
    var fileprefix: String,
    var classname: String,
)