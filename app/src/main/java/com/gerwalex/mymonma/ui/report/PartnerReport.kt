package com.gerwalex.mymonma.ui.report

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gerwalex.mymonma.database.data.PartnerdatenItem
import com.gerwalex.mymonma.database.data.PartnerdatenReport
import com.gerwalex.mymonma.database.room.DB.reportdao
import com.gerwalex.mymonma.database.tables.ReportBasisDaten
import com.gerwalex.mymonma.ui.navigation.Destination


@Composable
fun PartnerdatenReportScreen(
    report: ReportBasisDaten,
    navigateTo: (Destination) -> Unit
) {
    report.id?.let { reportid ->
        val list by reportdao.getPartnerdatenReport(reportid).collectAsState(initial = emptyList())
        if (list.isNotEmpty()) {
            PartnerdatenReportScreen(list = list, onSelected = {})
        }

    }
}

@Composable
fun PartnerdatenReportScreen(
    list: List<PartnerdatenReport>,
    onSelected: (PartnerdatenReport) -> Unit

) {
    Column {
        LazyColumn {
            items(list, key = { it.partnerid }) { item ->
                Card(modifier = Modifier.padding(4.dp)) {
                    PartnerdatenItem(
                        data = item, onClicked = {
                            onSelected(item)
                        })

                }
            }
        }
    }

}

