package com.gerwalex.mymonma.ui.report

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gerwalex.mymonma.database.data.PartnerdatenItem
import com.gerwalex.mymonma.database.data.PartnerdatenReport
import com.gerwalex.mymonma.database.room.DB.Companion.reportdao
import com.gerwalex.mymonma.database.tables.ReportBasisDaten
import com.gerwalex.mymonma.enums.ReportDateSelector
import com.gerwalex.mymonma.ext.rememberState
import com.gerwalex.mymonma.ui.content.QuerySearch
import com.gerwalex.mymonma.ui.navigation.Destination
import com.gerwalex.mymonma.ui.navigation.PartnerGeldflussDetailsDest
import com.gerwalex.mymonma.ui.navigation.TopToolBar
import com.gerwalex.mymonma.ui.navigation.Up


@Composable
fun PartnerDetailScreen(
    reportid: Long,
    navigateTo: (Destination) -> Unit
) {
    val report by reportdao.getReportBasisDatenAsFlow(reportid).collectAsStateWithLifecycle(
        ReportBasisDaten()
    )
    val list by reportdao.getPartnerdatenReport(reportid).collectAsStateWithLifecycle(emptyList())
    LaunchedEffect(reportid) {
        reportdao.getReportBasisDaten(reportid)?.let {
            if (it.zeitraum != ReportDateSelector.EigDatum) {
                it.von = it.zeitraum.dateSelection.startDate
                it.bis = it.zeitraum.dateSelection.endDate
            }
            reportdao.update(it)
        }
    }

    if (list.isNotEmpty()) {
        PartnerdatenReportScreen(report = report, list = list, navigateTo)
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartnerdatenReportScreen(
    report: ReportBasisDaten,
    list: List<PartnerdatenReport>,
    navigateTo: (Destination) -> Unit,
) {
    var filter by rememberState { "" }
    val filteredList by remember {
        derivedStateOf {
            list.filter {
                it.name.lowercase().contains(filter.lowercase())
            }
        }
    }

    BottomSheetScaffold(
        sheetContent = {
            ZeitraumCard(report = report)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                QuerySearch(query = filter, label = "Filter", onQueryChanged = {
                    filter = it
                })
            }

        },
        topBar = {
            TopToolBar(title = report.name) {
                navigateTo(Up)
            }
        },
    ) {


        Column {
            LazyColumn {
                items(filteredList, key = { it.partnerid }) { item ->
                    Card(modifier = Modifier.padding(4.dp)) {
                        PartnerdatenItem(
                            data = item, onClicked = {
                                PartnerGeldflussDetailsDest.also {
                                    it.reportid = report.id!!
                                    it.partnerid = item.partnerid
                                    navigateTo(it)
                                }
                            })

                    }
                }
            }
        }

    }
}

