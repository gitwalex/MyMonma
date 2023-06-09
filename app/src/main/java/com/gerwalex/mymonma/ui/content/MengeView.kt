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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.gerwalex.mymonma.database.room.MyConverter.NACHKOMMA
import com.gerwalex.mymonma.ext.getActivity
import com.gerwalex.mymonma.ext.rememberState
import com.gerwalex.mymonma.main.ComposeActivity
import com.gerwalex.mymonma.ui.AppTheme
import com.maltaisn.calcdialog.CalcDialog
import java.math.BigDecimal
import java.text.NumberFormat


@Composable
fun MengeView(
    value: Long,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
    fontWeight: FontWeight = FontWeight.Normal,
) {
    val number = remember { NumberFormat.getNumberInstance() }
    val digits = remember { BigDecimal(NACHKOMMA) }
    val myValue by rememberState(value) {
        BigDecimal(value).divide(digits)
    }
    Text(
        modifier = modifier,
        text = number.format(myValue),
        style = style,
        fontWeight = fontWeight,
        textAlign = TextAlign.End,
    )
}

@Composable
fun MengeEditView(
    value: Long,
    modifier: Modifier = Modifier,
    fontWeight: FontWeight = FontWeight.Normal,
    onChanged: (Long) -> Unit
) {
    var myValue by rememberState { value }
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    Box(
        modifier = modifier.clickable {
            context.getActivity()?.supportFragmentManager?.let { fm ->
                CalcDialog().apply {
                    settings.also { settings ->
                        settings.isSignBtnShown = false
                        settings.initialValue = BigDecimal(myValue).divide(BigDecimal(NACHKOMMA))
                        settings.requestCode = ComposeActivity.CalcMengeResultRequest
                    }
                    fm.setFragmentResultListener(
                        ComposeActivity.CalcMengeResult,
                        lifecycleOwner
                    ) { _, result ->
                        result.getLong(ComposeActivity.CalcMengeResult).let { value ->
                            myValue = value
                            onChanged(value)
                        }

                    }
                    show(fm, null)
                }
            }
        }) {
        MengeView(
            value = myValue,
            modifier = modifier,
            fontWeight = fontWeight,
        )

    }
}

@Preview(name = "Light", uiMode = UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun MengeViewPreview() {
    AppTheme {
        Surface {
            MengeView(1234567890L)
        }
    }
}