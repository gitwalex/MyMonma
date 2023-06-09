package com.gerwalex.mymonma.database.tables

import android.database.Cursor
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.database.ObservableTableRowNew
import com.gerwalex.mymonma.database.room.DB.dao
import com.gerwalex.mymonma.ui.content.AutoCompleteTextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Entity
data class Partnerstamm(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null,
    var name: String = "Unknown"
) : ObservableTableRowNew() {

    @Ignore
    constructor(c: Cursor) : this(null) {
        fillContent(c)
        id = getAsLong("id")
        name = getAsString("name")!!
    }
}

@Composable
fun AutoCompletePartnerView(partnerid: Long, selected: (Partnerstamm) -> Unit) {
    val partner by dao.getPartnerstamm(partnerid).collectAsState(initial = Partnerstamm())
    AutoCompletePartnerView(filter = partner.name, selected = selected)
}

@Composable
fun AutoCompletePartnerView(filter: String, selected: (Partnerstamm) -> Unit) {
    var partnername by remember { mutableStateOf(filter) }
    val cursor = MutableLiveData<Cursor>()
    val data by cursor.observeAsState()
    LaunchedEffect(partnername) {
        withContext(Dispatchers.IO) {
            val c = dao.getPartnerlist(partnername)
            if (c.moveToFirst()) {
                val firstPartner = Partnerstamm(c)
                if (firstPartner.name == partnername) {
                    // Der erste Eintrag passt vollständig - nehmen
                    selected(firstPartner)
                } else {
                    selected(Partnerstamm(name = partnername))
                }
            }
            cursor.postValue(c)
        }
    }


    AutoCompleteTextView(
        modifier = Modifier.fillMaxWidth(),
        query = partnername,
        queryLabel = stringResource(id = R.string.partnername),
        onQueryChanged = {
            partnername = it
        },
        count = data?.count ?: 0,
        onClearClick = { partnername = "" },
        onDoneActionClick = { },
        onItemClick = { position ->
            data?.let { c ->
                if (c.moveToPosition(position)) {
                    val partner = Partnerstamm(c)
                    partnername = partner.name
                    selected(partner)
                }
            }
        },
    ) { position ->
        data?.let { c ->
            if (c.moveToPosition(position)) {
                val partner = Partnerstamm(c)
                Text(partner.name, fontSize = 14.sp)
            }
        }
    }
}
