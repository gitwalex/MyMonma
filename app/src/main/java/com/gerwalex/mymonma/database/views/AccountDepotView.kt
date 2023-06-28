package com.gerwalex.mymonma.database.views

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.room.DatabaseView
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.database.room.DB
import com.gerwalex.mymonma.database.room.MyConverter.NACHKOMMA
import com.gerwalex.mymonma.database.tables.Cat
import com.gerwalex.mymonma.enums.Kontotyp
import com.gerwalex.mymonma.ui.content.AutoCompleteTextView
import java.sql.Date

@DatabaseView(
    """
        select *
		,(select total(marktwert) from (
            select accountid,  sum(menge) / $NACHKOMMA *
            (SELECT kurs from WPKurs d where a.wpid = d.wpid
            group by wpid having max(btag)) as marktwert
            from WPTrx a
            where paketid is null 
            group by accountid, wpid)
		    where accountid = a.id
            group by accountid) as marktwert
            from cat a
            join Account b using(id)
            where supercatid = 1002
            and catclassid != 1    
            """
)
data class AccountDepotView(
    var id: Long = -1,
    var name: String = "",
    var description: String? = null,
    var marktwert: Long = 0,
    var obercatid: Long = 0,
    var supercatid: Long = 0,
    var catclassid: Long = 0,
    var incomecat: Boolean? = null,
    var ausgeblendet: Boolean = false,
    var inhaber: String? = null,
    var currency: String? = null,
    var iban: String? = null,// IBAN, Kartnenummer, Kontonummer
    var blz: String? = null,
    var bezeichnung: String? = null, //Verwendungszweck
    var creditlimit: Long? = 0,
    var verrechnungskonto: Long? = null,
    var kontotyp: Kontotyp = Kontotyp.Giro,
    var openDate: Date? = Date(System.currentTimeMillis()),
    var bankname: String? = null,
    var bic: String? = null,
)

@Composable
fun AutoCompleteDepotView(filter: String, selected: (Cat) -> Unit) {
    var accountname by remember { mutableStateOf(filter) }
    var showDropdown by remember { mutableStateOf(true) }
    val data by DB.dao.getAccountlist(accountname).collectAsState(emptyList())
    var error by remember { mutableStateOf(false) }
    if (data.isEmpty()) {
        error = true
        selected(Cat())
    } else {
        selected(data[0])
        error = false
    }



    AutoCompleteTextView(
        modifier = Modifier.fillMaxWidth(),
        query = accountname,
        error = if (error) stringResource(id = R.string.errorListEmpty) else "",
        queryLabel = stringResource(id = R.string.categorie),
        onQueryChanged = {
            accountname = it
            showDropdown = true
        },
        list = data,
        onClearClick = { accountname = "" },
        onDismissRequest = { },
        onItemClick = { acc ->
            accountname = acc.name
            showDropdown = false
            selected(acc)

        },
        onFocusChanged = { isFocused ->
            showDropdown = isFocused
            if (!isFocused) {
                if (data.isNotEmpty()) {
                    accountname = data[0].name
                    selected(data[0])
                } else {
                    error = true
                    selected(Cat())
                }
            }
        }
    ) { acc ->
        Text(acc.name, fontSize = 14.sp)
    }
}

