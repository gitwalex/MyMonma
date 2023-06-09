package com.gerwalex.mymonma.database.data

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.room.DatabaseView
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.database.room.DB.Companion.reportdao
import com.gerwalex.mymonma.database.views.SplittedCatNameItem
import com.gerwalex.mymonma.ui.content.NoEntriesBox
import kotlinx.coroutines.launch

@DatabaseView(
    """
            select a.reportid, a.catid, 
                (select name from Cat b 
                where b.id = a.catid) as name 
                from ReportExcludedCats a 
                order by name

"""
)
data class ExcludedCats(
    val name: String,
    val reportid: Long,
    val catid: Long,
)

@Composable
fun ExcludedCatsCheckBoxes(reportid: Long) {
    Column {
        Text(
            text = stringResource(id = R.string.excludedCats),
            style = MaterialTheme.typography.labelMedium
        )


        val list by reportdao.getExcludedCats(reportid).collectAsStateWithLifecycle( emptyList())
        if (list.isNotEmpty()) {
            val scope = rememberCoroutineScope()
            LazyColumn(content = {
                items(list) { item ->
                    CatCheckbox(item = item) {
                        scope.launch {
                            reportdao.deleteExcludedCat(item.reportid, item.catid)
                        }


                    }
                }
            })
        } else {
            NoEntriesBox()
        }
    }
}

@Composable
fun CatCheckbox(item: ExcludedCats, onCheckedChanged: (isChecked: Boolean) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable {
            onCheckedChanged(false)
        }) {
        Checkbox(
            checked = true,
            onCheckedChange = { selected ->
                onCheckedChanged(selected)
            }
        )
        SplittedCatNameItem(name = item.name)
    }
}
