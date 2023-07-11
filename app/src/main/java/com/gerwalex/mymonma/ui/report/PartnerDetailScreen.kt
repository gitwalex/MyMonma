package com.gerwalex.mymonma.ui.report

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gerwalex.mymonma.database.data.PartnerdatenItem
import com.gerwalex.mymonma.database.data.PartnerdatenReport
import com.gerwalex.mymonma.database.room.DB.Companion.reportdao
import com.gerwalex.mymonma.database.tables.ReportBasisDaten
import com.gerwalex.mymonma.enums.ReportDateSelector
import com.gerwalex.mymonma.ext.rememberState
import com.gerwalex.mymonma.ui.navigation.Destination
import com.gerwalex.mymonma.ui.navigation.PartnerGeldflussDetailsDest


@Composable
fun PartnerDetailScreen(
    reportid: Long,
    navigateTo: (Destination) -> Unit
) {
    var report by rememberState { ReportBasisDaten() }
    val list by reportdao.getPartnerdatenReport(reportid).collectAsStateWithLifecycle(emptyList())
    LaunchedEffect(reportid) {
        reportdao.getReportBasisDaten(reportid)?.let {
            if (it.zeitraum != ReportDateSelector.EigDatum) {
                it.von = it.zeitraum.dateSelection.startDate
                it.bis = it.zeitraum.dateSelection.endDate
            }
            reportdao.update(report)
            report = it
        }
    }

    if (list.isNotEmpty()) {
        PartnerdatenReportScreen(list = list, onSelected = { data ->
            PartnerGeldflussDetailsDest.also {
                it.reportid = reportid
                it.partnerid = data.partnerid
                navigateTo(it)
            }
        })
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

