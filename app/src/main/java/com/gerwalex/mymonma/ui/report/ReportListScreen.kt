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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gerwalex.mymonma.database.room.DB.Companion.reportdao
import com.gerwalex.mymonma.database.tables.ReportBasisDaten
import com.gerwalex.mymonma.database.tables.ReportBasisDatenItem
import com.gerwalex.mymonma.enums.ReportTyp
import com.gerwalex.mymonma.main.MonMaViewModel
import com.gerwalex.mymonma.ui.content.NoEntriesBox
import com.gerwalex.mymonma.ui.navigation.AddReportDest
import com.gerwalex.mymonma.ui.navigation.Destination
import com.gerwalex.mymonma.ui.navigation.EditReportDest
import com.gerwalex.mymonma.ui.navigation.GeldflussDetailScreenDest
import com.gerwalex.mymonma.ui.navigation.PartnerDetailScreenDest
import com.gerwalex.mymonma.ui.navigation.ReportListDest
import com.gerwalex.mymonma.ui.navigation.TopToolBar
import com.gerwalex.mymonma.ui.navigation.Up


@Composable
fun ReportListScreen(viewModel: MonMaViewModel, navigateTo: (Destination) -> Unit) {
    val list by reportdao.getReportList().collectAsStateWithLifecycle(emptyList())
    Scaffold(
        topBar = {
            TopToolBar(
                stringResource(id = ReportListDest.title),
                actions = {
                    IconButton(
                        onClick = { navigateTo(AddReportDest) },
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
    if (list.isEmpty()) {
        NoEntriesBox()
    } else {

        LazyColumn {
            items(list) { reportBasisDaten ->
                Card(
                    modifier = Modifier
                        .padding(4.dp)
                        .clickable {
                            val destination: Destination
                            when (reportBasisDaten.typ) {
                                ReportTyp.GeldflussVergl -> {
                                    destination = GeldflussDetailScreenDest.apply {
                                        reportid = reportBasisDaten.id!!
                                    }
                                }

                                ReportTyp.Empfaenger -> {
                                    destination =
                                        PartnerDetailScreenDest.apply {
                                            reportid = reportBasisDaten.id!!
                                        }

                                }
                            }
                            navigateTo(destination)
                        }
                ) {
                    ReportBasisDatenItem(report = reportBasisDaten) {
                        navigateTo(EditReportDest.apply { id = reportBasisDaten.id!! })
                    }
                }
            }
        }

    }
}