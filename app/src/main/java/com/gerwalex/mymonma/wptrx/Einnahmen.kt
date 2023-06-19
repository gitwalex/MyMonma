package com.gerwalex.mymonma.wptrx

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.MutableLiveData
import com.gerwalex.mymonma.MonMaViewModel
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.database.room.DB.dao
import com.gerwalex.mymonma.database.room.MyConverter.NACHKOMMA
import com.gerwalex.mymonma.database.tables.Cat
import com.gerwalex.mymonma.database.tables.Cat.Companion.KONTOCLASS
import com.gerwalex.mymonma.database.views.CashTrxView
import com.gerwalex.mymonma.database.views.WPStammView
import com.gerwalex.mymonma.enums.WPTrxArt
import com.gerwalex.mymonma.enums.WPTyp
import com.gerwalex.mymonma.ui.AppTheme
import com.gerwalex.mymonma.ui.content.AmountEditView
import com.gerwalex.mymonma.ui.content.AmountView
import com.gerwalex.mymonma.ui.content.DatePickerView
import com.gerwalex.mymonma.ui.content.MengeView
import com.gerwalex.mymonma.ui.navigation.Destination
import com.gerwalex.mymonma.ui.navigation.TopToolBar
import com.gerwalex.mymonma.ui.navigation.Up
import kotlinx.coroutines.launch
import java.sql.Date

object Einnahmen : WPTrxTemplate() {
    override fun executeTransaktion() {
        TODO("Not yet implemented")
    }
}

@Composable
fun EinnahmenScreen(viewModel: MonMaViewModel, navigateTo: (Destination) -> Unit) {
    val wp = viewModel.wpstamm
    val account = MutableLiveData<AccountBestand>()
    val scope = rememberCoroutineScope()
    wp?.let { stamm ->
        val trxArt =
            when (stamm.wptyp) {
                WPTyp.Aktie -> WPTrxArt.DivIn
                WPTyp.Anleihe -> WPTrxArt.ZinsEin
                else -> WPTrxArt.Einnahmen
            }
        val depot by account.observeAsState()
        Scaffold(
            topBar = {
                TopToolBar(
                    title = wp.name, navigateTo = navigateTo,
                )
            }
        ) { padding ->
            Column(modifier = Modifier.padding(padding)) {
                Row {
                    Text(text = stringResource(id = R.string.gesamtbestand))
                    MengeView(value = stamm.bestand)
                }
                AccountMitBestandSpinner(wpid = stamm.id) {
                    account.postValue(it)
                }
                EinnahmenScreen(depot, wp = stamm, trxArt) { state ->
                    scope.launch {
                        val trxList = ArrayList<CashTrxView>()
                        val main = CashTrxView(
                            accountid = state.depot.id,
                            btag = state.btag,
                            amount = state.amount,
                            catid = state.catid,
                            partnerid = wp.id,
                        )
                        trxList.apply {
                            add(main)
                            add(
                                main.copy(
                                    catid = Cat.AbgeltSteuerCatid,
                                    amount = state.abgeltSteuer
                                )
                            )
                            add(
                                main.copy(
                                    catid = state.depot.verrechnungskonto ?: state.depot.id,
                                    amount = state.amount + state.abgeltSteuer,
                                    catclassid = KONTOCLASS
                                )
                            )
                            dao.insertCashTrxView(trxList)
                            navigateTo(Up)
                        }

                    }

                }
            }
        }
    }
}


@Composable
fun EinnahmenScreen(
    account: AccountBestand?,
    wp: WPStammView,
    trxArt: WPTrxArt,
    onSaveClicked: (state: EinnahmeScreenState) -> Unit,
) {
    account?.let { depot ->
        val state = rememberScreenState(depot, wp = wp, wpTrxArt = trxArt)
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(modifier = Modifier.weight(1f)) {
                item {
                    Column {
                        Row {
                            DatePickerView(date = state.btag, onChanged = {
                                state.btag = it
                            })
                            Spacer(modifier = Modifier.weight(1f))
                            Text(text = stringResource(id = R.string.bestandAktuell))
                            MengeView(value = depot.bestand)
                        }
                        Row {
                            Text(text = stringResource(id = state.wpTrxArt.bezeichnung))
                            Spacer(modifier = Modifier.weight(1f))
                            AmountEditView(value = state.amount) {
                                state.amount = it
                            }
                        }
                        Row {
                            Text(text = stringResource(id = R.string.abgeltSteuer))
                            Spacer(modifier = Modifier.weight(1f))
                            AmountEditView(value = state.abgeltSteuer) {
                                state.abgeltSteuer = it
                            }
                        }
                        Row {
                            Text(text = stringResource(id = R.string.ausmBetrag))
                            Spacer(modifier = Modifier.weight(1f))
                            AmountView(value = state.amount + state.abgeltSteuer)
                        }
                        if (state.wp.bestand > 0) {
                            Row {
                                Text(text = stringResource(id = R.string.jeAnteil))
                                Spacer(modifier = Modifier.weight(1f))
                                AmountView(value = state.amount / (state.depot.bestand / NACHKOMMA))
                            }

                        }
                    }

                }
            }
            Row {
                Spacer(modifier = Modifier.weight(1f))
                TextButton(onClick = { onSaveClicked(state) }) {
                    Text(text = stringResource(id = R.string.save))
                }

            }
        }
    }
}

class EinnahmeScreenState(val depot: AccountBestand, val wp: WPStammView, val wpTrxArt: WPTrxArt) {
    var wpid by mutableStateOf(wp.id)
    var catid by mutableStateOf(wpTrxArt.catid)
    var btag by mutableStateOf(Date(System.currentTimeMillis()))
    var amount by mutableStateOf(0L)
    var abgeltSteuer by mutableStateOf(0L)

}

@Composable
fun rememberScreenState(depot: AccountBestand, wp: WPStammView, wpTrxArt: WPTrxArt) = remember {
    EinnahmeScreenState(depot, wp, wpTrxArt)
}

@Preview(name = "Light", uiMode = UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun EinnahmenScreenPreview() {
    val depot = AccountBestand(id = 1, name = "Depot1", bestand = 1000 * NACHKOMMA)
    AppTheme {
        Surface {
            val wpstamm = WPStammView(id = 1, name = "My Share", bestand = 30000 * NACHKOMMA)
            EinnahmenScreen(depot, wpstamm, WPTrxArt.DivIn) {}

        }

    }
}