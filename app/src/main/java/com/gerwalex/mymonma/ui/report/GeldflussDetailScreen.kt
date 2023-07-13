package com.gerwalex.mymonma.ui.report

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.List
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
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberDismissState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.database.data.ExcludedCatClassesCheckBoxes
import com.gerwalex.mymonma.database.data.ExcludedCatsCheckBoxes
import com.gerwalex.mymonma.database.data.GeldflussDataItem
import com.gerwalex.mymonma.database.data.GeldflussSummen
import com.gerwalex.mymonma.database.room.DB
import com.gerwalex.mymonma.database.tables.ReportBasisDaten
import com.gerwalex.mymonma.ext.rememberState
import com.gerwalex.mymonma.ui.content.AmountView
import com.gerwalex.mymonma.ui.content.NoEntriesBox
import com.gerwalex.mymonma.ui.navigation.Destination
import com.gerwalex.mymonma.ui.navigation.ReportGeldflussDetailDest
import com.gerwalex.mymonma.ui.navigation.ReportGeldflussVerglDetailDest
import com.gerwalex.mymonma.ui.navigation.TopToolBar
import com.gerwalex.mymonma.ui.navigation.Up
import kotlinx.coroutines.launch

enum class ExcludedValuesSheet {
    Classes, Cats
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun GeldflussDetailScreen(
    reportid: Long,
    navigateTo: (Destination) -> Unit,

    ) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    var drawerContent by rememberState { ExcludedValuesSheet.Classes }
    val sheetState = rememberBottomSheetScaffoldState()
    var report by rememberState { ReportBasisDaten() }
    LaunchedEffect(reportid) {
        DB.reportdao.getReportBasisDaten(reportid)?.let {
            it.von = it.zeitraum.dateSelection.startDate
            it.bis = it.zeitraum.dateSelection.endDate
            it.verglVon = it.verglZeitraum.dateSelection.startDate
            it.verglBis = it.verglZeitraum.dateSelection.endDate
            DB.reportdao.update(report)
            report = it
        }
    }

    val list by DB.reportdao.getReportGeldflussData(reportid)
        .collectAsStateWithLifecycle(emptyList())
    if (list.isEmpty()) {
        NoEntriesBox()
    } else {
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
                    topBar = {
                        TopToolBar(title = report.name) {
                            navigateTo(Up)
                        }
                    },
                    sheetContent = {
                        BottomSheetScaffoldContent(report = report, open = {
                            drawerContent = it
                            scope.launch {
                                drawerState.open()
                            }
                        })
                    },
                    scaffoldState = sheetState,
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
                            list.groupBy { item -> item.name.split(":")[0] }
                                .forEach { (catname, catlist) ->

                                    item {
                                        var saldo = 0L
                                        catlist.forEach {
                                            saldo += it.amount
                                        }
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(text = catname, fontWeight = FontWeight.Bold)
                                            AmountView(
                                                value = saldo,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                    items(catlist, key = { it.catid }) { item ->
                                        val currentItem by rememberUpdatedState(newValue = item)
                                        val dismissState = rememberDismissState(
                                            confirmValueChange = { dismissValue ->
                                                when (dismissValue) {
                                                    DismissValue.Default -> false
                                                    DismissValue.DismissedToEnd -> false
                                                    DismissValue.DismissedToStart -> {
                                                        scope.launch {
                                                            DB.reportdao.insertExcludedCat(
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
                                                    GeldflussDataItem(
                                                        trx = item,
                                                        onClicked = {
                                                            navigateTo(
                                                                ReportGeldflussDetailDest.also {
                                                                    it.reportid = reportid
                                                                    it.catid = item.catid
                                                                    navigateTo(it)
                                                                })
                                                        },
                                                        onVerglClicked = {
                                                            navigateTo(
                                                                ReportGeldflussVerglDetailDest.also {
                                                                    it.reportid = reportid
                                                                    it.catid = item.catid
                                                                    navigateTo(it)
                                                                })

                                                        })
                                                }
                                            })

                                    }
                                }
                        }

                    }
                }
            })
    }
}

@Composable
fun BottomSheetScaffoldContent(
    report: ReportBasisDaten,
    open: (ExcludedValuesSheet) -> Unit
) {
    var zeitraumState = rememberZeitraumCardState(selector = report.zeitraum).apply {
        von = report.von
        bis = report.bis
    }
    var zeitraumVerglState = rememberZeitraumCardState(selector = report.verglZeitraum).apply {
        von = report.verglVon
        bis = report.verglBis
    }

    ZeitraumCard(state = zeitraumState, onChanged = {
        zeitraumState = it
        report.zeitraum = it.zeitraum
        report.von = it.von
        report.bis = it.bis
        report.update()

    })
    ZeitraumCard(state = zeitraumVerglState, onChanged = {
        zeitraumVerglState = it
        report.verglZeitraum = it.zeitraum
        report.verglVon = it.von
        report.verglBis = it.bis
        report.update()
    })
    NavigationBar {
        NavigationBarItem(
            selected = false,
            onClick = { open(ExcludedValuesSheet.Classes) },
            icon = { Icon(Icons.Default.List, "") },
            label = {
                Text(
                    text = stringResource(id = R.string.catClasses),
                    style = MaterialTheme.typography.labelSmall
                )
            })

        NavigationBarItem(
            selected = false,
            onClick = { open(ExcludedValuesSheet.Cats) },
            icon = { Icon(Icons.Default.List, "") },
            label = {
                Text(
                    text = stringResource(id = R.string.cats),
                    style = MaterialTheme.typography.labelSmall
                )
            })
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

