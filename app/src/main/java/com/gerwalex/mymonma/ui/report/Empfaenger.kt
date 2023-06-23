package com.gerwalex.mymonma.ui.report

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gerwalex.mymonma.database.data.GeldflussData
import com.gerwalex.mymonma.database.data.GeldflussDataItem
import com.gerwalex.mymonma.database.room.DB.reportdao
import com.gerwalex.mymonma.database.tables.ReportBasisDaten
import com.gerwalex.mymonma.main.MonMaViewModel
import com.gerwalex.mymonma.ui.navigation.Destination
import com.gerwalex.mymonma.ui.navigation.TopToolBar
import com.gerwalex.mymonma.ui.navigation.Up
import com.gerwalex.mymonma.ui.subscreens.GeldflussDrawer


@Composable
fun EmpfaengerScreen(viewModel: MonMaViewModel, navigateTo: (Destination) -> Unit) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val reportid = rememberSaveable { viewModel.reportId }
    var report by remember { mutableStateOf(ReportBasisDaten()) }
    report.let {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {

                ModalDrawerSheet {
                    Spacer(Modifier.height(12.dp))
                    GeldflussDrawer(reportid = it.id!!)
                }
            }, content = {

                Scaffold(
                    topBar = {
                        TopToolBar(title = it.name) {
                            navigateTo(Up)
                        }
                    },

                    ) { padding ->
                    Box(modifier = Modifier.padding(padding))
                    val list by reportdao.getReportGeldflussData(
                        it.id!!,
                        from = it.von,
                        to = it.bis
                    )
                        .collectAsState(initial = emptyList())
                    if (list.isNotEmpty()) {
                        EmpaengerDetailScreen(reportBasisDaten = it, list = list) {

                        }
                    }
                }
                LaunchedEffect(key1 = reportid) {
                    reportdao.getReportBasisDaten(reportid)?.apply {
                        report = this
                    }

                }
            })
    }
}

@Composable
fun EmpaengerDetailScreen(
    reportBasisDaten: ReportBasisDaten,
    list: List<GeldflussData>,
    onSelected: (GeldflussData) -> Unit

) {
    Column {
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(list, key = { it.catid }) { item ->
                GeldflussDataItem(trx = item, onClicked = {
                    onSelected(item)
                })
            }
        }
    }

}

