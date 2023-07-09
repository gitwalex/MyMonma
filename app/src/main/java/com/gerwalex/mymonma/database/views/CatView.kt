package com.gerwalex.mymonma.database.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.room.DatabaseView
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.database.room.DB.Companion.dao
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
    var cnt: Long? = 0,
    var obercatname: String? = null,

    )

/**
 * Wenn keien Kategorie ausgewählt wurde ist die cat.id == null,
 */
@Composable
fun AutoCompleteCatView(
    filter: String,
    modifier: Modifier = Modifier,
    selected: (CatView) -> Unit
) {
    var catname by remember { mutableStateOf(filter) }
    var showDropdown by remember { mutableStateOf(false) }
    val data by dao.getCatlist(catname).collectAsStateWithLifecycle( emptyList())
    AutoCompleteTextView(
        modifier = Modifier.fillMaxWidth(),
        query = catname,
        queryLabel = stringResource(id = R.string.partnername),
        onQueryChanged = {
            catname = it
            if (catname.isEmpty()) {
                selected(CatView(id = 0, name = ""))
            } else {
                if (data.isNotEmpty()) {
                    selected(data[0])
                }
            }
        },
        list = data,
        onDismissRequest = { },
        onItemClick = { cat ->
            catname = cat.name
            selected(cat)
            showDropdown = false
        },
        onFocusChanged = {
            showDropdown = it
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