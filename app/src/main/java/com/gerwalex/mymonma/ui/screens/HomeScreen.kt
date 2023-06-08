package com.gerwalex.mymonma.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.gerwalex.mymonma.MonMaViewModel
import com.gerwalex.mymonma.database.tables.Account
import com.gerwalex.mymonma.ui.content.AccountView
import com.gerwalex.mymonma.ui.navigation.Destination
import com.gerwalex.mymonma.ui.navigation.Home
import com.gerwalex.mymonma.ui.navigation.TopToolBar

@Composable
fun HomeScreen(viewModel: MonMaViewModel, navigateTo: (Destination) -> Unit) {
    val cursor by viewModel.accountlist.observeAsState()
    cursor?.let { c ->
        Scaffold(topBar = {
            TopToolBar(Home) {

            }
        })
        {
            Box(modifier = Modifier.padding(it)) {
                LazyColumn {
                    items(
                        count = c.count
                    ) { position ->
                        if (c.moveToPosition(position)) {
                            AccountView(Account(c))
                        }

                    }

                }

            }
        }
    }
}
