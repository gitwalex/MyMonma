package com.gerwalex.mymonma.ui.content

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.gerwalex.mymonma.ext.rememberState
import com.gerwalex.mymonma.ui.AppTheme
import java.math.BigDecimal
import java.text.NumberFormat
import kotlin.math.pow


@Composable
fun PercentView(
    value: Long,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
    fontWeight: FontWeight = FontWeight.Normal,
    colorMode: Boolean = true,
) {
    val number = remember { NumberFormat.getNumberInstance() }
    val digits = remember { BigDecimal(10.0.pow(2)) }
    val myValue by rememberState(value) { BigDecimal(value).divide(digits) }
    Text(
        modifier = modifier,
        text = "${number.format(myValue)}%",
        style = style,
        fontWeight = fontWeight,
        color = if (colorMode && myValue < BigDecimal(0)) Color.Red else MaterialTheme.colorScheme.onSurface,
        textAlign = TextAlign.End,
    )
}


@Preview(name = "Light", uiMode = UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PercentViewPreview() {
    AppTheme {
        Surface {
            PercentView(12_51)
        }
    }
}