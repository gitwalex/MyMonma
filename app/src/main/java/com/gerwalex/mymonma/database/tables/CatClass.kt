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
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.database.room.DB.dao
import com.gerwalex.mymonma.ui.content.AutoCompleteTextView

@Entity
data class CatClass(

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var name: String = "Unknown",
    var description: String? = "",
    var intern: Boolean = false,
)

@Composable
fun AutoCompleteCatClassView(filter: String, selected: (CatClass) -> Unit) {
    var catclassname by remember { mutableStateOf(filter) }
    val data by dao.getCatClasslist(catclassname).collectAsState(initial = emptyList())
    var count by remember { mutableStateOf(0) }


    AutoCompleteTextView(
        modifier = Modifier.fillMaxWidth(),
        query = catclassname,
        queryLabel = stringResource(id = R.string.categorie),
        onQueryChanged = { catclassname = it },
        count = count,
        onClearClick = { catclassname = "" },
        onDoneActionClick = { },
        onItemClick = { position ->
            val catClass = data[position]
            catclassname = catClass.name
            count = 0
            selected(catClass)
        },
    ) { position ->
        val catClass = data[position]
        Text(catClass.name, fontSize = 14.sp)
    }
}
