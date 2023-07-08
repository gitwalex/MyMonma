package com.gerwalex.mymonma.database.views

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Parcelable
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
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
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.room.DatabaseView
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.database.room.DB
import com.gerwalex.mymonma.database.room.MyConverter.NACHKOMMA
import com.gerwalex.mymonma.database.tables.Partnerstamm
import com.gerwalex.mymonma.enums.WPTrxArt
import com.gerwalex.mymonma.enums.WPTrxArtMenu
import com.gerwalex.mymonma.enums.WPTyp
import com.gerwalex.mymonma.ui.AppTheme
import com.gerwalex.mymonma.ui.content.AmountView
import com.gerwalex.mymonma.ui.content.AutoCompleteTextView
import com.gerwalex.mymonma.ui.content.MengeView
import com.gerwalex.mymonma.ui.views.KursLineChart
import kotlinx.android.parcel.Parcelize
import java.sql.Date

@DatabaseView(
    """
        select a.*, name, total(menge) as bestand, total(einstand) as einstand  
        ,(select total(einstand) from wptrx where wpid = a.id  
        and catid between 2001 and 2049) as gesamteinkaufpreis 
        ,(select count(einstand) from wptrx where wpid = a.id 
        and catid between 2001 and 2049) as anzahlkauf 
        ,(select total(amount) from CashTrx c where a.id = c.partnerid  
        and catid between 2101 and 2199) as income 
        ,(select min(btag) from wptrx where wpid = a.id) as firstums 
        ,(select max(btag) from wptrx where wpid = a.id) as lastums
        ,(select total(amount) from CashTrx  c where a.id = c.partnerid   
        and catid between 2001 and 2049) as gesamtkauf  
        ,(select total(amount) from CashTrx c where a.id = c.partnerid 
        and catid in(2201)) as kursverlust 
        ,(select total(amount) from CashTrx c where a.id = c.partnerid 
        and catid in(2200)) as kursgewinn   
        ,(SELECT kurs from WPKurs d where a.id = d.wpid  
        group by wpid having max(btag)) as lastkurs    
        ,(SELECT btag from WPKurs d  where a.id = d.wpid  
        group by wpid having max(btag)) as lastbtag 
        from WPStamm a    
        join Partnerstamm p on (p.id = a.partnerid)   
        left outer join WPTrx b on (b.wpid = a.id) 
        where paketid is null    
        group by accountid, wpid having bestand > 0   
        order by name
"""
)
@Parcelize
data class WPStammView(
    val id: Long,
    val name: String = "",
    val partnerid: Long = 0,
    val wpkenn: String = "",
    val isin: String? = "",
    val wptyp: WPTyp = WPTyp.Aktie,
    val risiko: Int = 2,
    val beobachten: Boolean = true,
    val estEarning: Long = 0,
    val bestand: Long = 0,
    val einstand: Long = 0,
    val gesamteinkaufpreis: Long = 0,
    val anzahlkauf: Long = 0,
    val firstums: Date = Date(System.currentTimeMillis()),
    val lastums: Date = Date(System.currentTimeMillis()),
    val gesamtkauf: Long = 0,
    val kursverlust: Long = 0,
    val kursgewinn: Long = 0,
    val income: Long = 0,
    val lastkurs: Long = 0,
    val lastbtag: Date? = Date(System.currentTimeMillis()),
) : Parcelable

@Composable
fun AutoCompleteWPStammView(filter: String, selected: (Partnerstamm) -> Unit) {
    var wpstammname by remember { mutableStateOf(filter) }
    var showDropdown by remember { mutableStateOf(true) }
    val data by DB.dao.getWPStammlist(wpstammname).collectAsStateWithLifecycle( emptyList())


    AutoCompleteTextView(
        modifier = Modifier.fillMaxWidth(),
        query = wpstammname,
        queryLabel = stringResource(id = R.string.wpstamm),
        onQueryChanged = {
            wpstammname = it
        },
        list = data,
        onDismissRequest = { },
        onItemClick = { wpstamm ->
            wpstammname = wpstamm.name
            selected(wpstamm)
            showDropdown = false
        },
        onFocusChanged = { hasFocus ->
            showDropdown = hasFocus
        }
    ) { wpstamm ->
        Text(wpstamm.name, fontSize = 14.sp)
    }
}

@Composable
fun WPStammItem(item: WPStammView, action: (WPTrxArt) -> Unit) {
    Card(modifier = Modifier.padding(4.dp)) {
        Column(
            modifier = Modifier
                .padding(8.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.tertiary),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.name,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.onTertiary
                )
                WPTrxArtMenu(selected = { trxArt ->
                    action(trxArt)
                })
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(id = R.string.marktwert), fontWeight = FontWeight.Bold)
                AmountView(value = item.bestand / NACHKOMMA * item.lastkurs)
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(id = R.string.einstand))
                AmountView(value = item.einstand)
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(id = R.string.buchgewinn))
                AmountView(value = (item.bestand / NACHKOMMA * item.lastkurs) - item.einstand)
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(id = R.string.einnahmen))
                AmountView(value = (item.income))
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(id = R.string.kurs))
                AmountView(value = item.lastkurs)
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(id = R.string.bestand))
                MengeView(value = item.bestand)
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(2.dp)
                    .border(1.dp, MaterialTheme.colorScheme.onSurface)
            ) {
                if (!LocalInspectionMode.current)
                    KursLineChart(wpStamm = item)

            }

        }

    }

}

@Preview(name = "Light", uiMode = UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = UI_MODE_NIGHT_YES)
@Preview(
    name = "Small Width",
    uiMode = UI_MODE_NIGHT_NO,
    device = "spec:height=1280dp,width=250dp,dpi=480"
)

@Composable
fun WPStammItemPreview() {
    val item = WPStammView(
        id = 0,
        name = "WPName",
        bestand = 10000 * NACHKOMMA,
        lastkurs = 12346,
        einstand = 987654

    )
    AppTheme {
        Surface {
            WPStammItem(item) {}

        }

    }
}