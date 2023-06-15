package com.gerwalex.mymonma.ui.content

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.gerwalex.mymonma.ui.AppTheme
import com.maltaisn.calcdialog.CalcDialog
import java.math.BigDecimal
import java.text.NumberFormat
import kotlin.math.pow


@Composable
fun AmountView(
    value: Long,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
    fontWeight: FontWeight = FontWeight.Normal,
    colorMode: Boolean = true,
) {
    val currency = remember { NumberFormat.getCurrencyInstance() }
    val digits = remember { BigDecimal(10.0.pow(currency.maximumFractionDigits.toDouble())) }
    val myValue = BigDecimal(value).divide(digits)
    Text(
        modifier = modifier,
        text = currency.format(myValue),
        style = style,
        fontWeight = fontWeight,
        color = if (colorMode && myValue < BigDecimal(0)) Color.Red else MaterialTheme.colorScheme.onSurface,
        textAlign = TextAlign.End,
    )
}

@Composable
fun AmountEditView(
    value: Long,
    modifier: Modifier = Modifier,
    fontWeight: FontWeight = FontWeight.Normal,
    colorMode: Boolean = true,
    onChanged: (Long) -> Unit
) {
    val currency = remember { NumberFormat.getCurrencyInstance() }
    val digits = remember { BigDecimal(10.0.pow(currency.maximumFractionDigits.toDouble())) }
    var myValue by remember { mutableStateOf(0L) }
    myValue = value
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    Box(
        modifier = modifier.clickable {
            context.getActivity()?.supportFragmentManager?.let { fm ->
                CalcDialog().apply {
                    settings.also { settings ->
                        settings.numberFormat = NumberFormat.getCurrencyInstance()
                        settings.initialValue = BigDecimal(myValue).divide(digits)
                        settings.requestCode = ComposeActivity.CalcResultRequest
                    }
                    fm.setFragmentResultListener(
                        ComposeActivity.CalcResult,
                        lifecycleOwner
                    ) { _, result ->
                        result.getLong(ComposeActivity.CalcResult).let { value ->
                            myValue = value
                            onChanged(value)
                        }

                    }
                    show(fm, null)
                }
            }
        }) {
        AmountView(
            value = myValue,
            modifier = modifier,
            fontWeight = fontWeight,
            colorMode = colorMode
        )

    }
}

@Preview(name = "Light", uiMode = UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun AmountViewPreview() {
    AppTheme {
        Surface {
            AmountView(1234567890L)
        }
    }
}