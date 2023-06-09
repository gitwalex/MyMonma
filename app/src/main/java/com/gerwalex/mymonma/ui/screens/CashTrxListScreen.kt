@file:OptIn(ExperimentalMaterial3Api::class)

package com.gerwalex.mymonma.ui.screens

import android.database.Cursor
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gerwalex.mymonma.MonMaViewModel
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.database.ObservableTableRowNew
import com.gerwalex.mymonma.database.room.DB
import com.gerwalex.mymonma.ui.Color
import com.gerwalex.mymonma.ui.content.AmountView
import com.gerwalex.mymonma.ui.navigation.Destination
import com.gerwalex.mymonma.ui.navigation.EditCashTrx
import com.gerwalex.mymonma.ui.navigation.TopToolBar
import com.gerwalex.mymonma.ui.navigation.Up
import java.sql.Date
import java.text.DateFormat

data class CashTrxView(
    var id: Long? = null,
    var btag: Date = Date(System.currentTimeMillis()),
    var accountname: String = "Unknown",
    var catname: String = "Unknown",
    var partnername: String = "Unknown",
    var amount: Long = 0,
    var memo: String? = null,
    var transferid: Long? = null,
    var imported: Boolean = false,
    var saldo: Long = 0,
) : ObservableTableRowNew() {
    constructor(c: Cursor) : this(null) {
        fillContent(c)
        id = getAsLongOrNull("_id")
        btag = getAsDate("btag")!!
        accountname = getAsString("accountname")!!
        partnername = getAsString("partnername")!!
        catname = getAsString("catname")!!
        amount = getAsLong("amount")
        memo = getAsString("memo")
        transferid = getAsLongOrNull("transferid")
        imported = getAsBooleanOrNull("imported") ?: false
        saldo = getAsLongOrNull("saldo") ?: 0
    }
}

@Composable
fun CashTrxList(viewModel: MonMaViewModel, navigateTo: (Destination) -> Unit) {
    val account by viewModel.account.observeAsState()
    account?.let { acc ->
        val list by DB.dao.getCashTrxList(acc.id!!).collectAsState(initial = ArrayList())
        Scaffold(
            topBar = {
                TopToolBar(acc.name) {
                    navigateTo(Up)
                }
            }) {


            LazyColumn(modifier = Modifier.padding(it)) {
                items(list, key = { it.id!! }) { trx ->
                    CashTrxViewItem(trx = trx) {
                        viewModel.cashTrx.value = trx
                        navigateTo(it)

                    }
                }
            }

        }

    }
}

@Composable
fun CashTrxViewItem(trx: CashTrxView, navigateTo: (Destination) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { navigateTo(EditCashTrx) }
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
                        if (trx.imported) Color.importedCashTrx else Color.cashTrx
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
            Divider(modifier = Modifier.padding(2.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = stringResource(id = R.string.saldo),
                    style = MaterialTheme.typography.labelSmall
                )
                AmountView(
                    value = trx.saldo,
                    style = MaterialTheme.typography.labelSmall
                )
            }

        }
    }
}

@Preview
@Composable
fun CashTrxItemPreview() {
    val cashtrans = CashTrxView(
        accountname = "My Account",
        amount = -123456L,
        catname = "Kategorie",
        partnername = "CashTrx Partner",
        memo = "Buchungstext oder VWZ",
        saldo = 12345678L
    )
    CashTrxViewItem(trx = cashtrans) {}
}


