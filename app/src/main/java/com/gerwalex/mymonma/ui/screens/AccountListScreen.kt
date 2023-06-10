package com.gerwalex.mymonma.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.R
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.gerwalex.mymonma.MonMaViewModel
import com.gerwalex.mymonma.database.tables.Cat
import com.gerwalex.mymonma.ui.AppTheme
import com.gerwalex.mymonma.ui.content.AmountView
import com.gerwalex.mymonma.ui.navigation.Destination
import com.gerwalex.mymonma.ui.navigation.Home
import com.gerwalex.mymonma.ui.navigation.TopToolBar

@Composable
fun AccountListScreen(viewModel: MonMaViewModel, navigateTo: (Destination) -> Unit) {
    val list by viewModel.accountlist.collectAsState(ArrayList())
    Scaffold(
        topBar = {
            TopToolBar(
                stringResource(id = Home.title),
                imageVector = Icons.Default.Menu,
                description = R.string.navigation_menu
            ) {

            }
        })
    {
        Box(modifier = Modifier.padding(it)) {
            LazyColumn {
                items(list,
                    key = { cat -> cat.id!! })
                {
                    AccountListItem(it) {
                        viewModel.account = it
                        navigateTo(com.gerwalex.mymonma.ui.navigation.CashTrxList)
                    }

                }
            }
        }
    }
}

@Composable
fun AccountListItem(cat: Cat, clicked: (Cat) -> Unit) {
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
    val cat = Cat(name = "my Account")
    AppTheme {
        Surface {
            AccountListItem(cat) {}
        }
    }
}