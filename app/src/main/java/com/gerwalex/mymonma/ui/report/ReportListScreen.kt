package com.gerwalex.mymonma.ui.report

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gerwalex.mymonma.database.room.DB.Companion.reportdao
import com.gerwalex.mymonma.database.tables.ReportBasisDaten
import com.gerwalex.mymonma.database.tables.ReportBasisDatenItem
import com.gerwalex.mymonma.ext.scaleOnPress
import com.gerwalex.mymonma.main.MonMaViewModel
import com.gerwalex.mymonma.ui.content.NoEntriesBox
import com.gerwalex.mymonma.ui.navigation.AddReport
import com.gerwalex.mymonma.ui.navigation.Destination
import com.gerwalex.mymonma.ui.navigation.EditReport
import com.gerwalex.mymonma.ui.navigation.ReportDetailScreen
import com.gerwalex.mymonma.ui.navigation.ReportList
import com.gerwalex.mymonma.ui.navigation.TopToolBar
import com.gerwalex.mymonma.ui.navigation.Up


@Composable
fun ReportListScreen(viewModel: MonMaViewModel, navigateTo: (Destination) -> Unit) {
    val list by reportdao.getReportList().collectAsStateWithLifecycle(emptyList())
    Scaffold(
        topBar = {
            TopToolBar(
                stringResource(id = ReportList.title),
                actions = {
                    IconButton(
                        onClick = { navigateTo(AddReport) },
                        modifier = Modifier.scale(1.5f)
                    ) {
                        Icon(imageVector = Icons.Default.Add, "")
                    }
                }) {
                navigateTo(Up)
            }
        }) {

        Column(modifier = Modifier.padding(it)) {
            if (list.isNotEmpty()) {
                ReportListScreen(list = list, navigateTo)
            } else {
                NoEntriesBox()
            }
        }
    }
}

@Composable
fun ReportListScreen(
    list: List<ReportBasisDaten>,
    navigateTo: (Destination) -> Unit,
) {
    val buttonInteractionSource = remember { MutableInteractionSource() }
    LazyColumn {
        items(list) { reportBasisDaten ->
            Card(
                modifier = Modifier
                    .padding(4.dp)
                    .clickable(
                        interactionSource = buttonInteractionSource,
                        indication = null,
                        onClick = {
                            navigateTo(ReportDetailScreen.apply { id = reportBasisDaten.id!! })
                        })
                    .scaleOnPress(buttonInteractionSource),
            ) {
                ReportBasisDatenItem(report = reportBasisDaten) {
                    navigateTo(EditReport.apply { id = reportBasisDaten.id!! })
                }
            }
        }
    }

}