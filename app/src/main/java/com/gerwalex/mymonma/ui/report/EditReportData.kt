package com.gerwalex.mymonma.ui.report

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.database.room.DB.Companion.reportdao
import com.gerwalex.mymonma.database.tables.ReportBasisDaten
import com.gerwalex.mymonma.enums.ReportTyp
import com.gerwalex.mymonma.enums.ReportTypSpinner
import com.gerwalex.mymonma.ext.rememberState
import com.gerwalex.mymonma.main.MonMaViewModel
import com.gerwalex.mymonma.ui.AppTheme
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
    var report by rememberState { ReportBasisDaten() }
    LaunchedEffect(reportid) {
        reportdao.getReportBasisDaten(reportid)?.let {
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
    var zeitraumState = rememberZeitraumCardState(selector = report.zeitraum).apply {
        von = report.von
        bis = report.bis
    }
    var zeitraumVerglState = rememberZeitraumCardState(selector = report.verglZeitraum).apply {
        von = report.von
        bis = report.bis

    }
    val scope = rememberCoroutineScope()
    var name by rememberState { report.name }
    var description by rememberState { report.description ?: "" }
    var typ by rememberState { report.typ }
    var error by rememberState { "" }
    val snackbarHostState = remember { SnackbarHostState() }
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
                onValueChange = { text ->
                    name = text
                    scope.launch {
                        report.name = name
                        reportdao.update(report)
                    }
                },
                label = { Text(text = stringResource(id = R.string.reportName)) }
            )
            OutlinedTextField(
                value = description,
                onValueChange = {
                    description = it
                    scope.launch {
                        report.description = description.ifEmpty { null }
                        reportdao.update(report)
                    }
                },
                label = { Text(text = stringResource(id = R.string.reportDesc)) }
            )
            ReportTypSpinner(typ = typ, selected = {
                typ = it
                scope.launch {
                    report.typ = typ
                    reportdao.update(report)
                }
            })

            ZeitraumCard(
                state = zeitraumState,
                onChanged = {
                    zeitraumState = it
                    report.zeitraum = it.zeitraum
                    report.von = it.von
                    report.bis = it.bis
                    report.update()
                })
            ZeitraumCard(
                state = zeitraumVerglState,
                onChanged = {
                    zeitraumVerglState = it
                    report.verglZeitraum = it.zeitraum
                    report.verglVon = it.von
                    report.verglBis = it.bis
                    report.update()
                })
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