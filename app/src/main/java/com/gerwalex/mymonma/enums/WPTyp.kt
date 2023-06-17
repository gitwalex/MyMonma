package com.gerwalex.mymonma.enums

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.ui.AppTheme
import com.skydoves.orchestra.spinner.Spinner
import com.skydoves.orchestra.spinner.SpinnerProperties

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
fun WPTypSpinner(onItemSelected: (WPTyp) -> Unit) {
    val list = WPTyp.values().asList()
    val texte = ArrayList<String>().apply {
        list.forEach {
            add(stringResource(id = it.nameTextRes))
        }
    }
    val (selectedItem, setSelectedItem)
            = remember { mutableStateOf(WPTyp.Aktie) }
    Spinner(
        text = stringResource(id = selectedItem.nameTextRes),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
//            .align(Alignment.CenterHorizontally)
            .background(MaterialTheme.colorScheme.secondaryContainer),
        itemList = texte,
        style = MaterialTheme.typography.bodyMedium,
        properties = SpinnerProperties(
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            textAlign = TextAlign.Center,
            showDivider = true,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            spinnerPadding = 16.dp,
            spinnerBackgroundColor = MaterialTheme.colorScheme.onBackground,
        ),
        onSpinnerItemSelected = { index, item ->
            setSelectedItem(list[index])
            onItemSelected(list[index])
        }
    )
}

@Preview(name = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun WPTypSpinnerPreview() {
    AppTheme {
        Surface {
            WPTypSpinner(onItemSelected = {})

        }
    }

}
