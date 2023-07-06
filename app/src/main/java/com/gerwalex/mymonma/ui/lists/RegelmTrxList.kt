package com.gerwalex.mymonma.ui.lists

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Card
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.database.room.DB.dao
import com.gerwalex.mymonma.database.views.TrxRegelmView
import com.gerwalex.mymonma.main.MonMaViewModel
import com.gerwalex.mymonma.ui.AppTheme
import com.gerwalex.mymonma.ui.content.AmountView
import com.gerwalex.mymonma.ui.content.DateView
import com.gerwalex.mymonma.ui.content.NoEntriesBox
import com.gerwalex.mymonma.ui.navigation.AddRegelmTrx
import com.gerwalex.mymonma.ui.navigation.Destination
import com.gerwalex.mymonma.ui.navigation.EditRegelmTrx
import com.gerwalex.mymonma.ui.navigation.RegelmTrxList
import com.gerwalex.mymonma.ui.navigation.TopToolBar
import com.gerwalex.mymonma.ui.navigation.Up
import com.gerwalex.mymonma.workers.RegelmTrxWorker
import kotlinx.coroutines.launch


@Composable
fun RegelmTrxList(viewModel: MonMaViewModel, navigateTo: (Destination) -> Unit) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val list by dao.getRegelmTrxList().collectAsState(initial = emptyList())
    if (list.isNotEmpty()) {
        val snackbarHostState = remember { SnackbarHostState() }
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            topBar = {
                TopToolBar(
                    title = RegelmTrxList.name,
                    actions = {
                        IconButton(onClick = { navigateTo(AddRegelmTrx) }) {
                            Icon(imageVector = Icons.Default.Add, "")
                        }
                        IconButton(onClick = {
                            scope.launch {
                                RegelmTrxWorker.doWork(context)
                            }
                        }) {
                            Icon(imageVector = Icons.Default.Work, "")
                        }
                    }) {
                    navigateTo(Up)
                }
            }
        )
        { padding ->
            val message = stringResource(id = R.string.deleted)
            val undo = stringResource(id = R.string.undo)
            LazyColumn(Modifier.padding(padding)) {
                items(list, key = { item -> item.id!! }) { item ->
                    TrxRegelmItem(
                        trx = item, selectedItem = { trx ->
                            navigateTo(EditRegelmTrx.apply {
                                id = trx.id!!
                            })
                        },
                        onDismissed = { trx ->
                            scope.launch {
                                val trxList = dao.getTrxRegelm(trx.id!!)
                                dao.delete(trx.trxRegelm)
                                val result = snackbarHostState.showSnackbar(
                                    message = message,
                                    actionLabel = undo,
                                    withDismissAction = true
                                )
                                when (result) {
                                    SnackbarResult.Dismissed -> {}
                                    SnackbarResult.ActionPerformed -> {
                                        scope.launch {
                                            TrxRegelmView.insert(trxList)
                                        }
                                    }
                                }
                            }
                        })
                }
            }
        }
    } else {
        NoEntriesBox()
    }
}

@Composable
fun RegelmTrxListItem(trx: TrxRegelmView, selected: (TrxRegelmView) -> Unit) {
    Card(modifier = Modifier
        .padding(4.dp)
        .clickable {
            selected(trx)
        }) {
        Column(
            modifier = Modifier.wrapContentHeight(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row {
                DateView(date = trx.btag)
                Text(
                    text = trx.partnername,
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                AmountView(value = trx.amount)
            }
            trx.memo?.let { memo ->
                Text(text = memo, maxLines = 1)
            }
            Row {
                Text(text = trx.accountname)
                Spacer(modifier = Modifier.weight(1f))
                Text(text = stringResource(id = trx.intervallname))
            }
        }
    }

}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun LazyItemScope.TrxRegelmItem(
    trx: TrxRegelmView,
    selectedItem: (TrxRegelmView) -> Unit,
    onDismissed: (TrxRegelmView) -> Unit
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
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable {
                        selectedItem(trx)
                    }
            ) {
                RegelmTrxListItem(trx = trx, selectedItem)
            }
        })

}

@Preview(name = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun RegelmTrxListItemPreview() {
    AppTheme {
        val trx = TrxRegelmView(
            partnername = "Ein Partner für RegelmTrx",
            accountname = "account",
            amount = 12345678L,
            memo = "Ein langer Memotext aber nur in einer Zeile"
        )
        Surface {
            RegelmTrxListItem(trx = trx) {}
        }

    }

}
