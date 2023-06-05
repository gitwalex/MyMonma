package com.gerwalex.mymonma.ui.content

import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import java.math.BigDecimal
import java.text.NumberFormat


@Composable
fun AmountView(
    value: BigDecimal,
    style: TextStyle? = null,
    fontWeight: FontWeight = FontWeight.Normal,
    colorMode: Boolean = true,
) {
    val currency = remember { NumberFormat.getCurrencyInstance() }
    Text(
        text = currency.format(value),
        style = style ?: LocalTextStyle.current,
        fontWeight = fontWeight,
        color = if (colorMode && value < BigDecimal(0)) Color.Red else Color.Black
    )
}

@Preview
@Composable
fun AmountViewPreview() {
    AmountView(BigDecimal(-12345678))
}