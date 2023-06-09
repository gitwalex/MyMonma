package com.gerwalex.mymonma.ui.lists

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.database.data.GesamtVermoegen
import com.gerwalex.mymonma.database.views.AccountCashView
import com.gerwalex.mymonma.database.views.AccountDepotView
import com.gerwalex.mymonma.ext.rememberState
import com.gerwalex.mymonma.main.MonMaViewModel
import com.gerwalex.mymonma.ui.AppTheme
import com.gerwalex.mymonma.ui.content.AmountView
import com.gerwalex.mymonma.ui.content.NoEntriesBox
import com.gerwalex.mymonma.ui.navigation.AccountListDest
import com.gerwalex.mymonma.ui.navigation.CashTrxListDest
import com.gerwalex.mymonma.ui.navigation.Destination
import com.gerwalex.mymonma.ui.navigation.TopToolBar
import com.gerwalex.mymonma.ui.navigation.Up

@Composable
fun AccountListScreen(viewModel: MonMaViewModel, navigateTo: (Destination) -> Unit) {
    Scaffold(
        topBar = {
            TopToolBar(title = AccountListDest.name) {
                navigateTo(Up)
            }
        }
    )
    {
        Box(modifier = Modifier.padding(it)) {
            val gesamtvermoegen by viewModel.gesamtvermoegen.collectAsStateWithLifecycle( GesamtVermoegen())
            val list by viewModel.accountlist.collectAsStateWithLifecycle(ArrayList())
            val depots by viewModel.depotlist.collectAsStateWithLifecycle(ArrayList())
            Column(Modifier.padding(4.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(id = R.string.vermoegen),
                        fontWeight = FontWeight.Bold
                    )
                    AmountView(
                        value = gesamtvermoegen.summe,
                        fontWeight = FontWeight.Bold,
                    )
                }
                if (list.isEmpty() && depots.isEmpty()) {
                    NoEntriesBox()
                }


                LazyColumn(contentPadding = PaddingValues(4.dp)) {
                    list.filter { !it.ausgeblendet }
                        .groupBy { it.obercatid }
                        .forEach { (_, accList) ->
                            // saldo ermitteln
                            var saldo = 0L
                            accList.map {
                                saldo += it.saldo
                            }
                            item {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(MaterialTheme.colorScheme.primaryContainer),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = accList[0].obercatname,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                    AmountView(
                                        value = saldo,
                                    )
                                }
                            }
                            items(accList,
                                key = { account -> account.id })
                            { accountid ->
                                AccountCashListItem(accountid) {
                                    navigateTo(CashTrxListDest.apply { id = accountid.id })
                                }
                            }
                        }
                    val acclist = depots.filter { !it.ausgeblendet }
                    if (acclist.isNotEmpty()) {
                        item {
                            var marktwert by rememberState(acclist) { 0L }
                            LaunchedEffect(key1 = depots) {
                                var saldo = 0L
                                acclist.forEach { saldo += it.marktwert }
                                marktwert = saldo
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.primaryContainer),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = stringResource(id = R.string.myDepots),
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                AmountView(
                                    value = marktwert,
                                )
                            }
                        }
                    }
                    items(acclist,
                        key = { account -> account.id })
                    { accountid ->
                        AccountDepotListItem(accountid) {
                            navigateTo(CashTrxListDest.apply { id = accountid.id })
                        }
                    }
                }

            }

        }
    }
}

@Composable
fun AccountCashListItem(cat: AccountCashView, clicked: (AccountCashView) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                clicked(cat)
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = cat.name)
        AmountView(
            value = cat.saldo,
            modifier = Modifier.weight(1f)
        )

    }

}

@Composable
fun AccountDepotListItem(cat: AccountDepotView, clicked: (AccountDepotView) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                clicked(cat)
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = cat.name)
        AmountView(
            value = cat.marktwert,
            modifier = Modifier.weight(1f)
        )

    }

}

@Preview
@Composable
fun AccountListPrevView() {
    val account = AccountCashView(name = "my Account")
    AppTheme {
        Surface {
            AccountCashListItem(account) {}
        }
    }
}