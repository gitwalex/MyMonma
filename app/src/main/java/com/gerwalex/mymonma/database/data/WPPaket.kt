package com.gerwalex.mymonma.database.data

import android.content.res.Configuration
import android.text.format.DateUtils
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.database.room.MyConverter.NACHKOMMA
import com.gerwalex.mymonma.ui.AppTheme
import com.gerwalex.mymonma.ui.LocalAppColors
import com.gerwalex.mymonma.ui.content.AmountView
import com.gerwalex.mymonma.ui.content.DateView
import com.gerwalex.mymonma.ui.content.MengeView
import com.gerwalex.mymonma.ui.content.PercentView
import java.sql.Date
import java.util.concurrent.TimeUnit

data class WPPaket(
    val id: Long,
    val name: String,
    val btag: Date,
    val accountid: Long,
    val wpid: Long,
    val catid: Long,
    val kaufkurs: Long,
    val menge: Long,
    val bestand: Long,
    val lastkurs: Long,
    val lastbtag: Date,

    ) {
    val einstand: Long
        get() {
            return kaufkurs * bestand / NACHKOMMA
        }
    val buchwert: Long
        get() {
            return lastkurs * bestand / NACHKOMMA

        }
    val gewinn: Long
        get() {
            return buchwert - einstand
        }
    val percent: Float
        get() {
            return gewinn * 100 / buchwert.toFloat()
        }
    val haltedauer: Long
        get() {
            return (System.currentTimeMillis() - btag.time) / DateUtils.DAY_IN_MILLIS
        }

}

@Composable
fun WPPaketItem(
    paket: WPPaket,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(LocalAppColors.current.WertpapierContainer),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = R.string.buchwert),
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
            AmountView(value = paket.buchwert, fontWeight = FontWeight.Bold)
        }
        Divider()
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = R.string.btag),
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                text = stringResource(id = R.string.kaufkurs),
                style = MaterialTheme.typography.labelSmall
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DateView(date = paket.btag)
            AmountView(value = paket.kaufkurs)
        }
        Divider()
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = R.string.bestand),
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                text = stringResource(id = R.string.einstand),
                style = MaterialTheme.typography.labelSmall
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            MengeView(value = paket.bestand)
            AmountView(value = paket.einstand)
        }
        Divider()
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = R.string.haltedauer),
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                text = stringResource(id = R.string.gewinn_verlust),
                style = MaterialTheme.typography.labelSmall
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = paket.haltedauer.toString())
            Column(horizontalAlignment = Alignment.End) {
                AmountView(value = paket.gewinn)
                PercentView(value = paket.percent)
            }
        }
    }
}

@Preview(name = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Light - size", uiMode = Configuration.UI_MODE_NIGHT_NO, widthDp = 250)
@Composable
fun WPPaketItemPreview() {
    val paket = WPPaket(
        id = 0,
        name = "Wertapiername",
        btag = Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(365)),
        accountid = 1,
        wpid = 1,
        catid = 1,
        kaufkurs = 50_01,
        menge = 123_000_000,
        bestand = 99_000_000,
        lastkurs = 55_50,
        lastbtag = Date(System.currentTimeMillis()),
    )
    AppTheme {
        Surface {
            WPPaketItem(paket)
        }
    }
}