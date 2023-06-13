package com.gerwalex.mymonma.ui.subscreens

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.database.tables.AutoCompleteCatView
import com.gerwalex.mymonma.database.views.CashTrxView
import com.gerwalex.mymonma.ui.AppTheme
import com.gerwalex.mymonma.ui.content.AmountEditView
import com.gerwalex.mymonma.ui.content.AmountView
import com.gerwalex.mymonma.ui.content.QuerySearch


@Composable
fun Splitlist(
    main: CashTrxView,
    splitlist: MutableList<CashTrxView>,
    isError: (Boolean) -> Unit,
) {
    var splitsumme by remember { mutableStateOf(0L) }
    var differenz by remember { mutableStateOf(0L) }
    if (LocalInspectionMode.current) {
        differenz = 123456 // Erzwingen Anzeige
    }

    Column {
        Divider(modifier = Modifier.padding(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.splittbuchung),
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = {
                splitlist.add(
                    main.copy(
                        id = null,
                        catid = 0,
                        amount = 0,
                        catname = ""
                    )
                )
            }) {
                Icon(imageVector = Icons.Default.Add, "")
            }

        }
        if (differenz != 0L) {
            DifferenzView(differenz) {
                main.amount -= differenz
            }
        }
        Row {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(id = R.string.summe),
                style = MaterialTheme.typography.labelSmall
            )
        }
        Row {
            Spacer(modifier = Modifier.weight(1f))
            AmountView(value = splitsumme)
        }
        LazyColumn {
            items(splitlist) {
                SplitLine(trx = it, onChanged = {
                    var amount = 0L
                    var error = false
                    splitlist.forEach { trx ->
                        amount += trx.amount
                        error = error || trx.catid == -1L
                    }
                    splitsumme = amount
                    differenz = main.amount - splitsumme
                    isError(error || differenz != 0L)
                })
            }
        }

    }
}


@Composable
fun SplitLine(trx: CashTrxView, onChanged: () -> Unit) {
    Card(modifier = Modifier.padding(4.dp)) {
        Row(
            modifier = Modifier
                .wrapContentHeight()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            if (LocalInspectionMode.current) {
                QuerySearch(query = "Kategorie", label = "kategorie", onQueryChanged = {})
            } else {
                AutoCompleteCatView(filter = trx.catname) { cat ->
                    trx.catid = cat.id ?: -1L
                    onChanged()
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            AmountEditView(value = trx.amount) {
                trx.amount = it
                onChanged()
            }

        }

    }
}

@Composable
fun DifferenzView(differenz: Long, onClick: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        TextButton(onClick = { onClick() }) {
            Text(text = stringResource(id = R.string.calculate))
        }
        Spacer(modifier = Modifier.weight(1f))
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = stringResource(id = R.string.differenz),
                style = MaterialTheme.typography.labelSmall
            )
            AmountView(value = differenz)
        }
    }
}

@Preview(name = "Light", uiMode = UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun SplitListPreview() {
    val list = ArrayList<CashTrxView>().apply {
        add(CashTrxView(transferid = 1))
        add(CashTrxView(transferid = 1))
        add(CashTrxView(transferid = 1))

    }
    AppTheme {
        val trx = CashTrxView()
        Surface {
            Splitlist(main = trx, splitlist = list) {}
        }
    }

}


@Preview(name = "Light", uiMode = UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun DifferenzColumnPreview() {
    AppTheme {
        Surface {
            DifferenzView(differenz = 123456L) {
            }
        }
    }
}

@Preview(name = "Light", uiMode = UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun SplitlinePreview() {
    AppTheme {
        Surface {
            SplitLine(trx = CashTrxView(), onChanged = {})
        }

    }

}





