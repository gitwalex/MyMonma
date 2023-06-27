package com.gerwalex.mymonma.ui.lists

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.gerwalex.mymonma.database.views.AccountView
import com.gerwalex.mymonma.main.MonMaViewModel
import com.gerwalex.mymonma.ui.AppTheme
import com.gerwalex.mymonma.ui.content.AmountView
import com.gerwalex.mymonma.ui.content.NoEntriesBox
import com.gerwalex.mymonma.ui.navigation.CashTrxList
import com.gerwalex.mymonma.ui.navigation.Destination

@Composable
fun AccountListScreen(viewModel: MonMaViewModel, navigateTo: (Destination) -> Unit) {
    val list by viewModel.accountlist.collectAsState(ArrayList())
    Scaffold(
//        topBar = {
//            TopToolBar(title = AccountList.name) {
//                navigateTo(Up)
//            }
//        }
    )
    {
        Box(modifier = Modifier.padding(it)) {
            if (list.isEmpty()) {
                NoEntriesBox()
            }
            LazyColumn {
                list.filter { !it.ausgeblendet }
                    .groupBy { it.obercatid }
                    .forEach { (ober, accList) ->
                        // saldo ermitteln
                        var saldo = 0L
                        accList.forEach {
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
                                    text = ober.toString(),
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
                            AccountListItem(accountid) {
                                navigateTo(CashTrxList.apply { id = accountid.id })
                            }
                        }
                    }
            }
        }
    }
}

@Composable
fun AccountListItem(cat: AccountView, clicked: (AccountView) -> Unit) {
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

@Preview
@Composable
fun AccountListPrevView() {
    val account = AccountView(name = "my Account")
    AppTheme {
        Surface {
            AccountListItem(account) {}
        }
    }
}