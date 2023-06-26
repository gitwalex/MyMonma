package com.gerwalex.mymonma.ui.report

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Card
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberDismissState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
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
import com.gerwalex.mymonma.ui.content.NoEntriesBox
import com.gerwalex.mymonma.ui.navigation.Destination
import com.gerwalex.mymonma.ui.navigation.TopToolBar
import com.gerwalex.mymonma.ui.navigation.Up
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeldflussScreen(reportid: Long, viewModel: MonMaViewModel, navigateTo: (Destination) -> Unit) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    var drawerContent by rememberState { ExcludedValuesSheet.Classes }
    val scope = rememberCoroutineScope()
    val sheetState = rememberBottomSheetScaffoldState()
    val report by reportdao.getReportBasisDaten(reportid)
        .collectAsState(initial = ReportBasisDaten())
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
                        val list by reportdao.getReportGeldflussData(reportid)
                            .collectAsState(initial = emptyList())
                        if (list.isNotEmpty()) {
                            GeldflussDetailScreen(report = report, list = list) {
                            }
                        } else {
                            NoEntriesBox()

                        }
                    }
                }
            })
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun GeldflussDetailScreen(
    report: ReportBasisDaten,
    list: List<GeldflussData>,
    onSelected: (GeldflussData) -> Unit

) {
    Column {
        Card(
            modifier = Modifier.padding(4.dp),
            shape = MaterialTheme.shapes.large
        ) {
            GeldflussSummen(report = report)
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
        ) {
            items(list, key = { it.catid }) { item ->
                val scope = rememberCoroutineScope()
                val currentItem by rememberUpdatedState(newValue = item)
                val dismissState = rememberDismissState(
                    confirmValueChange = { dismissValue ->
                        when (dismissValue) {
                            DismissValue.Default -> false
                            DismissValue.DismissedToEnd -> false
                            DismissValue.DismissedToStart -> {
                                scope.launch {
                                    reportdao.insertExcludedCat(
                                        currentItem.reportid,
                                        currentItem.catid
                                    )
                                }
                                true
                            }
                        }
                    }
                )
                SwipeToDismiss(
                    modifier = Modifier
                        .padding(vertical = 1.dp)
                        .animateItemPlacement(),
                    state = dismissState,
                    background = {
                        SwipeGeldflussBackground(dismissState = dismissState)
                    },
                    dismissContent = {
                        Card(modifier = Modifier.padding(2.dp)) {
                            GeldflussDataItem(trx = item) {
                                onSelected(item)
                            }
                        }
                    })

            }
        }
    }

}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SwipeGeldflussBackground(dismissState: DismissState) {
    val direction = dismissState.dismissDirection ?: return

    val color by animateColorAsState(
        when (dismissState.targetValue) {
            DismissValue.Default -> Color.LightGray
            DismissValue.DismissedToEnd -> Color.Green
            DismissValue.DismissedToStart -> Color.Red
        }
    )
    val alignment = when (direction) {
        DismissDirection.StartToEnd -> Alignment.CenterStart
        DismissDirection.EndToStart -> Alignment.CenterEnd
    }
    val icon = when (direction) {
        DismissDirection.StartToEnd -> Icons.Default.Done
        DismissDirection.EndToStart -> Icons.Default.Remove
    }
    val scale by animateFloatAsState(
        if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
    )

    Box(
        Modifier
            .fillMaxSize()
            .background(color)
            .padding(horizontal = 20.dp),
        contentAlignment = alignment
    ) {
        Icon(
            icon,
            contentDescription = "Localized description",
            modifier = Modifier.scale(scale)
        )
    }
}



