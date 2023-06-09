package com.gerwalex.mymonma.ui.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.gerwalex.mymonma.database.tables.Cat
import com.gerwalex.mymonma.ui.AppTheme


@Composable
fun AccountListView(cat: Cat, clicked: (Cat) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                clicked(cat)
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = "[${cat.name}]")
        AmountView(
            value = cat.saldo,
            modifier = Modifier.weight(1f)
        )

    }

}

@Preview
@Composable
fun AccountListPrevView() {
    val cat = Cat(name = "my Account")
    AppTheme {
        Surface {
            AccountListView(cat) {}
        }
    }
}