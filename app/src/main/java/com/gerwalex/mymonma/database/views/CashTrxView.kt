package com.gerwalex.mymonma.database.views

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.room.DatabaseView
import androidx.room.Ignore
import com.gerwalex.mymonma.database.tables.CashTrx
import com.gerwalex.mymonma.database.tables.Cat
import com.gerwalex.mymonma.ui.AppTheme
import com.gerwalex.mymonma.ui.Color
import com.gerwalex.mymonma.ui.content.AmountView
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
                partnername = partnername,
                cashTrx = gegenbuchung?.cashTrx,
            )
        }

    fun createGegenbuchung(): CashTrxView {
        return CashTrxView(
            id = null,
            btag = btag,
            accountid = catid,
            catid = accountid,
            partnerid = partnerid,
            amount = -amount,
            memo = memo,
            partnername = partnername,
            gegenbuchung = null,
        )
    }

    companion object {
        fun toCashTrxList(list: List<CashTrxView>): ArrayList<CashTrx> {
            return ArrayList<CashTrx>().apply {
                list.forEach { trx ->
                    if (trx.catclassid == Cat.KONTOCLASS) {
                        // Umbuchung!!
                        trx.gegenbuchung = trx.createGegenbuchung()
                    } else {
                        trx.gegenbuchung = null
                    }
                    add(trx.cashTrx)
                }
            }
        }
    }
}

@Composable
fun CashTrxViewItem(trx: CashTrxView) {
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .wrapContentHeight()
                .background(
                    trx.importTrxId?.let {
                        Color.importedCashTrx
                    } ?: Color.cashTrx
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
        Text(text = trx.memo ?: "")
        Text(text = trx.catname)

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

