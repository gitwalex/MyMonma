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
fun IntervallSpinner(intervall: Intervall, selected: (Intervall) -> Unit) {
    var myIntervall by rememberState { intervall }
    var isExpanded by remember { mutableStateOf(false) }
    Box(contentAlignment = Alignment.Center) {
        Text(
            text = stringResource(id = myIntervall.intervallNameTextResID),
            modifier = Modifier.clickable {
                isExpanded = !isExpanded
            })
        DropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false }) {
            ReportTyp.values()
                .forEachIndexed { index, s ->
                    DropdownMenuItem(
                        text = { Text(text = stringResource(id = s.textID)) },
                        onClick = {
                            myIntervall = Intervall.values()[index]
                            selected(myIntervall)
                            isExpanded = false
                        })
                }

        }
    }
}

@Preview(name = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun IntervallSpinnerPreview() {
    AppTheme {
        Surface {
            IntervallSpinner(Intervall.Monatlich, selected = {})

        }
    }

}