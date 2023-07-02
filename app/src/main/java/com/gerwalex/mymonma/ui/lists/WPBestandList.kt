package com.gerwalex.mymonma.ui.lists

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gerwalex.mymonma.database.room.DB.wpdao
import com.gerwalex.mymonma.database.views.WPStammItem
import com.gerwalex.mymonma.database.views.WPStammView
import com.gerwalex.mymonma.enums.WPTrxArt
import com.gerwalex.mymonma.main.MonMaViewModel
import com.gerwalex.mymonma.ui.content.NoEntriesBox
import com.gerwalex.mymonma.ui.navigation.Destination
import com.gerwalex.mymonma.ui.navigation.Einnahmen
import com.gerwalex.mymonma.ui.navigation.TopToolBar
import com.gerwalex.mymonma.ui.navigation.Up
import java.sql.Date


@Composable
fun WPBestandList(viewModel: MonMaViewModel, navigateTo: (Destination) -> Unit) {
    val list by wpdao.getWPBestandListe(Date(System.currentTimeMillis()))
        .collectAsState(initial = emptyList())
    if (list.isNotEmpty()) {
        WPBestandList(list = list, navigateTo = navigateTo) { wp, trxArt ->
            when (trxArt) {
                WPTrxArt.Income -> {
                    navigateTo(Einnahmen.apply {
                        wpid = wp.id
                    })
                }

                else -> {}
            }

        }
    } else {
        NoEntriesBox()
    }

}

@Composable
fun WPBestandList(
    list: List<WPStammView>,
    navigateTo: (Destination) -> Unit,
    action: (wp: WPStammView, trx: WPTrxArt) -> Unit
) {

    Scaffold(topBar = {
        TopToolBar(title = "WPBestandList", navigateTo = {
            navigateTo(Up)
        })
    }) {
        LazyVerticalGrid(
            modifier = Modifier.padding(it),
            columns = GridCells.Adaptive(minSize = 250.dp)
        ) {
            items(list, key = { item -> item.id }) { item ->
                WPStammItem(item = item) { wptrx ->
                    action(item, wptrx)

                }
            }
        }

    }

}
