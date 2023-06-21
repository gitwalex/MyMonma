package com.gerwalex.mymonma.ui.wp

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.database.room.DB.wpdao
import com.gerwalex.mymonma.database.room.MyConverter.NACHKOMMA
import com.gerwalex.mymonma.database.views.WPStammView
import com.gerwalex.mymonma.enums.WPTrxArt
import com.gerwalex.mymonma.enums.WPTyp
import com.gerwalex.mymonma.main.MonMaViewModel
import com.gerwalex.mymonma.ui.AppTheme
import com.gerwalex.mymonma.ui.content.AmountEditView
import com.gerwalex.mymonma.ui.content.AmountView
import com.gerwalex.mymonma.ui.content.DatePickerView
import com.gerwalex.mymonma.ui.content.MengeView
import com.gerwalex.mymonma.ui.navigation.Destination
import com.gerwalex.mymonma.ui.navigation.TopToolBar
import com.gerwalex.mymonma.ui.navigation.Up
import com.gerwalex.mymonma.wptrx.AccountBestand
import com.gerwalex.mymonma.wptrx.AccountBestandIncomeItem
import kotlinx.coroutines.launch
import java.sql.Date


@Composable
fun IncomeScreen(viewModel: MonMaViewModel, navigateTo: (Destination) -> Unit) {
    val wp = viewModel.wpstamm
    val scope = rememberCoroutineScope()


    wp?.let { stamm ->
        val accountbestand by wpdao.getAccountBestand(wp.id).collectAsState(initial = emptyList())
        var btag by remember { mutableStateOf(Date(System.currentTimeMillis())) }
        val trxArt =
            when (stamm.wptyp) {
                WPTyp.Aktie -> WPTrxArt.DivIn
                WPTyp.Anleihe -> WPTrxArt.ZinsEin
                else -> WPTrxArt.Income
            }
        Scaffold(
            topBar = {
                TopToolBar(
                    title = wp.name, navigateTo = navigateTo,
                    actions = {
                        IconButton(onClick = {
                            scope.launch {
                                accountbestand.forEach { acc ->
                                    acc.insertIncome(btag, wp, trxArt)
                                }
                                navigateTo(Up)
                            }
                        }) {
                            Icon(Icons.Default.Save, "")
                        }
                    }
                )
            }
        ) { padding ->
            Column(modifier = Modifier.padding(padding)) {
                Row {
                    DatePickerView(date = btag, onChanged = { btag = it })
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = stringResource(id = R.string.bestandAktuell))
                    MengeView(value = wp.bestand)
                }
                IncomeScreen(accountbestand, wp = stamm, trxArt)
            }
        }
    } ?: navigateTo(Up)
}


@Composable
fun IncomeScreen(
    accountbestand: List<AccountBestand>,
    wp: WPStammView,
    trxArt: WPTrxArt,
) {
    var amount by remember { mutableStateOf(0L) }
    var abgeltSteuer by remember { mutableStateOf(0L) }
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Column {
                Row {
                    Text(text = stringResource(id = trxArt.bezeichnung))
                    Spacer(modifier = Modifier.weight(1f))
                    AmountEditView(value = amount) {
                        amount = it
                    }
                }
                Row {
                    Text(text = stringResource(id = R.string.abgeltSteuer))
                    Spacer(modifier = Modifier.weight(1f))
                    AmountEditView(value = abgeltSteuer) {
                        abgeltSteuer = it
                    }
                }
                Row {
                    val ausmBetrag by remember(key1 = amount, abgeltSteuer) {
                        mutableStateOf(amount + abgeltSteuer)
                    }
                    Text(text = stringResource(id = R.string.ausmBetrag))
                    Spacer(modifier = Modifier.weight(1f))
                    AmountView(value = ausmBetrag)
                }
                if (wp.bestand > 0) {
                    Row {
                        Text(text = stringResource(id = R.string.jeAnteil))
                        Spacer(modifier = Modifier.weight(1f))
                        AmountView(value = amount / (wp.bestand / NACHKOMMA))
                    }

                }
            }

        }
        item { Divider(modifier = Modifier.padding(4.dp), 1.dp) }
        items(accountbestand) {

            AccountBestandIncomeItem(it) {
                var tAmount = 0L
                var tAbgeltSteuer = 0L
                var ausmBetrag = 0L
                accountbestand.forEach {
                    tAmount += it.amount
                    tAbgeltSteuer += it.abgeltSteuer
                    ausmBetrag += it.amount + it.abgeltSteuer
                }
                amount = tAmount
                abgeltSteuer = tAbgeltSteuer
            }
        }
    }
}


@Preview(name = "Light", uiMode = UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun IncomeScreenPreview() {
    val depot = AccountBestand(id = 1, name = "Depot1", bestand = 1000 * NACHKOMMA)
    val accountBestand = ArrayList<AccountBestand>().apply { add(depot) }
    AppTheme {
        Surface {
            val wpstamm = WPStammView(id = 1, name = "My Share", bestand = 30000 * NACHKOMMA)
            IncomeScreen(accountBestand, wpstamm, WPTrxArt.DivIn)

        }

    }
}