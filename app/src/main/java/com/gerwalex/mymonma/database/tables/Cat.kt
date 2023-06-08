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
import com.gerwalex.mymonma.ui.content.AutoCompleteTextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Entity(
    foreignKeys = [ForeignKey(
        entity = CatClass::class,
        parentColumns = ["id"],
        childColumns = ["catclassid"],
        onDelete = ForeignKey.RESTRICT,
        onUpdate = ForeignKey.CASCADE,
        deferred = true
    ), ForeignKey(
        entity = Cat::class,
        parentColumns = ["id"],
        childColumns = ["obercatid"],
        onDelete = ForeignKey.RESTRICT,
        onUpdate = ForeignKey.CASCADE,
        deferred = true
    ), ForeignKey(
        entity = Cat::class,
        parentColumns = ["id"],
        childColumns = ["supercatid"],
        onDelete = ForeignKey.RESTRICT,
        onUpdate = ForeignKey.CASCADE,
        deferred = true
    )]
)
data class Cat(

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var name: String = "Neu",
    var description: String? = null,

    @ColumnInfo(index = true)
    var obercatid: Long = 0,

    @ColumnInfo(index = true)
    var supercatid: Long = 0,

    @ColumnInfo(index = true)
    var catclassid: Long = 0,
    var incomecat: Boolean? = null,
    var ausgeblendet: Boolean = false,

    /**
     * Antahl der CashTrx zu dieser catid
     */
    var cnt: Long = 0,
) : ObservableTableRowNew() {

    @Ignore
    constructor(c: Cursor) : this(null) {
        fillContent(c)
        id = getAsLong("id")
        name = getAsString("name")!!
        description = getAsString("description")
        obercatid = getAsLong("obercatid")
        supercatid = getAsLong("supercatid")
        catclassid = getAsLong("catclassid")
        incomecat = getAsBooleanOrNull("incomecat")
        ausgeblendet = getAsBoolean("ausgeblendet")
        cnt = getAsLong("cnt")
    }

    companion object {

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

@Composable
fun AutoCompleteCatView(filter: String, selected: (Cat) -> Unit) {
    var catname by remember { mutableStateOf(filter) }
    val cursor = MutableLiveData<Cursor>()
    val data by cursor.observeAsState()
    LaunchedEffect(catname) {
        withContext(Dispatchers.IO) {
            val c = DB.dao.getCatlist(catname)
            if (c.moveToFirst()) {
                selected(Cat(c))
            }
            cursor.postValue(c)
        }
    }


    AutoCompleteTextView(
        modifier = Modifier.fillMaxWidth(),
        query = catname,
        queryLabel = stringResource(id = R.string.categorie),
        onQueryChanged = { catname = it },
        count = data?.count ?: 0,
        onClearClick = { catname = "" },
        onDoneActionClick = { },
        onItemClick = { position ->
            data?.let { c ->
                if (c.moveToPosition(position)) {
                    val cat = Cat(c)
                    catname = cat.name
                    selected(cat)
                }
            }
        },
    ) { position ->
        data?.let { c ->
            if (c.moveToPosition(position)) {
                val cat = Cat(c)
                Text(cat.name, fontSize = 14.sp)
            }
        }
    }
}
