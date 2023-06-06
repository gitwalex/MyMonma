package com.gerwalex.mymonma.database.tables

import android.database.Cursor
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.gerwalex.mymonma.database.ObservableTableRowNew

@Entity
data class CatClass(

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var name: String? = "Unknown",
    var description: String? = "",
    var intern: Boolean = false,
) : ObservableTableRowNew() {

    @Ignore
    constructor(c: Cursor) : this(null) {
        fillContent(c)
        id = getAsLong("id")
        name = getAsString("name")
        description = getAsString("description")
        intern = getAsBoolean("intern")
    }
}