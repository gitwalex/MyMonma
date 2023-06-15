package com.gerwalex.mymonma.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gerwalex.mymonma.MonMaViewModel
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.database.room.DB.dao
import com.gerwalex.mymonma.database.views.CashTrxView
import com.gerwalex.mymonma.ui.Color
import com.gerwalex.mymonma.ui.content.AmountView
import com.gerwalex.mymonma.ui.navigation.AddCashTrx
import com.gerwalex.mymonma.ui.navigation.Destination
import com.gerwalex.mymonma.ui.navigation.EditCashTrx
import com.gerwalex.mymonma.ui.navigation.TopToolBar
import com.gerwalex.mymonma.ui.navigation.Up
import java.text.DateFormat


@Composable
fun CashTrxList(viewModel: MonMaViewModel, navigateTo: (Destination) -> Unit) {
    val accountid = rememberSaveable { viewModel.accountid }
    val list by dao.getCashTrxList(accountid).collectAsState(initial = emptyList())
    val account = dao.getAccountData(accountid)
    Scaffold(
        topBar = {
            TopToolBar(
                account.name,
                actions = {
                    IconButton(onClick = { navigateTo(AddCashTrx) }) {
                        Icon(imageVector = Icons.Default.Add, "")
                    }
                }) {
                navigateTo(Up)
            }
        }) {


        LazyColumn(modifier = Modifier.padding(it)) {
            items(list, key = { item -> item.id!! }) { trx ->
                CashTrxViewItem(trx = trx) { destination ->
                    viewModel.cashTrxId = trx.id!!
                    navigateTo(destination)

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
                    style = MaterialTheme.typography.bodyLarge
                )
                AmountView(
                    value = trx.saldo ?: 0,
                )
            }

        }
    }
}

@Preview
@Composable
fun CashTrxItemPreview() {
    val cashtrans = CashTrxView(
        amount = -123456L,
        memo = "Buchungstext oder VWZ",
    ).apply {
        accountname = "My Account"
        catname = "Kategorie"
        partnername = "CashTrx Partner"
        saldo = 12345678L

    }
    CashTrxViewItem(trx = cashtrans) {}
}


