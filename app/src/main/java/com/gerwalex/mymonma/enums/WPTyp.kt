package com.gerwalex.mymonma.enums

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.ext.rememberState
import com.gerwalex.mymonma.ui.AppTheme

enum class WPTyp {
    Aktie {
        override val nameTextRes = R.string.aktie
        override var einnahmeArt = WPTrxArt.DivIn
        override val einnahmeartTextResID = R.string.dividende
        override val risioko = 1
    },


    Fonds {
        override val nameTextRes = R.string.fonds
        override var einnahmeArt = WPTrxArt.Ausschuettung
        override val einnahmeartTextResID = R.string.ausschuettung
        override val areFractionAllowed: Boolean = true
    },

    Zertifikat {
        override val nameTextRes = R.string.zertifikat
        override var einnahmeArt = WPTrxArt.ZinsEin
        override val einnahmeartTextResID = R.string.einnahmen
    },

    Anleihe {
        override val nameTextRes = R.string.anleihe
        override var einnahmeArt = WPTrxArt.ZinsEin
        override val einnahmeartTextResID = R.string.zinsEin
    },

    ETF {
        override val nameTextRes = R.string.etf
        override var einnahmeArt = WPTrxArt.Ausschuettung
        override val einnahmeartTextResID = R.string.ausschuettung
    },

    Optionschein {
        override val nameTextRes = R.string.optionen
        override val einnahmeArt: Nothing
            get() = TODO("Not yet implemented")
        override val einnahmeartTextResID = R.string.na
    },


    Index {
        override val nameTextRes = R.string.index
        override val einnahmeArt: Nothing
            get() = TODO("Not yet implemented")
        override val einnahmeartTextResID = R.string.na
    },

    Discount {
        override val nameTextRes = R.string.index
        override val einnahmeArt: Nothing
            get() = TODO("Not yet implemented")
        override val einnahmeartTextResID = R.string.na
    };

    abstract val nameTextRes: Int
    abstract val einnahmeArt: WPTrxArt
    abstract val einnahmeartTextResID: Int
    open val areFractionAllowed: Boolean = false
    open val risioko: Int = 2
}

@Composable
fun WPTypSpinner(typ: WPTyp, selected: (WPTyp) -> Unit) {
    var myTyp by rememberState(typ) { typ }
    var isExpanded by remember { mutableStateOf(false) }
    Box(contentAlignment = Alignment.Center) {
        Text(
            text = stringResource(id = typ.nameTextRes),
            modifier = Modifier.clickable {
                isExpanded = !isExpanded
            })
        DropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false }) {
            ReportTyp.values()
                .forEachIndexed { index, s ->
                    DropdownMenuItem(
                        text = { Text(text = stringResource(id = s.textID)) },
                        onClick = {
                            myTyp = WPTyp.values()[index]
                            selected(myTyp)
                            isExpanded = false
                        })
                }

        }
    }
}

@Preview(name = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun WPTypSpinnerPreview() {
    AppTheme {
        Surface {
            WPTypSpinner(WPTyp.Anleihe, {})

        }
    }

}
