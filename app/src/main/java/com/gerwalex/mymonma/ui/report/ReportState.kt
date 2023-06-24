package com.gerwalex.mymonma.ui.report

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.gerwalex.mymonma.database.tables.ReportBasisDaten

class ReportState(report: ReportBasisDaten) {
    var id = report.id
    var name by mutableStateOf(report.name)
    var typ by mutableStateOf(report.typ)
    var description by mutableStateOf(report.description)
    var zeitraum by mutableStateOf(report.zeitraum)
    var von by mutableStateOf(report.von)
    var bis by mutableStateOf(report.bis)
    var verglZeitraum by mutableStateOf(report.verglZeitraum)
    var verglVon by mutableStateOf(report.verglVon)
    var verglBis by mutableStateOf(report.verglBis)

    val report: ReportBasisDaten
        get() {
            return ReportBasisDaten(
                id = id,
                name = name,
                description = description?.ifEmpty { null },
                typ = typ,
                zeitraum = zeitraum,
                verglZeitraum = verglZeitraum,
                von = von,
                bis = bis,
                verglVon = verglVon,
                verglBis = verglBis
            )
        }
}

@Composable
fun rememberReportState(
    report: ReportBasisDaten
) = remember { ReportState(report) }