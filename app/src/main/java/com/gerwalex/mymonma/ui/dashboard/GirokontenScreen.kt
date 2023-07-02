package com.gerwalex.mymonma.ui.dashboard

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.database.tables.Cat
import com.gerwalex.mymonma.database.views.AccountCashView
import com.gerwalex.mymonma.ext.rememberState
import com.gerwalex.mymonma.main.MonMaViewModel
import com.gerwalex.mymonma.ui.AppTheme
import com.gerwalex.mymonma.ui.content.AmountView
import com.gerwalex.mymonma.ui.navigation.CashTrxList
import com.gerwalex.mymonma.ui.navigation.Destination

@Composable
fun GirokontenScreen(viewModel: MonMaViewModel, navigateTo: (Destination) -> Unit) {
    val accountList by viewModel.accountlist.collectAsState(initial = emptyList())
    GirokontenScreen(list = accountList.filter { it.obercatid == Cat.GiroCATID && !it.ausgeblendet }) {
        navigateTo(CashTrxList.apply {
            id = it.id
        })
    }
}

@Composable
fun GirokontenScreen(
    list: List<AccountCashView>,
    onClick: (AccountCashView) -> Unit
) {
    var gesamSaldo by rememberState { 0L }
    LaunchedEffect(key1 = list) {
        var saldo = 0L
        list.map { saldo += it.saldo }
        gesamSaldo = saldo
    }
    Box(
        modifier = Modifier
            .padding(4.dp)
            .border(
                1.dp,
                color = MaterialTheme.colorScheme.onSurface,
                shape = RectangleShape
            )
            .padding(4.dp)
    ) {
        LazyColumn(
            userScrollEnabled = false
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = stringResource(id = R.string.myGiros), fontWeight = FontWeight.Bold)
                    AmountView(value = gesamSaldo, fontWeight = FontWeight.Bold)

                }

            }
            items(list, key = { it.id }) { account ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onClick(account)
                        },
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = account.name)
                    AmountView(value = account.saldo)
                }

            }
        }
    }
}

@Preview(name = "Light", uiMode = UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun GirokontenScreenPreview() {
    val acountlist = ArrayList<AccountCashView>().apply {
        add(AccountCashView(id = 1, obercatid = Cat.GiroCATID, name = "Giro 1", saldo = 100_00))
        add(AccountCashView(id = 2, obercatid = Cat.GiroCATID, name = "Giro 2", saldo = -5000_00))
        add(AccountCashView(id = 3, obercatid = Cat.GiroCATID, name = "Giro 3", saldo = 3000_00))
        add(AccountCashView(id = 4, obercatid = Cat.GiroCATID, name = "Giro 4", saldo = 123_45))
    }
    AppTheme {
        Surface {
            GirokontenScreen(acountlist) {}
        }
    }

}