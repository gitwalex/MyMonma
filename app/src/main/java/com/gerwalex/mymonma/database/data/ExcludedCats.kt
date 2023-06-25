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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.database.room.DB.reportdao
import com.gerwalex.mymonma.ui.content.NoEntriesBox
import kotlinx.coroutines.launch

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


        val list by reportdao.getExcludedCats(reportid).collectAsState(initial = emptyList())
        if (list.isNotEmpty()) {
            LazyColumn(content = {
                items(list) { item ->
                    CatCheckbox(item = item)
                }
            })
        } else {
            NoEntriesBox()
        }
    }
}

@Composable
fun CatCheckbox(item: ExcludedCats) {
    val scope = rememberCoroutineScope()
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable {
            scope.launch {
                reportdao.deleteExcludedCat(item.reportid, item.catid)
            }
        }) {
        Checkbox(
            checked = true,
            onCheckedChange = { selected ->
            }
        )
        Text(text = item.name)
    }
}
