package com.gerwalex.mymonma.database.data

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.DatabaseView
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.database.room.DB
import com.gerwalex.mymonma.ext.rememberState
import com.gerwalex.mymonma.ui.AppTheme
import kotlinx.coroutines.launch

@DatabaseView(
    """
            select a.id, a.name, r.id as reportid, a.id as catclassid,  
        (select id from ReportExcludedCatClasses b   
        where b.reportid = r.id and b.catclassid = a.id) as excluded   
        from CatClass a   
		join ReportBasisDaten r
        where a.id > 100   
        order by a.name

"""
)
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
    var excluded by rememberState(key = item.excluded) { item.excluded }
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable {
            excluded = !excluded
        }) {
        Checkbox(
            checked = !excluded,
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

@Preview(name = "Light", uiMode = UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun CatClassChaeckBoxPreview() {
    val item = ExcludedCatClasses(
        id = 1,
        name = "Excluded",
        reportid = 1L,
        catclassid = 3L,
        excluded = false

    )
    AppTheme {
        Surface {
            Column {
                CatClassCheckbox(item = item)
                CatClassCheckbox(item = item.copy(excluded = true, name = "Not Excluded"))
            }
        }
    }
}