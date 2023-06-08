package com.gerwalex.mymonma.ui.content

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.gerwalex.mymonma.database.tables.Account
import com.gerwalex.mymonma.ui.AppTheme
import java.math.BigDecimal


@Composable
fun AccountView(account: Account) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = account.name)
        AmountView(
            value = account.saldo ?: BigDecimal.ZERO,
            modifier = Modifier.weight(1f)
        )

    }

}

@Preview
@Composable
fun AccountPrevView() {
    val account = Account(name = "my Account")
    AppTheme {
        Surface {
            AccountView(account = account)
        }
    }
}