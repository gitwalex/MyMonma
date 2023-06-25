package com.gerwalex.mymonma.ui.report

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
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
import com.gerwalex.mymonma.main.MonMaViewModel
import com.gerwalex.mymonma.ui.AppTheme
import com.gerwalex.mymonma.ui.content.DatePickerView
import com.gerwalex.mymonma.ui.navigation.Destination
import com.gerwalex.mymonma.ui.navigation.TopToolBar
import com.gerwalex.mymonma.ui.navigation.Up
import com.gerwalex.mymonma.ui.states.ReportState
import com.gerwalex.mymonma.ui.states.rememberReportState
import kotlinx.coroutines.launch


@Composable
fun AddReportData(viewModel: MonMaViewModel, navigateTo: (Destination) -> Unit) {
    val report = rememberReportState(report = ReportBasisDaten())
    EditReportData(report) {
        navigateTo(it)
    }
}


@Composable
fun EditReportData(viewModel: MonMaViewModel, navigateTo: (Destination) -> Unit) {
    viewModel.reportId?.let { id ->
        val report by reportdao.getReportBasisDaten(id).collectAsState(ReportBasisDaten())
        report.id?.let {
            val myReport = rememberReportState(report = report)
            EditReportData(myReport) {
                navigateTo(it)
            }
        }
    }
}

@Composable
fun EditReportData(
    report: ReportState,
    navigateTo: (Destination) -> Unit
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val errorMissingName = stringResource(id = R.string.errorMissingName)
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopToolBar(
                stringResource(report.id?.let { R.string.reportBearbeiten }
                    ?: R.string.reportNeu),
                actions = {
                    IconButton(
                        onClick = {
                            scope.launch {
                                if (report.name.isEmpty()) {
                                    snackbarHostState.showSnackbar(errorMissingName)
                                } else {
                                    with(report.report) {
                                        id?.let {
                                            reportdao.update(this)
                                        } ?: reportdao.insert(this)

                                    }
                                    navigateTo(Up)
                                }
                            }
                        },
                        modifier = Modifier.scale(1.5f)
                    )
                    {
                        Icon(imageVector = Icons.Default.Save, "")
                    }
                }) {
                navigateTo(Up)
            }
        }) { padding ->

        Column(
            modifier = Modifier.padding(padding),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            OutlinedTextField(
                value = report.name,
                onValueChange = { text -> report.name = text },
                label = { Text(text = stringResource(id = R.string.reportName)) }
            )
            OutlinedTextField(
                value = report.description ?: "",
                onValueChange = { report.description = it },
                label = { Text(text = stringResource(id = R.string.reportDesc)) }
            )
            ReportTypSpinner(typ = report.typ, selected = {
                report.typ = it
            })

            ZeitraumCard(report = report)
            VerglZeitraumCard(report = report)
        }
    }
}

@Composable
fun ZeitraumCard(report: ReportState) {
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

                })
                Spacer(modifier = Modifier.weight(1f))
                DatePickerView(date = report.bis, onChanged = {
                    report.bis = it
                    report.zeitraum = ReportDateSelector.EigDatum
                })
            }
        }

    }
}

@Composable
fun VerglZeitraumCard(report: ReportState) {
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
                })
                Spacer(modifier = Modifier.weight(1f))
                DatePickerView(date = report.verglBis, onChanged = {
                    report.verglBis = it
                    report.verglZeitraum = ReportDateSelector.EigDatum
                })
            }
        }

    }
}

@Preview(name = "Light", uiMode = UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun EditReportDataPreview() {
    val report = rememberReportState(
        report = ReportBasisDaten(
            typ = ReportTyp.GeldflussVergl,
            name = "my Report Preview",
            description = "my Report Preview Description"
        )
    )
    AppTheme {
        Surface {
            EditReportData(report = report, navigateTo = {})
        }
    }
}