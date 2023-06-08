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
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.database.ObservableTableRowNew
import com.gerwalex.mymonma.database.room.DB
import com.gerwalex.mymonma.ui.content.AutoCompleteTextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Entity
data class CatClass(

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var name: String = "Unknown",
    var description: String? = "",
    var intern: Boolean = false,
) : ObservableTableRowNew() {

    @Ignore
    constructor(c: Cursor) : this() {
        fillContent(c)
        id = getAsLong("id")
        name = getAsString("name")!!
        description = getAsString("description")
        intern = getAsBoolean("intern")
    }
}

@Composable
fun AutoCompleteCatClassView(filter: String, selected: (CatClass) -> Unit) {
    var catclassname by remember { mutableStateOf(filter) }
    val cursor = MutableLiveData<Cursor>()
    val data by cursor.observeAsState()
    var count by remember { mutableStateOf(0) }
    LaunchedEffect(catclassname) {
        withContext(Dispatchers.IO) {
            val c = DB.dao.getCatClasslist(catclassname)
            if (c.moveToFirst()) {
                selected(CatClass(c))
            }
            count = c.count
            cursor.postValue(c)
        }
    }


    AutoCompleteTextView(
        modifier = Modifier.fillMaxWidth(),
        query = catclassname,
        queryLabel = stringResource(id = R.string.categorie),
        onQueryChanged = { catclassname = it },
        count = count,
        onClearClick = { catclassname = "" },
        onDoneActionClick = { },
        onItemClick = { position ->
            data?.let { c ->
                if (c.moveToPosition(position)) {
                    val catClass = CatClass(c)
                    catclassname = catClass.name
                    count = 0
                    selected(catClass)
                }
            }
        },
    ) { position ->
        data?.let { c ->
            if (c.moveToPosition(position)) {
                val catClass = CatClass(c)
                Text(catClass.name, fontSize = 14.sp)
            }
        }
    }
}
