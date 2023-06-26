package com.gerwalex.mymonma.database.data

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.room.DatabaseView
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.database.views.SplittedCatNameItem
import com.gerwalex.mymonma.ui.AppTheme
import com.gerwalex.mymonma.ui.content.AmountView
import com.gerwalex.mymonma.ui.content.PercentView

@DatabaseView(
    """
                select r.id as reportid, a.id as catid, a.name  
            ,(select sum(b.amount) from CashTrx b   
            where b.catid = a.id and b.btag between von and bis) as amount   
            ,(select count(b.amount) from CashTrx b   
            where b.catid = a.id and b.btag between von and bis) as repcnt   
            ,(select sum(b.amount) from CashTrx b   
            where b.catid = a.id and b.btag between verglVon and verglBis) as verglAmount   
            ,(select count(b.amount) from CashTrx b   
            where b.catid = a.id and b.btag between verglVon and verglBis) as verglRepcnt   
            from Cat a   
            left outer join ReportBasisDaten r
            where catclassid > 100 
            and a.catclassid not in (select catclassid from ReportExcludedCatClasses d   
            where d.reportid = r.id)   
            and a.id not in (select catid from ReportExcludedCats d   
            where d.reportid = r.id)   
            group by reportid, a.name having repcnt > 0 or verglRepcnt > 0   
            order by a.name

"""
)
data class GeldflussData(
    val reportid: Long,
    val catid: Long,
    val name: String,
    val amount: Long,
    val repcnt: Int,
    val verglAmount: Long,
    val verglRepcnt: Int,
)

@Composable
fun GeldflussDataItem(trx: GeldflussData, onClicked: () -> Unit) {
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .clickable {
                    onClicked()
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column { SplittedCatNameItem(name = trx.name) }
            Spacer(modifier = Modifier.weight(1f))
            Column(horizontalAlignment = Alignment.End) {
                AmountView(value = trx.amount, fontWeight = FontWeight.Bold)
                Text(
                    text = stringResource(id = R.string.vergleich),
                    style = MaterialTheme.typography.labelSmall
                )
                AmountView(
                    value = trx.verglAmount,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.labelSmall
                )
            }

        }
        Divider()
        Text(
            text = stringResource(id = R.string.differenz),
            style = MaterialTheme.typography.labelSmall
        )
        Row {
            AmountView(value = trx.amount - trx.verglAmount)
            if (trx.amount != 0L) {
                Text(text = " (")
                PercentView(value = (trx.amount - trx.verglAmount) * 100f / trx.verglAmount)
                Text(text = ")")
            }
        }
    }

}

@Preview(name = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun GeldflussDataItemPreview() {
    AppTheme {
        Surface {
            val data = GeldflussData(
                reportid = 1,
                catid = 0,
                name = "KategorieKlasse:KategorienArt:Kategorie ",
                amount = -123456L,
                repcnt = 123,
                verglAmount = -124563L,
                verglRepcnt = 100,

                )
            GeldflussDataItem(trx = data) {}

        }
    }
}

