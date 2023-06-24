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

/**
 * Created by alex on 18.06.2015.
 */
enum class ReportTyp {
    Geldfluss {
        override val description = R.string.reportGeldflussDesc
        override val textID = R.string.reportGeldfluss
    },
    GeldflussVergl {
        override val description = R.string.reportGeldflussVerglDesc
        override val textID = R.string.reportGeldflussVergl
        override val isVergl = true

    },
    Empfaenger {
        override val description = R.string.reportEmpfaengerDesc
        override val textID = R.string.reportEmpfaenger

    }

    ;

    abstract val description: Int
    abstract val textID: Int

    /**
     * true, wenn es ein Report zu einem bestimmten Datum (== Report.bisDate() ) ist. Default =
     * false
     */
    open val isFixDay = false

    /**
     * true, wenn es ein Vergleichsreport ist. Default = false
     */
    open val isVergl = false
}

@Composable
fun ReportTypSpinner(typ: ReportTyp, selected: (ReportTyp) -> Unit) {
    var myTyp by rememberState(typ) { typ }
    var isExpanded by remember { mutableStateOf(false) }
    Box(contentAlignment = Alignment.Center) {
        Text(text = stringResource(id = myTyp.textID), modifier = Modifier.clickable {
            isExpanded = !isExpanded
        })
        DropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false }) {
            ReportTyp.values()
                .forEachIndexed { index, s ->
                    DropdownMenuItem(
                        text = { Text(text = stringResource(id = s.textID)) },
                        onClick = {
                            myTyp = ReportTyp.values()[index]
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
fun ReportSpinnerPreview() {
    AppTheme {
        Surface {
            ReportTypSpinner(typ = ReportTyp.Geldfluss, selected = {})

        }
    }

}