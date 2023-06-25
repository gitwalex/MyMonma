package com.gerwalex.mymonma.ui.report

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gerwalex.mymonma.database.data.ExcludedCatClassesCheckBoxes
import com.gerwalex.mymonma.database.data.ExcludedCatsCheckBoxes
import com.gerwalex.mymonma.database.data.GeldflussData
import com.gerwalex.mymonma.database.data.GeldflussDataItem
import com.gerwalex.mymonma.database.data.GeldflussSummen
import com.gerwalex.mymonma.database.room.DB.reportdao
import com.gerwalex.mymonma.database.tables.ReportBasisDaten
import com.gerwalex.mymonma.ext.rememberState
import com.gerwalex.mymonma.main.MonMaViewModel
import com.gerwalex.mymonma.ui.navigation.Destination
import com.gerwalex.mymonma.ui.navigation.TopToolBar
import com.gerwalex.mymonma.ui.navigation.Up
import kotlinx.coroutines.launch


@Composable
fun GeldflussScreen(viewModel: MonMaViewModel, navigateTo: (Destination) -> Unit) {
    viewModel.reportId?.let { reportid ->
        val drawerState = rememberDrawerState(DrawerValue.Closed)
        var drawerContent by rememberState { ExcludedValuesSheet.Classes }
        val scope = rememberCoroutineScope()
        val report by reportdao.getReportBasisDaten(reportid)
            .collectAsState(initial = ReportBasisDaten())
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {

                ModalDrawerSheet {
                    Spacer(Modifier.height(12.dp))
                    when (drawerContent) {
                        ExcludedValuesSheet.Classes -> ExcludedCatClassesCheckBoxes(reportid = reportid)
                        ExcludedValuesSheet.Cats -> ExcludedCatsCheckBoxes(reportid = reportid)
                    }
                }
            }, content = {
                Scaffold(
                    topBar = {
                        TopToolBar(title = report.name) {
                            navigateTo(Up)
                        }
                    },
                    bottomBar = {
                        BottomNavigationBar {
                            drawerContent = it
                            scope.launch {
                                if (drawerState.isOpen) {
                                    drawerState.close()
                                }
                                drawerState.open()
                            }
                        }
                    }

                ) { padding ->
                    Box(modifier = Modifier.padding(padding)) {
                        val list by reportdao.getReportGeldflussData(reportid)
                            .collectAsState(initial = emptyList())
                        if (list.isNotEmpty()) {
                            GeldflussDetailScreen(report = report, list = list) {

                            }
                        }
                    }
                }
            })
    } ?: navigateTo(Up)
}

@Composable
fun GeldflussDetailScreen(
    report: ReportBasisDaten,
    list: List<GeldflussData>,
    onSelected: (GeldflussData) -> Unit

) {
    Column {
        LazyColumn(
            modifier = Modifier.weight(1f),
        ) {
            items(list, key = { it.catid }) { item ->
                Card(modifier = Modifier.padding(2.dp)) {
                    GeldflussDataItem(trx = item) {
                        onSelected(item)
                    }
                }
            }
        }
        Card(
            modifier = Modifier.padding(4.dp),
            shape = MaterialTheme.shapes.large
        ) {
            GeldflussSummen(report = report)

        }
    }

}

