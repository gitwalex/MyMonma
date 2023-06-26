@file:OptIn(ExperimentalMaterial3Api::class)

package com.gerwalex.mymonma.ui.lists

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Card
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.database.room.DB.dao
import com.gerwalex.mymonma.database.tables.Cat
import com.gerwalex.mymonma.database.views.CashTrxView
import com.gerwalex.mymonma.database.views.CashTrxViewItem
import com.gerwalex.mymonma.main.MonMaViewModel
import com.gerwalex.mymonma.ui.content.AmountView
import com.gerwalex.mymonma.ui.content.DateView
import com.gerwalex.mymonma.ui.content.NoEntriesBox
import com.gerwalex.mymonma.ui.navigation.AddCashTrx
import com.gerwalex.mymonma.ui.navigation.Destination
import com.gerwalex.mymonma.ui.navigation.EditCashTrx
import com.gerwalex.mymonma.ui.navigation.TopToolBar
import com.gerwalex.mymonma.ui.navigation.Up
import kotlinx.coroutines.launch


@Composable
fun CashTrxListScreen(viewModel: MonMaViewModel, navigateTo: (Destination) -> Unit) {
    val accountid = rememberSaveable { viewModel.accountid }
    val list by dao.getCashTrxList(accountid).collectAsState(initial = emptyList())
    val account by dao.getAccountData(accountid).collectAsState(initial = Cat())
    if (list.isNotEmpty()) {
        val grouped = list.groupBy { it.btag }
        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()
        val message = stringResource(id = R.string.deleted)
        val undo = stringResource(id = R.string.undo)
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
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

            val lazyListState = rememberLazyListState()
            LazyColumn(
                state = lazyListState,
                modifier = Modifier.padding(it)
            ) {
                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = stringResource(id = R.string.saldo),
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        AmountView(value = account.saldo, fontWeight = FontWeight.Bold)
                    }
                }
                grouped.forEach { (btag, btaglist) ->
                    item { DateView(date = btag) }

                    items(btaglist, key = { item -> item.id!! }) { trx ->

                        LazyListItem(
                            trx = trx,
                            selectedItem = {
                                viewModel.cashTrxId = trx.id!!
                                navigateTo(EditCashTrx)
                            },
                            onDismiss = { dismissValue, item ->
                                when (dismissValue) {
                                    DismissValue.Default -> {}
                                    DismissValue.DismissedToEnd -> TODO()
                                    DismissValue.DismissedToStart -> {
                                        scope.launch {
                                            val trxList = dao.getCashTrx(item.id!!)
                                            dao.deleteCashTrx(item.id!!)
                                            val result = snackbarHostState.showSnackbar(
                                                message = message,
                                                actionLabel = undo,
                                                withDismissAction = true
                                            )
                                            when (result) {
                                                SnackbarResult.Dismissed -> {}
                                                SnackbarResult.ActionPerformed -> {
                                                    scope.launch {
                                                        dao.insertCashTrx(
                                                            CashTrxView.toCashTrxList(
                                                                trxList
                                                            )
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                            })
                    }
                }
            }
        }

    } else {
        NoEntriesBox()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyItemScope.LazyListItem(
    trx: CashTrxView,
    selectedItem: (CashTrxView) -> Unit,
    onDismiss: (DismissValue, CashTrxView) -> Unit
) {
    val currentItem by rememberUpdatedState(newValue = trx)
    val dismissState = rememberDismissState(
        confirmValueChange = { dismissValue ->
            onDismiss(dismissValue, currentItem)
            true
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
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
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