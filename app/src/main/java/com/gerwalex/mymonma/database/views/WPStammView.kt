package com.gerwalex.mymonma.database.views

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
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
import androidx.compose.ui.viewinterop.AndroidView
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.database.room.MyConverter.NACHKOMMA
import com.gerwalex.mymonma.enums.WPTrxArtMenu
import com.gerwalex.mymonma.enums.WPTyp
import com.gerwalex.mymonma.ui.AppTheme
import com.gerwalex.mymonma.ui.content.AmountView
import com.gerwalex.mymonma.ui.content.MengeView
import com.gerwalex.mymonma.ui.views.KursLineChart
import java.sql.Date

data class WPStammView(
    val id: Long? = null,
    val name: String = "",
    val partnerid: Long = 0,
    val wpkenn: String = "",
    val isin: String? = "",
    val wptyp: WPTyp = WPTyp.Aktie,
    val risiko: Int = 2,
    val beobachten: Boolean = true,
    val estEarning: Long = 0,
    val bestand: Long = 0,
    val einstand: Long = 0,
    val gesamteinkaufpreis: Long = 0,
    val anzahlkauf: Long = 0,
    val firstums: Date = Date(System.currentTimeMillis()),
    val lastums: Date = Date(System.currentTimeMillis()),
    val gesamtkauf: Long = 0,
    val kursverlust: Long = 0,
    val kursgewinn: Long = 0,
    val income: Long = 0,
    val lastkurs: Long = 0,
    val lastbtag: Date? = Date(System.currentTimeMillis())
)

@Composable
fun WPStammItem(item: WPStammView) {
    Card(modifier = Modifier.padding(4.dp)) {
        Column(
            modifier = Modifier
                .padding(8.dp),
        ) {
            Row(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.tertiary),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.name,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onTertiary
                )
                Spacer(modifier = Modifier.weight(1f))
                WPTrxArtMenu(selected = {})
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(id = R.string.marktwert), fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.weight(1f))
                AmountView(value = item.bestand / NACHKOMMA * item.lastkurs)
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(id = R.string.einstand))
                Spacer(modifier = Modifier.weight(1f))
                AmountView(value = item.einstand)
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(id = R.string.buchgewinn))
                Spacer(modifier = Modifier.weight(1f))
                AmountView(value = (item.bestand / NACHKOMMA * item.lastkurs) - item.einstand)
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(id = R.string.kurs))
                Spacer(modifier = Modifier.weight(1f))
                AmountView(value = item.lastkurs)
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(id = R.string.bestand))
                Spacer(modifier = Modifier.weight(1f))
                MengeView(value = item.bestand)
            }
            AndroidView(factory = {
                KursLineChart(context = it, wpid = item.id!!).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                    )
                }
            })

        }

    }

}

@Preview(name = "Light", uiMode = UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun WPStammItemPreview() {
    val item = WPStammView(
        name = "WPName",
        bestand = 1000 * NACHKOMMA,
        lastkurs = 1234,
        einstand = 987654

    )
    AppTheme {
        Surface {
            WPStammItem(item)

        }

    }
}