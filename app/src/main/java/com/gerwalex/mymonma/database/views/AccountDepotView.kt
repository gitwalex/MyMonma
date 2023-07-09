package com.gerwalex.mymonma.database.views

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.room.DatabaseView
import androidx.room.Ignore
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.database.room.DB
import com.gerwalex.mymonma.database.room.MyConverter.NACHKOMMA
import com.gerwalex.mymonma.database.tables.CashTrx
import com.gerwalex.mymonma.database.tables.Cat
import com.gerwalex.mymonma.database.tables.WPTrx
import com.gerwalex.mymonma.enums.Kontotyp
import com.gerwalex.mymonma.enums.WPTrxArt
import com.gerwalex.mymonma.ui.content.AutoCompleteTextView
import java.sql.Date

@DatabaseView(
    """
 select a.*, b.*,c.name as verrechnungskontoname
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
        JOIN cat c on(c.id = b.verrechnungskonto)
        where a.supercatid = 1002
        and a.catclassid != 1    
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
    var verrechnungskontoname: String = "",
) {
    @Ignore
    var wptrx: WPTrx? = null

    suspend fun insertIncome(btag: Date, wp: WPStammView, trxArt: WPTrxArt) {
        wptrx?.let { trx ->
            val trxList = ArrayList<CashTrx>()
            val main = CashTrx(
                accountid = id,
                btag = btag,
                amount = trx.amount,
                catid = trxArt.catid,
                partnerid = wp.id,
            )
            trxList.apply {
                add(main)
                if (trx.abgeltungssteuer != 0L) {
                    add(
                        main.copy(
                            catid = Cat.AbgeltSteuerCatid,
                            amount = trx.abgeltungssteuer
                        )
                    )
                }
                if (trx.ausmachenderBetrag != 0L) {
                    val cashTrx = main.copy(
                        accountid = id,
                        catid = verrechnungskonto ?: id,
                        amount = -(trx.ausmachenderBetrag),
                    ).also {
                        it.gegenbuchung = it.copy(
                            accountid = it.catid,
                            catid = it.accountid,
                            amount = -it.amount
                        )

                    }
                    add(cashTrx)
                }
                DB.dao.insertCashTrx(trxList)

            }
        }
    }

}

@Composable
fun AutoCompleteDepotView(filter: String, selected: (Cat) -> Unit) {
    var accountname by remember { mutableStateOf(filter) }
    var showDropdown by remember { mutableStateOf(true) }
    val data by DB.dao.getAccountlist(accountname).collectAsStateWithLifecycle(emptyList())
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

