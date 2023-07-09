package com.gerwalex.mymonma.database.views

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.DatabaseView
import androidx.room.Ignore
import com.gerwalex.mymonma.database.room.DB.Companion.dao
import com.gerwalex.mymonma.database.tables.CashTrx
import com.gerwalex.mymonma.database.tables.Cat.Companion.KONTOCLASS
import com.gerwalex.mymonma.ui.AppTheme
import com.gerwalex.mymonma.ui.LocalAppColors
import com.gerwalex.mymonma.ui.content.AmountView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Date
import java.text.DateFormat

@DatabaseView(
    """ 
        select a.*
        ,(select name from Partnerstamm  p where p.id = a.partnerid) as partnername
        ,(select name from Cat c where c.id = a.accountid) as accountname 
		,c.name as catname, c.catclassid
		,t.id as importTrxId
        from CashTrx a 
        left join Cat c on c.id = catid
        left outer join ImportTrx t on a.id = t.umsatzid 
"""
)
data class CashTrxView(
    var id: Long? = null,
    var btag: Date = Date(System.currentTimeMillis()),
    var accountid: Long = 0,
    var catid: Long = -1,
    var partnerid: Long = 0,
    var amount: Long = 0,
    var memo: String? = null,
    var transferid: Long? = null,
    var accountname: String = "",
    var partnername: String = "",
    var catname: String = "",
    var catclassid: Long = -1,
    var isUmbuchung: Boolean = false,
    var importTrxId: Long? = null,
    @Ignore
    var gegenbuchung: CashTrxView? = null
) {
    val cashTrx: CashTrx
        get() {
            return CashTrx(
                id = id,
                btag = btag,
                accountid = accountid,
                catid = catid,
                partnerid = partnerid,
                amount = amount,
                memo = memo,
                transferid = transferid,
                isUmbuchung = isUmbuchung,
                partnername = partnername.ifEmpty { null },
                gegenbuchung = gegenbuchung?.cashTrx,
            )
        }


    companion object {

        /**
         * Change a CashTrxView-list to CashTrx-list and insert it
         */
        suspend fun insert(list: List<CashTrxView>): ArrayList<CashTrx> {
            return withContext(Dispatchers.IO) {
                ArrayList<CashTrx>().apply {
                    list.forEach { trx ->
                        if (trx.catclassid == KONTOCLASS) {
                            // Umbuchung!!
                            trx.gegenbuchung = trx.copy(
                                id = null,
                                accountid = trx.catid,
                                catid = trx.accountid,
                                amount = -trx.amount,
                                transferid = null,
                            )
                        } else {
                            trx.gegenbuchung = null
                        }
                        add(trx.cashTrx)
                    }
                    dao.insertCashTrx(this)
                }
            }
        }
    }
}

@Composable
fun CashTrxViewItem(trx: CashTrxView, modifier: Modifier = Modifier) {
    Column(modifier) {
        Row(
            modifier = Modifier
                .wrapContentHeight()
                .background(
                    trx.importTrxId?.let {
                        LocalAppColors.current.importedCashTrx
                    } ?: LocalAppColors.current.cashTrx
                )
        ) {
            Text(
                text = DateFormat.getDateInstance().format(trx.btag),
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier.weight(1f),
                text = trx.partnername,
                maxLines = 1,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center

            )
            AmountView(value = trx.amount, fontWeight = FontWeight.Bold)
        }
        trx.memo?.let {
            Text(text = it)
        }
        Text(text = if (trx.catclassid == KONTOCLASS) "[${trx.catname}]" else trx.catname)

    }

}

@Preview(name = "Light", uiMode = UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun CashTrxItemPreview() {
    AppTheme {
        Surface {
            val cashtrans = CashTrxView(
                id = 0,
                amount = -123456L,
                memo = "Buchungstext oder VWZ",
            ).apply {
                accountname = "My Account"
                catname = "Kategorie"
                partnername = "CashTrx Partner"

            }
            CashTrxViewItem(trx = cashtrans)

        }
    }
}

