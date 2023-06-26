package com.gerwalex.mymonma.database.views

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.room.DatabaseView
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.database.room.DB
import com.gerwalex.mymonma.database.tables.Cat
import com.gerwalex.mymonma.ui.content.AutoCompleteTextView

@DatabaseView(
    """
        select a.*, b.name as obercatname
        from Cat a
        join Cat b on a.obercatid = b.id
"""
)
data class CatView(
    var id: Long? = null,
    var name: String = "",
    var description: String? = null,
    var obercatid: Long = 0,
    var supercatid: Long = 0,
    var catclassid: Long = 0,
    var incomecat: Boolean? = null,
    var ausgeblendet: Boolean = false,
    var saldo: Long = 0,
    var cnt: Long = 0,
    var obercatname: String? = null,

    )

/**
 * Wenn keien Kategorie ausgewählt wurde ist die cat.id == null,
 */
@Composable
fun AutoCompleteCatView(filter: String, modifier: Modifier = Modifier, selected: (Cat) -> Unit) {
    var catname by remember { mutableStateOf(filter) }
    var isError by remember { mutableStateOf(false) }
    var showDropdown by remember { mutableStateOf(true) }
    val data by DB.dao.getCatlist(catname).collectAsState(initial = emptyList()).apply {
        when (value.size) {
            0 -> {
                isError = true
                selected(Cat())
            }

            1 -> {
                isError = false
                selected(value[0])
                showDropdown = false
            }

            else -> {
                isError = false
                if (catname.isEmpty())
                    selected(Cat()) else selected(value[0])
            }
        }

    }
    AutoCompleteTextView(
        query = catname, list = data,

        queryLabel = stringResource(id = R.string.categorie),
        onQueryChanged = {
            catname = it
        },
        showDropdown = showDropdown,
        onClearClick = { catname = "" },
        onDismissRequest = { showDropdown = false },
        onItemClick = { cat ->
            catname = cat.name
            showDropdown = false
            selected(cat)
        },
        onFocusChanged = { isFocused ->
            Log.d("AutoCompleteCatView", "Focus=$isFocused")
            if (!isFocused) {
                if (data.isNotEmpty() && catname.isNotEmpty()) {
                    catname = data[0].name
                    selected(data[0])
                } else {
                    isError = true
                    selected(Cat())
                }
            }
        }
    ) {
        Text(text = if (it.catclassid == Cat.KONTOCLASS) "[${it.name}]" else it.name)

    }
}

/**
 * Splittet einen Catnamen auf
 * Wenn catname:
 * -supercat: 1. Zeile supercatname
 * -obercat: 1. Zeile supercatname als Label, 2. Zeile obercatname
 * - normale cat: 1. Zeile supercatname + Zeile obercatname als Label, 2. Zeile name
 */
@Composable
fun SplittedCatNameItem(name: String) {
    val splitted = name.split(":")
    Column {
        when (splitted.size) {
            1 -> {
                Text(splitted[0])
            }

            2 -> {
                Text(splitted[0], style = MaterialTheme.typography.labelMedium)
                Text(splitted[1])

            }

            else -> {
                Text(
                    "${splitted[0]}/${splitted[1]}",
                    style = MaterialTheme.typography.labelMedium
                )
                Text(splitted[2])

            }
        }
    }


}