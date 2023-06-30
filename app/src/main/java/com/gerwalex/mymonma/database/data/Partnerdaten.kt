package com.gerwalex.mymonma.database.data

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.gerwalex.mymonma.ui.AppTheme
import com.gerwalex.mymonma.ui.content.AmountView

data class PartnerdatenReport(
    val reportid: Long = 0,
    val partnerid: Long = 0L,
    val name: String = "Partnername",
    val amount: Long = 0,
    val repcnt: Long = 0

)

@Composable
fun PartnerdatenItem(
    data: PartnerdatenReport,
    onClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClicked()
            },
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = data.name, maxLines = 1)
        AmountView(value = data.amount)
    }
}

@Preview(name = "Light", uiMode = UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PartnerdateItemPreview() {
    val data = PartnerdatenReport()
    AppTheme {
        Surface {
            PartnerdatenItem(data) {}
        }
    }
}