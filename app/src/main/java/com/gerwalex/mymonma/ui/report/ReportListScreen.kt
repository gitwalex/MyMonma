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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gerwalex.mymonma.database.room.DB.reportdao
import com.gerwalex.mymonma.database.tables.ReportBasisDaten
import com.gerwalex.mymonma.database.tables.ReportBasisDatenItem
import com.gerwalex.mymonma.ext.scaleOnPress
import com.gerwalex.mymonma.main.MonMaViewModel
import com.gerwalex.mymonma.ui.content.NoEntriesBox
import com.gerwalex.mymonma.ui.navigation.AddReport
import com.gerwalex.mymonma.ui.navigation.Destination
import com.gerwalex.mymonma.ui.navigation.EditReport
import com.gerwalex.mymonma.ui.navigation.ReportDetail
import com.gerwalex.mymonma.ui.navigation.ReportList
import com.gerwalex.mymonma.ui.navigation.TopToolBar
import com.gerwalex.mymonma.ui.navigation.Up


@Composable
fun ReportListScreen(viewModel: MonMaViewModel, navigateTo: (Destination) -> Unit) {
    val list by reportdao.getReportList().collectAsState(initial = emptyList())
    Scaffold(
        topBar = {
            TopToolBar(
                stringResource(id = ReportList.title),
                actions = {
                    IconButton(onClick = { navigateTo(AddReport) }) {
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
                            navigateTo(ReportDetail.apply { id = reportBasisDaten.id!! })
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