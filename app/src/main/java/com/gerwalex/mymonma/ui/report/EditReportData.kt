package com.gerwalex.mymonma.ui.report

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.database.room.DB.reportdao
import com.gerwalex.mymonma.database.tables.ReportBasisDaten
import com.gerwalex.mymonma.enums.ReportDateSelector
import com.gerwalex.mymonma.enums.ReportDateSpinner
import com.gerwalex.mymonma.enums.ReportTyp
import com.gerwalex.mymonma.enums.ReportTypSpinner
import com.gerwalex.mymonma.ext.rememberState
import com.gerwalex.mymonma.main.MonMaViewModel
import com.gerwalex.mymonma.ui.AppTheme
import com.gerwalex.mymonma.ui.content.DatePickerView
import com.gerwalex.mymonma.ui.navigation.Destination
import com.gerwalex.mymonma.ui.navigation.TopToolBar
import com.gerwalex.mymonma.ui.navigation.Up
import kotlinx.coroutines.launch


@Composable
fun AddReportData(viewModel: MonMaViewModel, navigateTo: (Destination) -> Unit) {
    var reportid by rememberState { 0L }
    LaunchedEffect(Unit) {
        reportid = reportdao.insert(ReportBasisDaten())
    }
    if (reportid > 0) {
        EditReportData(reportid, viewModel) {
            navigateTo(it)
        }
    }
}


@Composable
fun EditReportData(reportid: Long, viewModel: MonMaViewModel, navigateTo: (Destination) -> Unit) {
    val report by reportdao.getReportBasisDaten(reportid).collectAsState(ReportBasisDaten())
    report.id?.let {
        EditReportData(report) {
            navigateTo(it)
        }
    }
}

@Composable
fun EditReportData(
    report: ReportBasisDaten,
    navigateTo: (Destination) -> Unit
) {
    val scope = rememberCoroutineScope()
    var name by rememberState { report.name }
    var description by rememberState { report.description ?: "" }
    var typ by rememberState { report.typ }
    var error by rememberState { "" }
    val snackbarHostState = remember { SnackbarHostState() }
    DisposableEffect(key1 = Unit) {
        onDispose {
            scope.launch {
                report.name = name
                report.description = description.ifEmpty { null }
                report.typ = typ
                reportdao.update(report)
            }
        }
    }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            val errorMissingName = stringResource(id = R.string.errorMissingName)
            TopToolBar(
                stringResource(report.id?.let { R.string.reportBearbeiten }
                    ?: R.string.reportNeu),
            ) {
                if (name.isEmpty()) {
                    error = errorMissingName
                } else {
                    navigateTo(Up)
                }
            }
        }) { padding ->

        Column(
            modifier = Modifier.padding(padding),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            OutlinedTextField(
                value = name,
                supportingText = { Text(text = error) },
                onValueChange = { text -> name = text },
                label = { Text(text = stringResource(id = R.string.reportName)) }
            )
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text(text = stringResource(id = R.string.reportDesc)) }
            )
            ReportTypSpinner(typ = typ, selected = { typ = it })

            ZeitraumCard(report = report)
            VerglZeitraumCard(report = report)
        }
    }
}

@Composable
fun ZeitraumCard(report: ReportBasisDaten) {
    val scope = rememberCoroutineScope()
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

@Composable
fun VerglZeitraumCard(report: ReportBasisDaten) {
    val scope = rememberCoroutineScope()
    Card(modifier = Modifier.padding(4.dp)) {
        Column {
            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = stringResource(id = R.string.verglZeitraum),
                    maxLines = 1,
                    textAlign = TextAlign.Center
                )
                ReportDateSpinner(selector = report.verglZeitraum, selected = {
                    report.verglZeitraum = it
                    report.verglVon = it.dateSelection.startDate
                    report.verglBis = it.dateSelection.endDate
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
                DatePickerView(date = report.verglVon, onChanged = {
                    report.verglVon = it
                    report.verglZeitraum = ReportDateSelector.EigDatum
                    report.update()
                })
                Spacer(modifier = Modifier.weight(1f))
                DatePickerView(date = report.verglBis, onChanged = {
                    report.verglBis = it
                    report.verglZeitraum = ReportDateSelector.EigDatum
                    report.update()
                })
            }
        }

    }
}

@Preview(name = "Light", uiMode = UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun EditReportDataPreview() {
    val report = ReportBasisDaten(
        typ = ReportTyp.GeldflussVergl,
        name = "my Report Preview",
        description = "my Report Preview Description"
    )
    AppTheme {
        Surface {
            EditReportData(report = report, navigateTo = {})
        }
    }
}