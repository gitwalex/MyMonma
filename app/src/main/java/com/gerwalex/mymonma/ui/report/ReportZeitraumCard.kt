package com.gerwalex.mymonma.ui.report

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.enums.ReportDateSelector
import com.gerwalex.mymonma.enums.ReportDateSpinner
import com.gerwalex.mymonma.ui.content.DateView

class ZeitraumCardState(selector: ReportDateSelector) {
    var zeitraum by mutableStateOf(selector)
    var von by mutableStateOf(selector.dateSelection.startDate)
    var bis by mutableStateOf(selector.dateSelection.endDate)
}

@Composable
fun rememberZeitraumCardState(
    selector: ReportDateSelector,
) = remember { ZeitraumCardState(selector = selector) }


@Composable
fun ZeitraumCard(
    state: ZeitraumCardState,
    onChanged: (ZeitraumCardState) -> Unit,
) {
    Card(modifier = Modifier.padding(4.dp)) {
        Column {
            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = stringResource(id = R.string.zeitraum),
                    maxLines = 1,
                    textAlign = TextAlign.Center
                )
                ReportDateSpinner(selector = state.zeitraum, selected = {
                    ZeitraumCardState(it).apply {
                        von = it.dateSelection.startDate
                        bis = it.dateSelection.endDate
                        onChanged(this)
                    }
                })

            }
            Row {
                Text(
                    text = stringResource(id = R.string.reportStart),
                    style = MaterialTheme.typography.labelMedium
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(id = R.string.reportEnde),
                    style = MaterialTheme.typography.labelMedium
                )
            }

            Row {
                DateView(date = state.von)
                Spacer(modifier = Modifier.weight(1f))
                DateView(date = state.bis)
            }
        }
    }
}

@Composable
fun ZeitraumCard(
    zeitraum: ReportDateSelector,
    onChanged: (zeitraum: ReportDateSelector) -> Unit
) {
    Card(modifier = Modifier.padding(4.dp)) {
        Column {
            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = stringResource(id = R.string.zeitraum),
                    maxLines = 1,
                    textAlign = TextAlign.Center
                )
                ReportDateSpinner(selector = zeitraum, selected = {
                    onChanged(it)
                })

            }
        }
    }
}


