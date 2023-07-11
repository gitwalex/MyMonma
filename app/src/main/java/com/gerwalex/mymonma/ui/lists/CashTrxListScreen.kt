@file:OptIn(ExperimentalMaterial3Api::class)

package com.gerwalex.mymonma.ui.lists

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.database.room.DB.Companion.dao
import com.gerwalex.mymonma.database.tables.Cat
import com.gerwalex.mymonma.database.views.CashTrxView
import com.gerwalex.mymonma.database.views.CashTrxViewItem
import com.gerwalex.mymonma.ext.rememberState
import com.gerwalex.mymonma.main.MonMaViewModel
import com.gerwalex.mymonma.ui.content.AmountView
import com.gerwalex.mymonma.ui.content.DateView
import com.gerwalex.mymonma.ui.content.NoEntriesBox
import com.gerwalex.mymonma.ui.navigation.AddCashTrxDest
import com.gerwalex.mymonma.ui.navigation.Destination
import com.gerwalex.mymonma.ui.navigation.EditCashTrxDest
import com.gerwalex.mymonma.ui.navigation.TopToolBar
import com.gerwalex.mymonma.ui.navigation.Up
import kotlinx.coroutines.launch
import java.sql.Date


@Composable
fun CashTrxListScreen(
    accountid: Long,
    viewModel: MonMaViewModel,
    navigateTo: (Destination) -> Unit
) {
    val scope = rememberCoroutineScope()
    val message = stringResource(id = R.string.deleted)
    val undo = stringResource(id = R.string.undo)

    val list by dao.getCashTrxList(accountid).collectAsStateWithLifecycle(emptyList())
    val account by dao.getAccountData(accountid).collectAsStateWithLifecycle(Cat())
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopToolBar(
                account.name,
                actions = {
                    IconButton(
                        onClick = { navigateTo(AddCashTrxDest.apply { id = accountid }) },
                        modifier = Modifier.scale(1.5f)
                    ) {
                        Icon(imageVector = Icons.Default.Add, "")
                    }
                }) {
                navigateTo(Up)
            }
        }) {

        val lazyListState = rememberLazyListState()
        Box {
            LazyColumn(
                state = lazyListState,
                modifier = Modifier.padding(it)
            ) {
                list.groupBy { it.btag }
                    .forEach { (btag, btaglist) ->
                        item {
                            SaldoView(accountid = accountid, btag = btag)
                        }
                        items(btaglist, key = { item -> item.id!! }) { trx ->
                            CashTrxItem(
                                trx = trx,
                                selectedItem = {
                                    navigateTo(EditCashTrxDest.apply { id = trx.id!! })
                                },
                                onDismissed = {
                                    scope.launch {
                                        val trxList = dao.getCashTrx(trx.id!!)
                                        dao.deleteCashTrx(trx.id!!)
                                        val result = snackbarHostState.showSnackbar(
                                            message = message,
                                            actionLabel = undo,
                                            withDismissAction = true
                                        )
                                        when (result) {
                                            SnackbarResult.Dismissed -> {}
                                            SnackbarResult.ActionPerformed -> {
                                                scope.launch {
                                                    CashTrxView.insert(trxList)
                                                }
                                            }
                                        }
                                    }

                                }
                            )
                        }
                    }
            }
        }
        if (list.isEmpty()) {
            NoEntriesBox()
        }
    }

}

@Composable
fun SaldoView(accountid: Long, btag: Date) {
    var saldo by rememberState(key = btag) { 0L }
    LaunchedEffect(key1 = btag) {
        saldo = dao.getSaldo(accountid, btag)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        DateView(
            date = btag, fontWeight = FontWeight.Bold
        )
        AmountView(
            value = saldo, fontWeight = FontWeight.Bold
        )
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyItemScope.CashTrxItem(
    trx: CashTrxView,
    selectedItem: (CashTrxView) -> Unit,
    onDismissed: (CashTrxView) -> Unit
) {
    val dismissState = rememberDismissState(
        confirmValueChange = { dismissValue ->
            when (dismissValue) {
                DismissValue.Default,
                DismissValue.DismissedToEnd -> false

                DismissValue.DismissedToStart -> {
                    onDismissed(trx)
                    true
                }
            }
        }
    )
    SwipeToDismiss(
        modifier = Modifier
            .padding(vertical = 1.dp)
            .animateItemPlacement(),
        state = dismissState,
        background = {
            SwipeBackground(dismissState = dismissState)
        },
        dismissContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .border(
                        BorderStroke(1.dp, color = MaterialTheme.colorScheme.surfaceTint),
                        shape = RectangleShape
                    )
                    .clickable {
                        selectedItem(trx)
                    }
            ) {
                CashTrxViewItem(trx = trx)
            }
        })

}

@Composable
fun SwipeBackground(dismissState: DismissState) {
    val direction = dismissState.dismissDirection ?: return

    val color by animateColorAsState(
        when (dismissState.targetValue) {
            DismissValue.Default -> Color.LightGray
            DismissValue.DismissedToEnd -> Color.Green
            DismissValue.DismissedToStart -> Color.Red
        }
    )
    val alignment = when (direction) {
        DismissDirection.StartToEnd -> Alignment.CenterStart
        DismissDirection.EndToStart -> Alignment.CenterEnd
    }
    val icon = when (direction) {
        DismissDirection.StartToEnd -> Icons.Default.Done
        DismissDirection.EndToStart -> Icons.Default.Delete
    }
    val scale by animateFloatAsState(
        if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
    )

    Box(
        Modifier
            .fillMaxSize()
            .background(color)
            .padding(horizontal = 20.dp),
        contentAlignment = alignment
    ) {
        Icon(
            icon,
            contentDescription = "Localized description",
            modifier = Modifier.scale(scale)
        )
    }
}