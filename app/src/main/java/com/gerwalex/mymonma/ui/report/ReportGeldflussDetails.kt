package com.gerwalex.mymonma.ui.report

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gerwalex.mymonma.database.views.CashTrxView
import com.gerwalex.mymonma.database.views.CashTrxViewItem
import com.gerwalex.mymonma.ext.scaleOnPress
import com.gerwalex.mymonma.main.MonMaViewModel
import com.gerwalex.mymonma.ui.content.NoEntriesBox
import com.gerwalex.mymonma.ui.navigation.Destination
import com.gerwalex.mymonma.ui.navigation.ReportList
import com.gerwalex.mymonma.ui.navigation.TopToolBar
import com.gerwalex.mymonma.ui.navigation.Up


@Composable
fun ReportGeldflussDetails(
    reportid: Long,
    catid: Long,
    viewModel: MonMaViewModel,
    navigateTo: (Destination) -> Unit
) {
    val list by viewModel.reportDetailsList(reportid, catid).collectAsState(initial = emptyList())
    Scaffold(
        topBar = {
            TopToolBar(
                stringResource(id = ReportList.title)
            ) {
                navigateTo(Up)
            }
        }) {

        Column(modifier = Modifier.padding(it)) {
            if (list.isNotEmpty()) {
                ReportGeldflussDetails(list = list, navigateTo)
            } else {
                NoEntriesBox()
            }
        }
    }
}

@Composable
fun ReportGeldflussDetails(
    list: List<CashTrxView>,
    navigateTo: (Destination) -> Unit,
) {
    val buttonInteractionSource = remember { MutableInteractionSource() }
    LazyColumn {
        items(list) { trx ->
            Card(
                modifier = Modifier
                    .padding(4.dp)
                    .clickable(
                        interactionSource = buttonInteractionSource,
                        indication = null,
                        onClick = {
                        })
                    .scaleOnPress(buttonInteractionSource),
            ) {
                CashTrxViewItem(trx = trx)
            }
        }
    }

}