package com.gerwalex.mymonma.database.tables

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.database.room.DB.dao
import com.gerwalex.mymonma.database.tables.Cat.Companion.KONTOCLASS
import com.gerwalex.mymonma.ui.content.AutoCompleteTextView

@Entity(
    foreignKeys = [ForeignKey(
        entity = CatClass::class,
        parentColumns = ["id"],
        childColumns = ["catclassid"],
        onDelete = ForeignKey.RESTRICT,
        onUpdate = ForeignKey.CASCADE,
    ), ForeignKey(
        entity = Cat::class,
        parentColumns = ["id"],
        childColumns = ["obercatid"],
        onDelete = ForeignKey.RESTRICT,
        onUpdate = ForeignKey.CASCADE,
    ), ForeignKey(
        entity = Cat::class,
        parentColumns = ["id"],
        childColumns = ["supercatid"],
        onDelete = ForeignKey.RESTRICT,
        onUpdate = ForeignKey.CASCADE,

        )]
)
data class Cat(

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var name: String = "",
    var description: String? = null,

    @ColumnInfo(index = true)
    var obercatid: Long = 0,

    @ColumnInfo(index = true)
    var supercatid: Long = 0,

    @ColumnInfo(index = true)
    var catclassid: Long = 0,
    var incomecat: Boolean? = null,
    var ausgeblendet: Boolean = false,

    var saldo: Long = 0,

    /**
     * Antahl der CashTrx zu dieser catid
     */
    var cnt: Long = 0,
) {

    companion object {

        const val AbgeltSteuerCatid: Long = 10040L
        const val CASHKONTOCATID = 1001L
        const val CATID = "CATID"
        const val DEPOTCATID = 1002L
        const val EROEFFNUNGCAT = 9000L
        const val MAXACCOUNTNUMBER: Long = 899
        const val MINACCOUNTNUMBER: Long = 1
        const val NOCATID: Long = 0
        const val SPLITBUCHUNGCATID: Long = 1000L
        const val KONTOCLASS: Long = 2
    }
}

/**
 * Wenn keien Kategorie ausgewählt wurde ist die cat.id == null,
 */
@Composable
fun AutoCompleteCatView(filter: String, modifier: Modifier = Modifier, selected: (Cat) -> Unit) {
    var catname by remember { mutableStateOf(filter) }
    var isError by remember { mutableStateOf(false) }
    var showDropdown by remember { mutableStateOf(true) }
    val data by dao.getCatlist(catname).collectAsState(initial = emptyList()).apply {
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
            showDropdown = true
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
            showDropdown = isFocused
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
        Text(text = if (it.catclassid == KONTOCLASS) "[${it.name}]" else it.name)

    }
}
