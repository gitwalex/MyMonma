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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.database.tables.ReportBasisDaten
import com.gerwalex.mymonma.enums.ReportDateSelector
import com.gerwalex.mymonma.enums.ReportDateSpinner
import com.gerwalex.mymonma.ui.content.DatePickerView

@Composable
fun ZeitraumCard(report: ReportBasisDaten) {
    Card(modifier = Modifier.padding(4.dp)) {
        Column {
            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = stringResource(id = R.string.zeitraum),
                    maxLines = 1,
                    textAlign = TextAlign.Center
                )
                ReportDateSpinner(selector = report.zeitraum, selected = {
                    report.zeitraum = it
                    report.von = it.dateSelection.startDate
                    report.bis = it.dateSelection.endDate
                    report.update()
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
                DatePickerView(date = report.von, onChanged = {
                    report.von = it
                    report.zeitraum = ReportDateSelector.EigDatum
                    report.update()
                })
                Spacer(modifier = Modifier.weight(1f))
                DatePickerView(date = report.bis, onChanged = {
                    report.bis = it
                    report.zeitraum = ReportDateSelector.EigDatum
                    report.update()
                })
            }
        }

    }
}
