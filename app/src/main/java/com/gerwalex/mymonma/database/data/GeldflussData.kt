package com.gerwalex.mymonma.database.data

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.ext.rememberState
import com.gerwalex.mymonma.ui.AppTheme
import com.gerwalex.mymonma.ui.content.AmountView
import com.gerwalex.mymonma.ui.content.PercentView

data class GeldflussData(
    val reportid: Long? = null,
    val catid: Long,
    val name: String,
    val amount: Long,
    val repcnt: Int,
    val verglAmount: Long,
    val verglRepcnt: Int,
)

@Composable
fun GeldflussDataItem(trx: GeldflussData, isVergl: Boolean = false, onClicked: () -> Unit) {
    val splitted by rememberState(trx.name) {
        trx.name.split(":")
    }
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .clickable {
                    onClicked()
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                when (splitted.size) {
                    1 -> {
                        Text(splitted[0])
                    }

                    2 -> {
                        Text(splitted[0], style = MaterialTheme.typography.labelMedium)
                        Text(splitted[1])

                    }

                    else -> {
                        Text(
                            "${splitted[0]}/${splitted[1]}",
                            style = MaterialTheme.typography.labelMedium
                        )
                        Text(splitted[2])

                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Column(horizontalAlignment = Alignment.End) {
                AmountView(value = trx.amount, fontWeight = FontWeight.Bold)
                if (isVergl) {
                    Text(
                        text = stringResource(id = R.string.vergleich),
                        style = MaterialTheme.typography.labelSmall
                    )
                    AmountView(
                        value = trx.verglAmount,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelSmall
                    )
                }

            }

        }
        if (isVergl) {
            Row {
                AmountView(value = trx.amount - trx.verglAmount)
                PercentView(value = (trx.amount * 100 / (trx.amount + trx.verglAmount)))
            }
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
                name = "KategorieKlasse:KategorienArt:Kategorie ",
                amount = -123456L,
                repcnt = 123,
                verglAmount = -124563L,
                verglRepcnt = 100,

                )
            GeldflussDataItem(trx = data, isVergl = true) {}

        }
    }
}

