package com.gerwalex.mymonma.database.tables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.database.room.DB.dao
import com.gerwalex.mymonma.enums.WPTyp
import com.gerwalex.mymonma.ui.content.AutoCompleteTextView

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

@Composable
fun AutoCompleteWPStammView(filter: String, selected: (Partnerstamm) -> Unit) {
    var wpstammname by remember { mutableStateOf(filter) }
    var showDropdown by remember { mutableStateOf(true) }
    val data by dao.getWPStammlist(wpstammname).collectAsState(initial = emptyList())


    AutoCompleteTextView(
        modifier = Modifier.fillMaxWidth(),
        query = wpstammname,
        queryLabel = stringResource(id = R.string.wpstamm),
        onQueryChanged = {
            wpstammname = it
        },
        list = data,
        onClearClick = { wpstammname = "" },
        onDismissRequest = { },
        onItemClick = { wpstamm ->
            wpstammname = wpstamm.name
            selected(wpstamm)
            showDropdown = false
        },
        onFocusChanged = { hasFocus ->
            showDropdown = hasFocus
        }
    ) { wpstamm ->
        Text(wpstamm.name, fontSize = 14.sp)
    }
}
