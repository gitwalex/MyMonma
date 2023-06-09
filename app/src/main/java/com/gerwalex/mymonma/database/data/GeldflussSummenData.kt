package com.gerwalex.mymonma.database.data

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.room.DatabaseView
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.database.room.DB.Companion.reportdao
import com.gerwalex.mymonma.database.tables.ReportBasisDaten
import com.gerwalex.mymonma.enums.ReportTyp
import com.gerwalex.mymonma.ui.AppTheme
import com.gerwalex.mymonma.ui.content.AmountView
import com.gerwalex.mymonma.ui.content.DateView

@DatabaseView(
    """
            select reportid, sum(einnahmen) as einnahmen, sum(ausgaben) as ausgaben,
            sum(vergleinnahmen) as verglEinnahmen,  sum(verglausgaben) as verglAusgaben  from (   
            select a.id   
            ,(select sum(amount) from CashTrx b where btag between von and bis   
            and catid = a.id and incomecat) as einnahmen   
            ,(select sum(amount) from CashTrx b where btag between von and bis     
            and catid = a.id and not incomecat) as ausgaben   
            ,(select sum(amount) from CashTrx b where btag between  verglVon and verglBis   
            and catid = a.id and incomecat) as vergleinnahmen   
            ,(select sum(amount) from CashTrx b where btag between verglVon and verglBis   
            and catid = a.id and not incomecat) as verglausgaben   
            ,r.id as reportid
            from Cat a   
            join ReportBasisDaten r  
            where a.id not in (select catid from ReportExcludedCats d where d.reportid = r.id)
            and a.catclassid not in (select catclassid from ReportExcludedCatClasses d 
            where d.reportid = r.id
)   
            and catclassid > 100  )
            group by reportid
            

"""
)
data class GeldflussSummenData(
    val reportid: Long = 0,
    val id: Long? = null,
    val einnahmen: Long = 0,
    val ausgaben: Long = 0,
    val verglEinnahmen: Long = 0,
    val verglAusgaben: Long = 0,
)

@Composable
fun GeldflussSummen(
    report: ReportBasisDaten,
    modifier: Modifier = Modifier,
) {
    report.id?.let { reportid ->
        val reportsummen by reportdao.getGeldflussSummendaten(reportid)
            .collectAsStateWithLifecycle( GeldflussSummenData())
        GeldflussSummen(report, reportsummen, modifier)

    }


}

@Composable
fun GeldflussSummen(
    report: ReportBasisDaten,
    data: GeldflussSummenData,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    stringResource(id = R.string.einnahmen),
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = stringResource(id = R.string.zeitraum),
                    style = MaterialTheme.typography.labelMedium
                )
                AmountView(value = data.einnahmen)
                Text(
                    text = stringResource(id = R.string.verglZeitraum),
                    style = MaterialTheme.typography.labelMedium
                )
                AmountView(value = data.verglEinnahmen)
            }

            Column {
                Text(
                    stringResource(id = R.string.ausgaben),
                    style = MaterialTheme.typography.labelMedium
                )
                DateView(
                    date = report.von, style = MaterialTheme.typography.labelMedium
                )
                AmountView(value = data.ausgaben)
                DateView(
                    date = report.verglVon, style = MaterialTheme.typography.labelMedium
                )
                AmountView(value = data.verglAusgaben)
            }
            Column {
                Text(
                    stringResource(id = R.string.summe),
                    style = MaterialTheme.typography.labelMedium
                )
                DateView(date = report.bis, style = MaterialTheme.typography.labelMedium)
                AmountView(value = data.einnahmen + data.ausgaben)
                DateView(
                    date = report.verglBis, style = MaterialTheme.typography.labelMedium
                )
                AmountView(value = data.verglEinnahmen + data.verglAusgaben)
            }
        }
    }
}

@Preview(name = "Light", uiMode = UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun GeldflussSummenPreview() {
    val data = GeldflussSummenData(
        reportid = 1,
        id = 1,
        einnahmen = 1000_00,
        ausgaben = -5000_00,
        verglEinnahmen = 1500_00,
        verglAusgaben = -7000_00
    )
    AppTheme {
        Surface {
            GeldflussSummen(ReportBasisDaten(typ = ReportTyp.GeldflussVergl), data = data)
        }
    }

}