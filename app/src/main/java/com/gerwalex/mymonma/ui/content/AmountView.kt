package com.gerwalex.mymonma.ui.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.gerwalex.mymonma.ComposeActivity
import com.gerwalex.mymonma.ext.getActivity
import com.maltaisn.calcdialog.CalcDialog
import java.math.BigDecimal
import java.text.NumberFormat


@Composable
fun AmountView(
    value: BigDecimal,
    modifier: Modifier = Modifier,
    style: TextStyle? = null,
    fontWeight: FontWeight = FontWeight.Normal,
    colorMode: Boolean = true,
) {
    val currency = remember { NumberFormat.getCurrencyInstance() }
    Text(
        modifier = modifier,
        text = currency.format(value),
        style = style ?: LocalTextStyle.current,
        fontWeight = fontWeight,
        color = if (colorMode && value < BigDecimal(0)) Color.Red else Color.Black,
        textAlign = TextAlign.End,
    )
}

@Composable
fun AmountEditView(
    value: BigDecimal,
    modifier: Modifier = Modifier,
    style: TextStyle? = null,
    fontWeight: FontWeight = FontWeight.Normal,
    colorMode: Boolean = true,
    onChanged: (BigDecimal) -> Unit
) {
    var myValue by remember(value) { mutableStateOf(value) }
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    Box(
        modifier = modifier.clickable {
            context.getActivity()?.supportFragmentManager?.let { fm ->
                CalcDialog().apply {
                    settings.also { settings ->
                        settings.numberFormat = NumberFormat.getCurrencyInstance()
                        settings.initialValue = myValue
                    }
                    fm.setFragmentResultListener(
                        ComposeActivity.CalcResult,
                        lifecycleOwner
                    ) { _, result ->
                        result.getString(ComposeActivity.CalcResult)?.let { value ->
                            myValue = BigDecimal(value)
                            onChanged(myValue)
                        }

                    }
                    show(fm, null)
                }
            }
        }) {
        AmountView(
            value = myValue,
            modifier = modifier,
            style = style,
            fontWeight = fontWeight,
            colorMode = colorMode
        )

    }
}

@Preview
@Composable
fun AmountViewPreview() {
    AmountView(BigDecimal(-12345678.90))
}