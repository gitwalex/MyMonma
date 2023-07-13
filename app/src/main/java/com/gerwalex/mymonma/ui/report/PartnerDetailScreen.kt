package com.gerwalex.mymonma.ui.report

import android.util.Log
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartnerDetailScreen(
    reportid: Long,
    navigateTo: (Destination) -> Unit
) {
    val scope = rememberCoroutineScope()
    var report by rememberState { ReportBasisDaten(id = reportid) }
    var zeitraum by rememberState { ReportDateSelector.Ltz12Mnt }
    var list by rememberState { listOf<PartnerdatenReport>() }
    var filter by rememberState { "" }
    LaunchedEffect(reportid) {
        reportdao.getReportBasisDaten(reportid)?.let {
            it.von = it.zeitraum.dateSelection.startDate
            it.bis = it.zeitraum.dateSelection.endDate
            zeitraum = it.zeitraum
            reportdao.update(it)
            report = it
            list = reportdao.getPartnerdaten(reportid, filter)
        }
    }

    BottomSheetScaffold(
        sheetContent = {
            ZeitraumCard(zeitraum, onChanged = { z ->
                zeitraum = z
                scope.launch {
                    report.zeitraum = zeitraum
                    report.von = z.dateSelection.startDate
                    report.bis = z.dateSelection.endDate
                    reportdao.update(report)
                    list = reportdao.getPartnerdaten(reportid, filter)
                    Log.d("PartnerDetailScreen", "selected: $z")
                    Log.d("PartnerDetailScreen", "PartnerdatenReportScreen: listsize=${list.size}")

                }
            })
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                QuerySearch(query = filter, label = "Filter", onQueryChanged = {
                    filter = it
                    scope.launch {
                        list = reportdao.getPartnerdaten(reportid, filter)
                        Log.d(
                            "PartnerDetailScreen",
                            "PartnerdatenReportScreen: listsize=${list.size}"
                        )
                    }
                })
            }

        },
        topBar = {
            TopToolBar(title = report.name) {
                navigateTo(Up)
            }
        },
    ) {


        PartnerdatenReportScreen(list = list, selected = { data ->
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
    selected: (PartnerdatenReport) -> Unit
) {

    LaunchedEffect(list) {
        Log.d("PartnerDetailScreen", "PartnerdatenReportScreen: listsize=${list.size}")
    }
    Column {
        LazyColumn {
            items(list, key = { it.partnerid }) { item ->
                Card(modifier = Modifier.padding(4.dp)) {
                    PartnerdatenItem(
                        data = item, onClicked = {
                            selected(item)
                        })

                }
            }
        }
    }
}

