package com.gerwalex.mymonma.enums

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
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
import com.gerwalex.mymonma.database.tables.Cat
import com.gerwalex.mymonma.ui.AppTheme
import com.skydoves.orchestra.spinner.Spinner
import com.skydoves.orchestra.spinner.SpinnerProperties

enum class Kontotyp {
    Giro {

        override val supercatID = Cat.CASHKONTOCATID
        override var color = Color.parseColor("#0Fffff00")
        override val catID = 1003
        override val textID = R.string.shortGiro
        override val description = R.string.shortGiro
        override val obercatID = 1003L
    },
    Kreditkarte {

        override val supercatID = Cat.CASHKONTOCATID
        override var color = Color.parseColor("#0Fffff00")
        override val textID = R.string.shortKK
        override var isVerrechnungsKontoNeeded = true
        override val catID = 1004
        override val description = R.string.shortKK
        override val obercatID = 1004L
    },
    Depot {

        override val supercatID = Cat.DEPOTCATID
        override var color = Color.parseColor("#0F0000FF")
        override var isVerrechnungsKontoNeeded = true
        override val catID = 1005
        override val textID = R.string.shortDepo
        override val description = R.string.shortDepo
        override val obercatID = 1005L
    },
    Vermoegen {

        override val supercatID = Cat.CASHKONTOCATID
        override val catID = 1006
        override val textID = R.string.shortVerm
        override val description = R.string.shortVerm
        override val obercatID = 1006L
    },
    Anlagen {

        override val supercatID = Cat.CASHKONTOCATID
        override val catID = 1007
        override val textID = R.string.shortAnlg
        override val description = R.string.shortAnlg
        override val obercatID = 1007L
    },
    Verbindlichkeiten {

        override val supercatID = Cat.CASHKONTOCATID
        override val textID = R.string.shortVerb
        override var isVerrechnungsKontoNeeded = true
        override val catID = 1008
        override val description = R.string.shortVerb
        override val obercatID = 1008L
    },
    Immobilien {

        override val supercatID = Cat.CASHKONTOCATID
        override val catID = 1009
        override val textID = R.string.longImmo
        override val description = R.string.longImmo
        override val obercatID = 1009L
    };

    abstract val catID: Int
    open var color = Color.parseColor("#0Fffff00")
    abstract val description: Int
    abstract val obercatID: Long
    abstract val supercatID: Long
    abstract val textID: Int
    open var isVerrechnungsKontoNeeded = false
    fun getText(context: Context): String {
        return context.getString(textID)
    }

    companion object {

        fun find(obercatid: Long): Kontotyp {
            for (typ in values()) {
                if (typ.obercatID == obercatid) {
                    return typ
                }
            }
            throw IllegalArgumentException("Kein Kontotyp für obercatid $obercatid")
        }
    }
}

@Composable
fun KontotypSpinner(onItemSelected: (Kontotyp) -> Unit) {
    val list = Kontotyp.values().asList()
    val texte = ArrayList<String>().apply {
        list.forEach {
            add(stringResource(id = it.textID))
        }
    }
    val (selectedItem, setSelectedItem)
            = remember { mutableStateOf(Kontotyp.Giro) }
    Spinner(
        text = stringResource(id = selectedItem.textID),
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
fun KontotypSpinnerPreview() {
    AppTheme {
        Surface {
            KontotypSpinner(onItemSelected = {})

        }
    }

}