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
import com.gerwalex.mymonma.database.room.DB
import com.gerwalex.mymonma.database.tables.CashTrx
import com.gerwalex.mymonma.database.tables.Cat
import com.gerwalex.mymonma.database.views.WPStammView
import com.gerwalex.mymonma.enums.WPTrxArt
import com.gerwalex.mymonma.ui.AppTheme
import com.gerwalex.mymonma.ui.content.AmountEditView
import com.gerwalex.mymonma.ui.content.AmountView
import java.sql.Date

data class AccountBestand(

    var id: Long,
    var name: String,
    var bestand: Long = 0L,
    var verrechnungskonto: Long? = 0,
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

    suspend fun insertIncome(btag: Date, wp: WPStammView, trxArt: WPTrxArt) {
        val trxList = ArrayList<CashTrx>()
        val main = CashTrx(
            accountid = id,
            btag = btag,
            amount = amount,
            catid = trxArt.catid,
            partnerid = wp.id,
        )
        trxList.apply {
            add(main)
            if (abgeltSteuer != 0L) {
                add(
                    main.copy(
                        catid = Cat.AbgeltSteuerCatid,
                        amount = abgeltSteuer
                    )
                )
            }
            if (amount + abgeltSteuer != 0L) {
                val cashTrx = main.copy(
                    accountid = id,
                    catid = verrechnungskonto ?: id,
                    amount = -(amount + abgeltSteuer),
                ).also {
                    it.gegenbuchung = it.copy(
                        accountid = it.catid,
                        catid = it.accountid,
                        amount = -it.amount
                    )

                }

                add(
                    cashTrx
                )
            }
            DB.dao.insertCashTrx(trxList)

        }
    }
}

@Composable
fun AccountBestandIncomeItem(
    item: AccountBestand,
    modifier: Modifier = Modifier,
    onChanged: (AccountBestand) -> Unit
) {
    var amount by remember { mutableStateOf(item.amount) }
    var abgeltSteuer by remember { mutableStateOf(item.abgeltSteuer) }
    Box(modifier = modifier) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = item.name, fontWeight = FontWeight.Bold)
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
                    item.amount = amount
                    item.abgeltSteuer = abgeltSteuer
                    onChanged(item)
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
    val acc = AccountBestand(id = 1, name = "Depot 1").apply {
        amount = 120000
        abgeltSteuer = 12000
    }
    AppTheme {
        Surface {
            AccountBestandIncomeItem(item = acc) {}
        }
    }

}

