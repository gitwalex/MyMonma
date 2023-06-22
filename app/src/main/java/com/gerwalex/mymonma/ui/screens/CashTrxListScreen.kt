package com.gerwalex.mymonma.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.database.room.DB.dao
import com.gerwalex.mymonma.database.tables.Cat
import com.gerwalex.mymonma.database.views.CashTrxViewItem
import com.gerwalex.mymonma.main.MonMaViewModel
import com.gerwalex.mymonma.ui.content.AmountView
import com.gerwalex.mymonma.ui.content.NoEntriesBox
import com.gerwalex.mymonma.ui.navigation.AddCashTrx
import com.gerwalex.mymonma.ui.navigation.Destination
import com.gerwalex.mymonma.ui.navigation.EditCashTrx
import com.gerwalex.mymonma.ui.navigation.TopToolBar
import com.gerwalex.mymonma.ui.navigation.Up


@Composable
fun CashTrxListScreen(viewModel: MonMaViewModel, navigateTo: (Destination) -> Unit) {
    val accountid = rememberSaveable { viewModel.accountid }
    val list by dao.getCashTrxList(accountid).collectAsState(initial = emptyList())
    val account by dao.getAccountData(accountid).collectAsState(initial = Cat())
    if (list.isNotEmpty()) {
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

            Column(modifier = Modifier.padding(it)) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = stringResource(id = R.string.saldo), fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.weight(1f))
                    AmountView(value = account.saldo, fontWeight = FontWeight.Bold)
                }
                LazyColumn {
                    items(list, key = { item -> item.id!! }) { trx ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clickable {
                                    viewModel.cashTrxId = trx.id!!
                                    navigateTo(EditCashTrx)
                                }
                        ) {
                            CashTrxViewItem(trx = trx)
                        }
                    }
                }
            }
        }

    } else {
        NoEntriesBox()
    }
}




