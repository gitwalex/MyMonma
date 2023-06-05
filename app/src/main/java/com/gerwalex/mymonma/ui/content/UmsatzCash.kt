package com.gerwalex.mymonma.ui.content

import android.database.Cursor
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.database.views.CashTrxView
import com.gerwalex.mymonma.ui.Color
import java.math.BigDecimal
import java.text.DateFormat

@Composable
fun UmsatzCashList(c: Cursor, modifier: Modifier = Modifier) {
    val idIndex = remember { c.getColumnIndex("_id") }
    LazyColumn(modifier) {
        items(count = c.count,
            key = { position ->
                if (c.moveToPosition(position)) c.getLong(idIndex)
            }
        ) { position ->
            if (c.moveToPosition(position)) {
                UmsatzCash(cashTrans = CashTrxView(c))
            }
        }
    }


}


@Composable
fun UmsatzCash(cashTrans: CashTrxView) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .wrapContentHeight()
                    .background(
                        if (cashTrans.imported) Color.importedCashTrx else Color.cashTrx
                    )
            ) {
                Text(
                    text = DateFormat.getDateInstance().format(cashTrans.btag),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    modifier = Modifier.weight(1f),
                    text = cashTrans.partner,
                    maxLines = 1,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center

                )
                AmountView(value = cashTrans.amount, fontWeight = FontWeight.Bold)
            }
            Text(text = cashTrans.memo ?: "")
            Text(text = cashTrans.cat)
            Divider(modifier = Modifier.padding(2.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = stringResource(id = R.string.saldo),
                    style = MaterialTheme.typography.labelSmall
                )
                AmountView(
                    value = cashTrans.saldo,
                    style = MaterialTheme.typography.labelSmall
                )
            }

        }
    }
}

@Preview
@Composable
fun UmsatzCashPreview() {
    val cashtrans = CashTrxView(
        account = "My Account",
        amount = BigDecimal(-1234.56),
        cat = "Kategorie",
        partner = "CashTrx Partner",
        memo = "Buchungstext oder VWZ",
        saldo = BigDecimal(123456.78)
    )
    UmsatzCash(cashTrans = cashtrans)
}