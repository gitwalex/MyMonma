package com.gerwalex.mymonma.database.tables

import android.database.Cursor
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.gerwalex.mymonma.database.ObservableTableRowNew

@Entity
class Partnerstamm(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null,
    var name: String = "Unknown"
) : ObservableTableRowNew() {

    @Ignore
    constructor(c: Cursor) : this(null) {
        id = getAsLong("id")
        name = getAsString("name")!!
    }
}