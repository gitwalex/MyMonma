package com.gerwalex.mymonma.ui.report

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.gerwalex.mymonma.database.data.GeldflussData
import com.gerwalex.mymonma.database.data.GeldflussDataItem
import com.gerwalex.mymonma.database.data.GeldflussSummen
import com.gerwalex.mymonma.database.room.DB
import com.gerwalex.mymonma.database.tables.ReportBasisDaten
import com.gerwalex.mymonma.ui.content.NoEntriesBox
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun GeldflussDetailScreen(
    report: ReportBasisDaten,
    onSelected: (GeldflussData) -> Unit

) {
    val list by DB.reportdao.getReportGeldflussData(report.id!!)
        .collectAsState(initial = emptyList())
    if (list.isEmpty()) {
        NoEntriesBox()
    } else
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

