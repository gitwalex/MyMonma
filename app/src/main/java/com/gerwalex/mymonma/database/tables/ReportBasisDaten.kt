package com.gerwalex.mymonma.database.tables

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.enums.ReportDateSelector
import com.gerwalex.mymonma.enums.ReportTyp
import com.gerwalex.mymonma.ui.AppTheme
import com.gerwalex.mymonma.ui.content.DateView
import java.sql.Date

@Entity
data class ReportBasisDaten(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var typ: ReportTyp = ReportTyp.Geldfluss,
    var name: String,
    var von: Date,
    var bis: Date,
    var verglVon: Date? = null,
    var verglBis: Date? = null,
    var description: String? = null,
)

@Composable
fun ReportBasisDatenItem(report: ReportBasisDaten, modifier: Modifier = Modifier) {
    Box(modifier) {


        Column(modifier = Modifier.padding(4.dp)) {
            Text(
                text = report.name, fontWeight = FontWeight.Bold,
            )
            Text(text = stringResource(id = report.typ.textID))
            Divider()
            Text(
                text = stringResource(id = R.string.zeitraum), fontStyle = FontStyle.Italic,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
            Row {
                DateView(date = report.von)
                Spacer(modifier = Modifier.weight(1f))
                DateView(date = report.bis)
            }
            if (report.typ.isVergl) {
                Divider()
                Text(
                    text = stringResource(id = R.string.verglZeitraum),
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
                Row {
                    DateView(date = report.verglVon!!)
                    Spacer(modifier = Modifier.weight(1f))
                    DateView(date = report.verglBis!!)
                }
            }
            report.description?.let {
                Divider()
                Text(text = it, maxLines = 3)
            }
        }
    }
}

@Preview(name = "Light", uiMode = UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = UI_MODE_NIGHT_YES)
@Preview(name = "Light", widthDp = 250)
@Composable
fun ReportBasisDatenItemPreview() {
    val report = ReportBasisDaten(
        name = "Mein Report",
        typ = ReportTyp.GeldflussVergl,
        von = ReportDateSelector.AktJhr.dateSelection.startDate,
        bis = ReportDateSelector.AktJhr.dateSelection.endDate,
        verglVon = ReportDateSelector.LztJhr.dateSelection.startDate,
        verglBis = ReportDateSelector.LztJhr.dateSelection.endDate,
        description = "meine Desription"
    )
    AppTheme {
        Surface {
            ReportBasisDatenItem(report)

        }

    }

}