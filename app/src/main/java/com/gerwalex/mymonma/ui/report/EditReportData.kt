package com.gerwalex.mymonma.ui.report

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.database.room.DB.reportdao
import com.gerwalex.mymonma.database.tables.ReportBasisDaten
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
    val report by remember { mutableStateOf(ReportBasisDaten()) }
    EditReportData(report) {
        navigateTo(it)
    }
}


@Composable
fun EditReportData(viewModel: MonMaViewModel, navigateTo: (Destination) -> Unit) {
    val reportid = rememberSaveable { viewModel.reportId }
    var report by remember { mutableStateOf(ReportBasisDaten()) }
    LaunchedEffect(key1 = reportid) {
        reportdao.getReportBasisDaten(reportid)?.also {
            report = it
        }
    }
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
    val snackbarHostState = remember { SnackbarHostState() }
    val errorMissingName = stringResource(id = R.string.errorMissingName)
    var name by rememberState { report.name }
    var typ by rememberState { report.typ }
    var description by rememberState { report.description ?: "" }
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
                                report.typ = typ
                                report.name = name
                                report.description = description.ifEmpty { null }
                                if (report.name.isEmpty()) {
                                    snackbarHostState.showSnackbar(errorMissingName)
                                } else {
                                    report.id?.let {
                                        reportdao.update(report)
                                    } ?: reportdao.insert(report)
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
                value = name,
                onValueChange = { text -> name = text },
                label = { Text(text = stringResource(id = R.string.reportName)) }
            )
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text(text = stringResource(id = R.string.reportDesc)) }
            )
            ReportTypSpinner(typ = report.typ, selected = {
                typ = it
            })
            Card(modifier = Modifier.padding(4.dp)) {
                Column {
                    Text(
                        text = stringResource(id = R.string.zeitraum),
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 1,
                        textAlign = TextAlign.Center
                    )
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
                        DatePickerView(date = report.von, onChanged = { report.von = it })
                        Spacer(modifier = Modifier.weight(1f))
                        DatePickerView(date = report.bis, onChanged = { report.bis = it })
                    }
                }
            }
            if (typ.isVergl) {
                Card(modifier = Modifier.padding(4.dp)) {
                    Column {
                        Text(
                            text = stringResource(id = R.string.verglZeitraum),
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 1,
                            textAlign = TextAlign.Center
                        )
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
                            DatePickerView(
                                date = report.verglVon ?: report.von,
                                onChanged = { report.verglVon = it })
                            Spacer(modifier = Modifier.weight(1f))
                            DatePickerView(
                                date = report.verglBis ?: report.bis,
                                onChanged = { report.verglBis = it })
                        }
                    }
                }
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