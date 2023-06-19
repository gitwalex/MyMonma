package com.gerwalex.mymonma.wptrx

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.gerwalex.mymonma.MonMaViewModel
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.database.room.MyConverter.NACHKOMMA
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
import java.sql.Date

object Einnahmen : WPTrxTemplate() {
    override fun executeTransaktion() {
        TODO("Not yet implemented")
    }
}

@Composable
fun EinnahmenScreen(viewModel: MonMaViewModel, navigateTo: (Destination) -> Unit) {
    val wp = viewModel.wpstamm
    wp?.let { stamm ->
        val trxArt =
            when (stamm.wptyp) {
                WPTyp.Aktie -> WPTrxArt.DivIn
                WPTyp.Anleihe -> WPTrxArt.ZinsEin
                else -> WPTrxArt.Einnahmen
            }
        EinnahmenScreen(wp, trxArt, navigateTo)
    } ?: navigateTo(Up)
}


@Composable
fun EinnahmenScreen(wp: WPStammView, wpTrxArt: WPTrxArt, navigateTo: (Destination) -> Unit) {
    val state = rememberScreenState(wp = wp, wpTrxArt = wpTrxArt)

    Scaffold(
        topBar = {
            TopToolBar(
                title = wp.name, navigateTo = navigateTo,
                actions = {
                    IconButton(onClick = {
                        /*TODO*/
                    }) {
                        Icon(imageVector = Icons.Default.Save, contentDescription = "")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Row {
                DatePickerView(date = state.btag, onChanged = {
                    state.btag = it
                })
                Spacer(modifier = Modifier.weight(1f))
                Text(text = stringResource(id = R.string.bestandAktuell))
                MengeView(value = wp.bestand)
            }
            Row {
                Text(text = stringResource(id = wpTrxArt.bezeichnung))
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
            if (wp.bestand > 0) {
                Row {
                    Text(text = stringResource(id = R.string.jeAnteil))
                    Spacer(modifier = Modifier.weight(1f))
                    AmountView(value = state.amount / (wp.bestand / NACHKOMMA))
                }

            }

        }

    }
}

class EinnahmeScreenState(wp: WPStammView, wpTrxArt: WPTrxArt) {
    var wpid by mutableStateOf(wp.id)
    var catid by mutableStateOf(wpTrxArt.catid)
    var btag by mutableStateOf(Date(System.currentTimeMillis()))
    var amount by mutableStateOf(0L)
    var abgeltSteuer by mutableStateOf(0L)

}

@Composable
fun rememberScreenState(wp: WPStammView, wpTrxArt: WPTrxArt) = remember {
    EinnahmeScreenState(wp, wpTrxArt)
}

@Preview(name = "Light", uiMode = UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun EinnahmenScreenPreview() {
    AppTheme {
        Surface {
            val wpstamm = WPStammView(id = 1, name = "My Share", bestand = 100 * NACHKOMMA)
            EinnahmenScreen(wpstamm, WPTrxArt.DivIn) {

            }

        }

    }
}