package com.gerwalex.mymonma.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.gerwalex.mymonma.MonMaViewModel
import com.gerwalex.mymonma.ui.content.AccountListView
import com.gerwalex.mymonma.ui.navigation.TopToolBar

@Composable
fun HomeScreen(viewModel: MonMaViewModel, navigateTo: (Destination) -> Unit) {
    val list by viewModel.accountlist.collectAsState(ArrayList())
    Scaffold(topBar = {
        TopToolBar(Home) {

        }
    })
    {
        Box(modifier = Modifier.padding(it)) {
            LazyColumn {
                items(list,
                    key = { cat -> cat.id!! })
                {
                    AccountListView(it) {
                        viewModel.account.value = it
                        navigateTo(CashTrxList)
                    }

                }
            }
        }
    }
}
