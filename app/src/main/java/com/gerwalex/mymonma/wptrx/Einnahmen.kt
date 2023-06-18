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
import androidx.compose.runtime.saveable.rememberSaveable
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

data class EinnahmenTrx(
    var wpid: Long,
    val catid: Long = 0,
    var btag: Date = Date(System.currentTimeMillis()),
    var amount: Long = 0,
    var abgeltungSteuer: Long = 0,
)

@Composable
fun EinnahmenScreen(wp: WPStammView, wpTrxArt: WPTrxArt, navigateTo: (Destination) -> Unit) {
    var wpid = rememberSaveable { wp.id!! }
    var catid = rememberSaveable { wpTrxArt.catid }
    var btag = rememberSaveable { Date(System.currentTimeMillis()) }
    var amount = rememberSaveable { 0L }
    var abgeltSteuer = rememberSaveable { 0L }

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
                DatePickerView(date = btag, onChanged = {
                    btag = it
                })
                Spacer(modifier = Modifier.weight(1f))
                Text(text = stringResource(id = R.string.bestandAktuell))
                MengeView(value = wp.bestand)
            }
            Row {
                Text(text = stringResource(id = wpTrxArt.bezeichnung))
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
                Text(text = stringResource(id = R.string.ausmBetrag))
                Spacer(modifier = Modifier.weight(1f))
                AmountView(value = amount + abgeltSteuer)
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