package com.gerwalex.mymonma.ui.report

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gerwalex.mymonma.database.data.ExcludedCatClassesCheckBoxes
import com.gerwalex.mymonma.database.data.ExcludedCatsCheckBoxes
import com.gerwalex.mymonma.database.room.DB.Companion.reportdao
import com.gerwalex.mymonma.database.tables.ReportBasisDaten
import com.gerwalex.mymonma.enums.ReportDateSelector
import com.gerwalex.mymonma.enums.ReportTyp
import com.gerwalex.mymonma.ext.rememberState
import com.gerwalex.mymonma.main.MonMaViewModel
import com.gerwalex.mymonma.ui.navigation.Destination
import com.gerwalex.mymonma.ui.navigation.ReportGeldflussDetail
import com.gerwalex.mymonma.ui.navigation.ReportGeldflussVerglDetail
import com.gerwalex.mymonma.ui.navigation.TopToolBar
import com.gerwalex.mymonma.ui.navigation.Up
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportDetailScreen(
    reportid: Long,
    viewModel: MonMaViewModel,
    navigateTo: (Destination) -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    var drawerContent by rememberState { ExcludedValuesSheet.Classes }
    val scope = rememberCoroutineScope()
    val sheetState = rememberBottomSheetScaffoldState()
    var report by rememberState { ReportBasisDaten() }
    LaunchedEffect(reportid) {
        reportdao.getReportBasisDaten(reportid)?.let {
            if (it.zeitraum != ReportDateSelector.EigDatum) {
                it.von = it.zeitraum.dateSelection.startDate
                it.bis = it.zeitraum.dateSelection.endDate
            }
            if (it.verglZeitraum != ReportDateSelector.EigDatum) {
                it.verglVon = it.verglZeitraum.dateSelection.startDate
                it.verglBis = it.verglZeitraum.dateSelection.endDate
            }
            reportdao.update(report)
            report = it
        }
    }
    report.id?.let {
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
                BottomSheetScaffold(
                    sheetContent = {
                        ZeitraumCard(report = report)
                        VerglZeitraumCard(report = report)
                        BottomNavigationBar {
                            drawerContent = it
                            scope.launch {
                                if (drawerState.isOpen) {
                                    drawerState.close()
                                }
                                drawerState.open()
                            }
                        }
                    },
                    scaffoldState = sheetState,
                    topBar = {
                        TopToolBar(title = report.name,
                            actions = {}) {
                            navigateTo(Up)
                        }
                    },

                    ) { padding ->
                    Box(modifier = Modifier.padding(padding)) {
                        when (report.typ) {
                            ReportTyp.GeldflussVergl ->
                                GeldflussDetailScreen(report = report, onSelected = { data ->
                                    ReportGeldflussDetail.also {
                                        it.reportid = report.id!!
                                        it.catid = data.catid
                                        navigateTo(it)
                                    }
                                },
                                    onVerglSelected = { data ->
                                        ReportGeldflussVerglDetail.also {
                                            it.reportid = report.id!!
                                            it.catid = data.catid
                                            navigateTo(it)
                                        }

                                    })

                            ReportTyp.Empfaenger -> PartnerdatenReportScreen(
                                report = report,
                                navigateTo = navigateTo
                            )
                        }
                    }
                }
            })

    }
}





