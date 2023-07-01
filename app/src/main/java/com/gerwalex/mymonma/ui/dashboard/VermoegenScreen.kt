package com.gerwalex.mymonma.ui.dashboard

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.database.data.GesamtVermoegen
import com.gerwalex.mymonma.main.MonMaViewModel
import com.gerwalex.mymonma.ui.AppTheme
import com.gerwalex.mymonma.ui.content.AmountView

@Composable
fun VermoegenScreen(viewModel: MonMaViewModel) {
    val vermoegen by viewModel.gesamtvermoegen.collectAsState(initial = GesamtVermoegen())
    VermoegenScreen(vermoegen)
}

@Composable
fun VermoegenScreen(vermoegen: GesamtVermoegen) {
    Column(
        modifier = Modifier
            .padding(4.dp)
            .border(
                1.dp,
                color = MaterialTheme.colorScheme.onSurface,
                shape = RectangleShape
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = stringResource(id = R.string.vermoegen), fontWeight = FontWeight.Bold)
            AmountView(value = vermoegen.summe, fontWeight = FontWeight.Bold)

        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = stringResource(id = R.string.vermoegenCashKontoarten))
            AmountView(value = vermoegen.saldo)

        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = stringResource(id = R.string.shortDepo))
            AmountView(value = vermoegen.marktwert)

        }
    }

}

@Preview(name = "Light", uiMode = UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun VermoegenScreenPreview() {
    val vermoegen = GesamtVermoegen(saldo = -100_000_00, marktwert = 123_456_78)
    AppTheme {
        Surface {
            VermoegenScreen(vermoegen = vermoegen)
        }
    }

}