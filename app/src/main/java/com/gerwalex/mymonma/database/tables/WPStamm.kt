package com.gerwalex.mymonma.database.tables

import android.database.Cursor
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.database.ObservableTableRowNew
import com.gerwalex.mymonma.database.room.DB
import com.gerwalex.mymonma.database.room.MyConverter
import com.gerwalex.mymonma.enums.WPTyp
import com.gerwalex.mymonma.ui.content.AutoCompleteTextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Entity(
    foreignKeys = [ForeignKey(
        entity = Partnerstamm::class,
        parentColumns = ["id"],
        childColumns = ["partnerid"],
        onDelete = ForeignKey.RESTRICT,
        onUpdate = ForeignKey.CASCADE,
        deferred = true
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
) : ObservableTableRowNew() {

    @Ignore
    constructor(c: Cursor) : this(null) {
        fillContent(c)
        id = getAsLong("id")
        partnerid = getAsLong("partnerid")
        wpkenn = getAsString("wpkenn")
        isin = getAsString("isin")
        wptyp = MyConverter.convertWPTyp(getAsString("wptyp")!!)
        risiko = getAsInt("risiko")
        beobachten = getAsBoolean("beobachten")
        estEarning = getAsLong("estEarning")
    }
}

@Composable
fun AutoCompleteWPStammView(filter: String, selected: (Partnerstamm) -> Unit) {
    var wpstammname by remember { mutableStateOf(filter) }
    val cursor = MutableLiveData<Cursor>()
    val data by cursor.observeAsState()
    LaunchedEffect(wpstammname) {
        withContext(Dispatchers.IO) {
            val c = DB.dao.getWPStammlist(wpstammname)
            if (c.moveToFirst()) {
                val first = Partnerstamm(c)
                if (first.name == wpstammname) {
                    // Der erste Eintrag passt vollständig - nehmen
                    selected(first)
                } else {
                    selected(Partnerstamm(name = wpstammname))
                }
            }
            cursor.postValue(c)
        }
    }


    AutoCompleteTextView(
        modifier = Modifier.fillMaxWidth(),
        query = wpstammname,
        queryLabel = stringResource(id = R.string.wpstamm),
        onQueryChanged = {
            wpstammname = it
        },
        count = data?.count ?: 0,
        onClearClick = { wpstammname = "" },
        onDoneActionClick = { },
        onItemClick = { position ->
            data?.let { c ->
                if (c.moveToPosition(position)) {
                    val wpstamm = Partnerstamm(c)
                    wpstammname = wpstamm.name
                    selected(wpstamm)
                }
            }
        },
    ) { position ->
        data?.let { c ->
            if (c.moveToPosition(position)) {
                val wpstamm = Partnerstamm(c)
                Text(wpstamm.name, fontSize = 14.sp)
            }
        }
    }
}
