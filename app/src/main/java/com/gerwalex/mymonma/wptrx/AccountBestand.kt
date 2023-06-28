package com.gerwalex.mymonma.wptrx

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Ignore
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.database.tables.WPTrx
import com.gerwalex.mymonma.ui.AppTheme
import com.gerwalex.mymonma.ui.content.AmountEditView
import com.gerwalex.mymonma.ui.content.AmountView

data class AccountBestand(

    var id: Long,
    var name: String,
    var bestand: Long = 0L,
    var verrechnungskonto: Long? = 0,
    var vname: String = "", // Name Verrechnungskonto
) {
    @Ignore
    var amount: Long = 0

    @Ignore
    var abgeltSteuer: Long = 0

    @Ignore
    var gebuehren: Long = 0

    @Ignore
    var menge: Long = 0

    @Ignore
    var kurs: Long = 0

}


@Composable
fun AccountIncomeVerrechnungItem(
    name: String,
    trx: WPTrx,
    modifier: Modifier = Modifier,
    onChanged: (WPTrx) -> Unit
) {
    var amount by remember { mutableStateOf(trx.amount) }
    var abgeltSteuer by remember { mutableStateOf(trx.abgeltungssteuer) }
    Box(modifier = modifier) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = name, fontWeight = FontWeight.Bold)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = R.string.einnahmen),
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    text = stringResource(id = R.string.abgeltSteuer),
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    text = stringResource(id = R.string.ausmBetrag),
                    style = MaterialTheme.typography.labelSmall
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val ausmBetrag by remember(key1 = amount, abgeltSteuer) {
                    trx.amount = amount
                    trx.abgeltungssteuer = abgeltSteuer
                    onChanged(trx)
                    mutableStateOf(amount + abgeltSteuer)
                }
                AmountEditView(value = amount, onChanged = { amount = it })
                AmountEditView(value = abgeltSteuer, onChanged = { abgeltSteuer = it })
                AmountView(value = ausmBetrag)
            }
        }
    }
}

@Preview(name = "Light", uiMode = UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun AccountBestandIncomeItemPreview() {
    val wpTrx = WPTrx(
        id = 1, amount = 120000,
        abgeltungssteuer = 12000
    )
    AppTheme {
        Surface {
            AccountIncomeVerrechnungItem("Verrechnungskonto", wpTrx) {}
        }
    }

}

