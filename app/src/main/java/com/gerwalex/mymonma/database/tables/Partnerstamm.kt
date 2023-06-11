package com.gerwalex.mymonma.database.tables

import android.util.Log
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
import androidx.room.PrimaryKey
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.database.room.DB.dao
import com.gerwalex.mymonma.ui.content.AutoCompleteTextView

@Entity
data class Partnerstamm(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null,
    var name: String = "Unknown"
)


@Composable
fun AutoCompletePartnerView(filter: String, selected: (Partnerstamm) -> Unit) {
    var partnername by remember { mutableStateOf(filter) }
    var showDropdown by remember { mutableStateOf(false) }
    val data by dao.getPartnerlist(partnername).collectAsState(initial = emptyList()).apply {
        when (value.size) {
            0 -> {
                selected(Partnerstamm(name = partnername))
            }

            else -> {
                selected(value[0])
            }
        }

    }
    AutoCompleteTextView(
        modifier = Modifier.fillMaxWidth(),
        query = partnername,
        queryLabel = stringResource(id = R.string.partnername),
        onQueryChanged = {
            partnername = it
        },
        count = data.size,
        onClearClick = { partnername = "" },
        onDismissRequest = { },
        onItemClick = { position ->
            val selectedItem = data[position]
            partnername = selectedItem.name
            selected(selectedItem)
        },
        onFocusChanged = {
            showDropdown = it
        }
    ) { position ->
        if (position < data.size) {
            Log.d("Partnerstamm", "AutoCompletePartnerView: position=$position, size=${data.size}")
            val partner = data[position]
            Text(partner.name, fontSize = 14.sp)
        }
    }
}

