package com.gerwalex.mymonma.enums

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.ui.AppTheme
import com.skydoves.orchestra.spinner.Spinner
import com.skydoves.orchestra.spinner.SpinnerProperties

enum class WPTrxArt {

    /*
     * Belegung:
     *
     * Einstand: Summe aller Einstände der zugehörigen Pakete, negativ
     *
     * Haltedauer: (negativ) durchschnittliche Haltedauer der Summe der Einstände. Die
     * durchschnittliche
     * Haltedauer wird errechnet wie folgt:
     *
     * Summe von [(paket1_einstand * paket1_haltedauer), (paket2_einstand *
     * paket2_haltedauer) +...+ (paketN_einstand * paketN_haltedauer)] geteilt durch Summe
     * der Einstände. Durch die Multiplikation werden die einzelnen Pakete gewichtet.
     *
     * amount: menge + kurs, negativ
     *
     *
     */
    Kauf {
        override val catid = 2001L
        override val bezeichnung = R.string.kauf
    },
    VerKauf {
        override val catid = 2050L
        override val bezeichnung = R.string.verkauf
    },
    Income {
        override val catid = 0L
        override val bezeichnung = R.string.einnahmen
    },
    Stammdaten {
        override val catid = 0L
        override val bezeichnung = R.string.stammdaten

    },
    Pakete {
        override val catid = 0L
        override val bezeichnung = R.string.pakete

    },
    WPSplitt {
        override val catid = 2090L
        override val bezeichnung = R.string.wpsplitt
    },
    Spin_Off {
        override val catid = 2091L
        override val bezeichnung = R.string.spinn_off

    },
    Stueckzins {
        override val catid = 2101L
        override val bezeichnung = R.string.stueckzins
        override val selectable = false

    },
    Thesaurierung {
        override val catid = 2103L
        override val bezeichnung = R.string.thesaurierung
        override val selectable = false

    },
    SonstEinnahmen {
        override val catid = 2104L
        override val bezeichnung = R.string.sonstEinnahmen
        override val selectable = false

    },
    DivIn {
        override val catid = 2105L
        override val bezeichnung = R.string.dividende
        override val selectable = false

    },
    DivAus {
        override val catid = 2106L
        override val bezeichnung = R.string.divAus
        override val selectable = false

    },
    ZinsEin {
        override val catid = 2107L
        override val bezeichnung = R.string.zinsEin
        override val selectable = false

    },
    Ausschuettung {
        override val catid = 2108L
        override val bezeichnung = R.string.ausschuettung
        override val selectable = false

    },
    Einnahmen {
        override val catid = 2111L
        override val bezeichnung = R.string.einnahmen
        override val selectable = false

    },

    /**
     * Belegung:
     * <p>
     * paketid: _id des verkauften Pakets
     * <p>
     * amount: der realisierte Gewinn eines Paketes, positiv
     * <p>
     * kurs: verkaufkurs (positiv)
     * <p>
     * menge: Menge des aus dem Paekts verkauften Bestands. Negativ
     * <p>
     * einstand: (Anteiliger) Einstandspreis der verkauften Anteile. Negativ
     * <p>
     * haltedauer: Haltedauer des Pakets. Negativ
     */

    Kursgewinn {
        override val catid = 2200L
        override val bezeichnung = R.string.kursgewinne
        override val selectable = false

    },

    /**
     * Belegung:
     * <p>
     * paketid: _id des verkauften Pakets
     * <p>
     * amount: der realisierte Verlust eines Paketes, negativ
     * <p>
     * kurs: verkaufkurs (positiv)
     * <p>
     * menge: Menge des aus dem Paekts verkauften Bestands. Negativ).
     * <p>
     * einstand: (Anteiliger) Einstandspreis der verkauften Anteile. Negativ
     */
    Kursverlust {
        override val catid = 2201L
        override val bezeichnung = R.string.kursverluste
        override val selectable = false

    },
    Bankgebuehren {
        override val catid = 10015L
        override val bezeichnung = R.string.gebuehren

    },
    Abgeltungssteuer {
        override val catid = 2500L
        override val bezeichnung = R.string.abgeltSteuer
        override val selectable = false

    },
    Umbuchung {
        override val catid: Nothing
            get() {
                throw NullPointerException("Bei einer Umbuchung ist die catid mit der VerrechnungskontoID zu belegen")
            }
        override val bezeichnung = R.string.umbuchung
        override val selectable = false

    };


    abstract val catid: Long
    abstract val bezeichnung: Int
    open val selectable: Boolean = true

    companion object {
        /**
         * Sucht eine WPTrxArt zu einer catid.
         * @throws IllegalArgumentException, wenn keine WPTrxArt gefunden
         */
        fun find(catid: Long): WPTrxArt {
            for (typ in values()) {
                if (typ.catid == catid) {
                    return typ
                }
            }
            throw IllegalArgumentException("keine WPTrxArt zu catid gefunden - catid: $catid")
        }


    }
}

@Composable
fun WPTrxArtSpinner(onItemSelected: (WPTrxArt) -> Unit) {
    val list = WPTrxArt.values().asList()
    val texte = ArrayList<String>().apply {
        list.forEach {
            add(stringResource(id = it.bezeichnung))
        }
    }
    val (selectedItem, setSelectedItem)
            = remember { mutableStateOf(WPTrxArt.Kauf) }
    Spinner(
        text = stringResource(id = selectedItem.bezeichnung),
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
            spinnerPadding = 4.dp,
            spinnerBackgroundColor = MaterialTheme.colorScheme.onBackground,
        ),
        onSpinnerItemSelected = { index, item ->
            setSelectedItem(list[index])
            onItemSelected(list[index])
        }
    )
}

@Composable
fun WPTrxArtMenu(
    selected: (WPTrxArt) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    Box(contentAlignment = Alignment.Center) {
        IconButton(onClick = {
            isExpanded = !isExpanded
        }) {
            Icon(
                Icons.Default.MoreVert, "",
                tint = MaterialTheme.colorScheme.onTertiary
            )
        }
        DropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false }) {
            WPTrxArt.values()
                .filter { it.selectable }
                .forEachIndexed { index, s ->
                    DropdownMenuItem(
                        text = { Text(text = stringResource(id = s.bezeichnung)) },
                        onClick = {
                            selected(WPTrxArt.values()[index])
                            isExpanded = false
                        })
                }

        }
    }
}

@Preview(name = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun WPTrxArtSpinnerPreview() {
    AppTheme {
        Surface {
            WPTrxArtSpinner(onItemSelected = {})

        }
    }

}

@Preview(name = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun WPTrxArtMenuPreview() {
    AppTheme {
        Surface {
            WPTrxArtMenu(selected = {})

        }
    }

}