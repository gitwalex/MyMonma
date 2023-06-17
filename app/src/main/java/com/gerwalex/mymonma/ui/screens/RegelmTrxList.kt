package com.gerwalex.mymonma.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gerwalex.mymonma.MonMaViewModel
import com.gerwalex.mymonma.database.room.DB.dao
import com.gerwalex.mymonma.database.views.TrxRegelmView
import com.gerwalex.mymonma.ui.AppTheme
import com.gerwalex.mymonma.ui.content.AmountView
import com.gerwalex.mymonma.ui.content.DateView
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
        Scaffold(
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
        {
            LazyColumn(Modifier.padding(it)) {
                items(list) { item ->
                    RegelmTrxListItem(trx = item) { trx ->
                        viewModel.regelmTrxId = trx.id!!
                        navigateTo(EditRegelmTrx)
                    }
                }
            }
        }
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
                DateView(date = trx.btag, onClick = {})
                Text(
                    text = trx.partnername ?: "",
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
                Text(text = trx.accountname ?: "")
                Spacer(modifier = Modifier.weight(1f))
                Text(text = stringResource(id = trx.intervallname))
            }
        }
    }

}

@Preview(name = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun RegelmTrxListItemPreview() {
    AppTheme {
        val trx = TrxRegelmView(
            accountname = "account",
            amount = 12345678L,
            memo = "Ein langer Memotext aber nur in einer Zeile"
        )
        Surface {
            RegelmTrxListItem(trx = trx) {}
        }

    }

}
