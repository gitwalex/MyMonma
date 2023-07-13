package com.gerwalex.mymonma.ui.wp

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.database.room.DB.Companion.wpdao
import com.gerwalex.mymonma.database.room.MyConverter.NACHKOMMA
import com.gerwalex.mymonma.database.tables.CashTrx
import com.gerwalex.mymonma.database.tables.WPTrx
import com.gerwalex.mymonma.database.views.AccountDepotView
import com.gerwalex.mymonma.database.views.WPStammView
import com.gerwalex.mymonma.ext.rememberState
import com.gerwalex.mymonma.main.MonMaViewModel
import com.gerwalex.mymonma.ui.AppTheme
import com.gerwalex.mymonma.ui.content.AmountEditView
import com.gerwalex.mymonma.ui.content.AmountView
import com.gerwalex.mymonma.ui.content.DatePickerView
import com.gerwalex.mymonma.ui.content.MengeEditView
import com.gerwalex.mymonma.ui.navigation.Destination
import com.gerwalex.mymonma.ui.navigation.TopToolBar
import com.gerwalex.mymonma.ui.navigation.Up
import com.gerwalex.mymonma.ui.spinner.DepotSpinner
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.sql.Date

@Composable
fun WPKaufScreen(
    wpid: Long,
    viewModel: MonMaViewModel,
    navigateTo: (destination: Destination) -> Unit
) {
    var wp by rememberState { WPStammView() }
    var accounts by rememberState { listOf<AccountDepotView>() }
    LaunchedEffect(key1 = wpid) {
        wp = wpdao.getWPStamm(wpid)
        accounts = wpdao.getDepotList().first()
    }
    WPKaufScreen(wp = wp, accounts, navigateTo = navigateTo)
}

@Composable
fun WPKaufScreen(
    wp: WPStammView,
    accounts: List<AccountDepotView>,
    navigateTo: (destination: Destination) -> Unit
) {
    val scope = rememberCoroutineScope()
    var date by rememberState { Date(System.currentTimeMillis()) }
    var menge by rememberState { 0L }
    var kurs by rememberState { 0L }
    var gebuehren by rememberState { 0L }
    var depot by rememberState { AccountDepotView(id = -1) }
    val ausmBetrag by remember { derivedStateOf { ((kurs * menge) / NACHKOMMA) + gebuehren } }

    Scaffold(topBar = {
        TopToolBar(
            title = wp.name,
            actions = {
                IconButton(onClick = {
                    scope.launch {
                        insertKauf(wp, depot, date, kurs, menge, gebuehren, ausmBetrag)
                        navigateTo(Up)
                    }
                }) {
                    Icon(Icons.Default.Save, "")
                }
            },
            navigateTo = navigateTo
        )
    }) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = R.string.btag),
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    text = stringResource(id = R.string.depotname),
                    style = MaterialTheme.typography.labelSmall
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DatePickerView(date = date, onChanged = {
                    date = it
                })
                DepotSpinner(depot.id, accounts, selected = {
                    depot = it
                })
            }
            Divider()
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = R.string.anzahl),
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    text = stringResource(id = R.string.kurs),
                    style = MaterialTheme.typography.labelSmall
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MengeEditView(value = menge, onChanged = {
                    menge = it
                })
                AmountEditView(value = kurs, isSignBtnShown = false, onChanged = {
                    kurs = it
                })
            }
            Divider()
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = R.string.gebuehren),
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
                AmountEditView(value = gebuehren, isSignBtnShown = false, onChanged = {
                    gebuehren = it
                })
                AmountView(value = ausmBetrag)
            }

        }
    }
}

suspend fun insertKauf(
    wp: WPStammView,
    depot: AccountDepotView,
    date: Date,
    kurs: Long,
    menge: Long,
    gebuehren: Long,
    ausmBetrag: Long,
): WPTrx {
    val CATID = 2001L
    val wptrx = WPTrx().apply {
        btag = date
        accountid = depot.id
        this.menge = menge
        this.kurs = kurs
        this.einstand = ausmBetrag
        catid = CATID
        wpid = wp.id
        cashtrx = ArrayList<CashTrx>().also { cashTrxList ->
            val main = CashTrx().also { cashTrx ->
                cashTrx.btag = date
                cashTrx.partnerid = wp.id
                cashTrx.accountid = depot.id
                cashTrx.catid = depot.verrechnungskonto ?: depot.id
                cashTrx.amount = ausmBetrag
                cashTrx.memo = "Kauf ${wp.name} Anzahl: ${menge / NACHKOMMA}"
            }
            cashTrxList.add(main)
            if (gebuehren != 0L) {
                val geb = main.copy(catid = 10015, amount = -gebuehren)
                cashTrxList.add(geb)
            }
            main.gegenbuchung =
                main.copy(
                    accountid = main.catid,
                    catid = main.accountid,
                    amount = -main.amount
                )


        }
    }
    Log.d("WPKaufScreen", "WPKaufScreen: $wptrx, ${wptrx.cashtrx}")
    wpdao.insert(listOf(wptrx))
    return wptrx

}

@Preview(name = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun WPKaufScreenPreview() {
    val wp = WPStammView(name = "My Wertpapier")
    val depots = listOf(AccountDepotView())
    AppTheme {
        Surface {
            WPKaufScreen(wp, depots) {}

        }

    }
}
