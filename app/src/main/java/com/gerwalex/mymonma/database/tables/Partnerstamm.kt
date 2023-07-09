package com.gerwalex.mymonma.database.tables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.database.room.DB.Companion.dao
import com.gerwalex.mymonma.ui.content.AutoCompleteTextView

@Entity
data class Partnerstamm(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null,
    var name: String = "Unknown"
) {
    suspend fun update() {
        dao.update(this)

    }

    companion object {
        const val Undefined = 0L
    }
}


@Composable
fun AutoCompletePartnerView(filter: String, selected: (Partnerstamm) -> Unit) {
    var partnername by remember { mutableStateOf(filter) }
    var showDropdown by remember { mutableStateOf(false) }
    val data by dao.getPartnerlist(partnername).collectAsStateWithLifecycle( emptyList())
    AutoCompleteTextView(
        modifier = Modifier.fillMaxWidth(),
        query = partnername,
        queryLabel = stringResource(id = R.string.partnername),
        onQueryChanged = {
            partnername = it
            if (partnername.isEmpty()) {
                selected(Partnerstamm(id = 0, name = ""))
            } else {
                if (data.isNotEmpty()) {
                    if (data[0].name == partnername) {
                        selected(data[0])
                    } else {
                        selected(Partnerstamm(id = 0, name = partnername))
                    }
                }
            }
        },
        list = data,
        onDismissRequest = { },
        onItemClick = { partner ->
            partnername = partner.name
            selected(partner)
            showDropdown = false
        },
        onFocusChanged = {
            showDropdown = it
        }
    ) { partner ->
        Text(partner.name, fontSize = 14.sp)
    }
}

