package com.gerwalex.mymonma.database.data

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
import androidx.compose.ui.res.stringResource
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.database.room.DB
import kotlinx.coroutines.launch

data class ExcludedCatClasses(
    val id: Long,
    val name: String,
    val reportid: Long,
    val catclassid: Long,
    val excluded: Boolean
)

@Composable
fun ExcludedCatClassesCheckBoxes(reportid: Long) {
    Column {
        Text(
            text = stringResource(id = R.string.excludedCatClasses),
            style = MaterialTheme.typography.labelMedium
        )

        val list by DB.reportdao.getExcludedCatClasses(reportid)
            .collectAsState(initial = emptyList())
        LazyColumn(content = {
            items(list) { item ->
                CatClassCheckbox(item = item)
            }
        })
    }
}

@Composable
fun CatClassCheckbox(item: ExcludedCatClasses) {
    val scope = rememberCoroutineScope()
    Row {
        Checkbox(
            checked = !item.excluded,
            onCheckedChange = { selected ->
                scope.launch {
                    if (selected)
                        DB.reportdao.deleteExcludedCatClass(item.reportid, item.catclassid)
                    else
                        DB.reportdao.insertExcludedCatClass(item.reportid, item.catclassid)
                }
            }
        )
        Text(text = item.name)
    }
}