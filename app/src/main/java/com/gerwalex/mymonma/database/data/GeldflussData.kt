package com.gerwalex.mymonma.database.data

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gerwalex.mymonma.ui.AppTheme
import com.gerwalex.mymonma.ui.content.AmountView

data class GeldflussData(
    val reportid: Long? = null,
    val catid: Long,
    val name: String,
    val amount: Long,
    val repcnt: Int,
)

@Composable
fun GeldflussDataItem(trx: GeldflussData, onClicked: () -> Unit) {
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .padding(8.dp)
    ) {
        Row(modifier = Modifier.clickable {
            onClicked()
        }) {
            Text(text = trx.name)
            Spacer(modifier = Modifier.weight(1f))
            AmountView(value = trx.amount, fontWeight = FontWeight.Bold)
        }
    }

}

@Preview(name = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun GeldflussDataItemPreview() {
    AppTheme {
        Surface {
            val data = GeldflussData(
                catid = 0,
                name = "Kategoriename",
                amount = -123456L,
                repcnt = 123,
            )
            GeldflussDataItem(trx = data) {}

        }
    }
}

