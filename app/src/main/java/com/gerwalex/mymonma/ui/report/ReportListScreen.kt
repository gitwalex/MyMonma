package com.gerwalex.mymonma.ui.report

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.gerwalex.mymonma.database.room.DB.reportdao
import com.gerwalex.mymonma.database.tables.ReportBasisDaten
import com.gerwalex.mymonma.database.tables.ReportBasisDatenItem
import com.gerwalex.mymonma.main.MonMaViewModel
import com.gerwalex.mymonma.ui.content.NoEntriesBox
import com.gerwalex.mymonma.ui.navigation.AddCashTrx
import com.gerwalex.mymonma.ui.navigation.Destination
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
                    IconButton(onClick = { navigateTo(AddCashTrx) }) {
                        Icon(imageVector = Icons.Default.Add, "")
                    }
                }) {
                navigateTo(Up)
            }
        }) {

        Column(modifier = Modifier.padding(it)) {

            if (list.isNotEmpty()) {
                ReportListScreen(list) { report ->
                    viewModel.reportId = report.id!!

                }
            } else {
                NoEntriesBox()
            }
        }
    }
}

@Composable
fun ReportListScreen(
    list: List<ReportBasisDaten>,
    itemSelected: (ReportBasisDaten) -> Unit
) {
    LazyColumn {
        items(list) { item ->
            Card(modifier = Modifier.clickable {
                itemSelected(item)
            }) {

            }
            ReportBasisDatenItem(report = item)
        }
    }

}