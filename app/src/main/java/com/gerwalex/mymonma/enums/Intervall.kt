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
import java.sql.Date
import java.util.Calendar
import java.util.GregorianCalendar

enum class Intervall {
    Monatlich {

        override val intervallNameTextResID = R.string.intervall_monat
        override fun getNextBtag(btag: Date): Date {
            val cal = GregorianCalendar.getInstance()
            cal.time = btag
            cal.add(Calendar.MONTH, 1)
            return Date(cal.time.time)
        }
    },
    Einmalig {

        override val intervallNameTextResID = R.string.intervall_einmalig
        override fun getNextBtag(btag: Date): Nothing {
            throw IllegalArgumentException("Für einen einmalige Auftrag gibt es kein Folgedatum")
        }
    },
    Zweimonatlich {

        override val intervallNameTextResID = R.string.intervall_alle2monate
        override fun getNextBtag(btag: Date): Date {
            val cal = GregorianCalendar.getInstance()
            cal.time = btag
            cal.add(Calendar.MONTH, 2)
            return Date(cal.time.time)
        }
    },
    Halbjaehrlich {

        override val intervallNameTextResID = R.string.intervall_halbjaehrlich
        override fun getNextBtag(btag: Date): Date {
            val cal = GregorianCalendar.getInstance()
            cal.time = btag
            cal.add(Calendar.MONTH, 6)
            return Date(cal.time.time)
        }
    },
    vierteljaehrlich {

        override val intervallNameTextResID = R.string.intervall_quartal
        override fun getNextBtag(btag: Date): Date {
            val cal = GregorianCalendar.getInstance()
            cal.time = btag
            cal.add(Calendar.MONTH, 3)
            return Date(cal.time.time)
        }
    },
    Jaehrlich {

        override val intervallNameTextResID = R.string.intervall_jahr
        override fun getNextBtag(btag: Date): Date {
            val cal = GregorianCalendar.getInstance()
            cal.time = btag
            cal.add(Calendar.YEAR, 1)
            return Date(cal.time.time)
        }
    },
    Woechentlich {
        override val intervallNameTextResID = R.string.intervall_woechentlich
        override fun getNextBtag(btag: Date): Date {
            val cal = GregorianCalendar.getInstance()
            cal.time = btag
            cal.add(Calendar.DAY_OF_WEEK, 7)
            return Date(cal.time.time)
        }
    };


    abstract val intervallNameTextResID: Int

    abstract fun getNextBtag(btag: Date): Date
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