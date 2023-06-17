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

enum class Intervall {
    Monatlich {

        override val intervallNameTextResID = R.string.intervall_monat
        override val ausfuehrung = "0|0|1|0"
    },
    Einmalig {

        override val intervallNameTextResID = R.string.intervall_einmalig
        override val ausfuehrung = "0|0|0|0"
    },
    Zweimonatlich {

        override val intervallNameTextResID = R.string.intervall_alle2monate
        override val ausfuehrung = "0|0|2|0"
    },
    Halbjaehrlich {

        override val intervallNameTextResID = R.string.intervall_halbjaehrlich
        override val ausfuehrung = "0|0|6|0"
    },
    vierteljaehrlich {

        override val intervallNameTextResID = R.string.intervall_quartal
        override val ausfuehrung = "0|0|3|0"
    },
    Jaehrlich {

        override val intervallNameTextResID = R.string.intervall_jahr
        override val ausfuehrung = "0|0|0|1"
    },
    Woechentlich {

        override val intervallNameTextResID = R.string.intervall_woechentlich
        override val ausfuehrung = "1|0|0|0"
    };


    abstract val intervallNameTextResID: Int

    /**
     * Intervall der Ausfuehrung: Aufbau a|b|c|d mit Anzahl a=Tage, b=Wochen,
     * c=Monate,d=Jahre. null: Einmalig. Beispiel 0|1|2|1 = naechste Ausführung
     * in 0 Tagen + 1 Woche + 2 Monate + 1 Jahr
     */
    abstract val ausfuehrung: String
}

@Composable
fun IntervallSpinner(onItemSelected: (Intervall) -> Unit) {
    val list = Intervall.values().asList()
    val texte = ArrayList<String>().apply {
        list.forEach {
            add(stringResource(id = it.intervallNameTextResID))
        }
    }
    val (selectedItem, setSelectedItem)
            = remember { mutableStateOf(Intervall.Monatlich) }
    Spinner(
        text = stringResource(id = selectedItem.intervallNameTextResID),
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
fun IntervallSpinnerPreview() {
    AppTheme {
        Surface {
            IntervallSpinner(onItemSelected = {})

        }
    }

}