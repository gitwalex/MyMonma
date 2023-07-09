package com.gerwalex.mymonma.ui.lists

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gerwalex.mymonma.database.data.WPPaket
import com.gerwalex.mymonma.database.data.WPPaketItem
import com.gerwalex.mymonma.database.room.DB.Companion.wpdao
import com.gerwalex.mymonma.database.views.WPStammView
import com.gerwalex.mymonma.ext.rememberState
import com.gerwalex.mymonma.ui.navigation.Destination
import com.gerwalex.mymonma.ui.navigation.TopToolBar

@Composable
fun WPPaketList(wpid: Long, navigateTo: (Destination) -> Unit) {
    var wpstamm by rememberState { WPStammView() }
    var list by rememberState { listOf<WPPaket>() }
    LaunchedEffect(wpid) {
        wpstamm = wpdao.getWPStamm(wpid)
        list = wpdao.getWPPaketList(wpid)
    }
    Scaffold(
        topBar = {
            TopToolBar(
                title = "${wpstamm.name} (${wpstamm.wpkenn})",
                navigateTo = navigateTo
            )
        }) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(list, key = { it.id }) { item: WPPaket ->
                Card(
                    modifier = Modifier.padding(4.dp),
                    colors = CardDefaults.cardColors(),
                ) {
                    WPPaketItem(paket = item)

                }
            }
        }
    }
}