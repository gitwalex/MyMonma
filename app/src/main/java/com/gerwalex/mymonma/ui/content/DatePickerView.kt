package com.gerwalex.mymonma.ui.content

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Popup
import com.gerwalex.mymonma.R
import java.sql.Date
import java.text.DateFormat


@Composable
fun DateView(
    date: Date,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
    fontWeight: FontWeight = FontWeight.Normal,
    onClick: () -> Unit = {}
) {
    val dateformatter = remember { DateFormat.getDateInstance(DateFormat.DEFAULT) }
    Column(modifier = Modifier.clickable {
        onClick()
    }) {
        Text(
            text = dateformatter.format(date),
            style = style,
            fontWeight = fontWeight,
        )
    }
}

@Composable
fun DateTimeView(
    date: Date,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
    fontWeight: FontWeight = FontWeight.Normal,
) {
    val dateformatter = remember { DateFormat.getDateTimeInstance().format(date) }
    Column(modifier) {
        Text(
            text = dateformatter.format(date),
            style = style,
            fontWeight = fontWeight,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerView(
    date: Date,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
    fontWeight: FontWeight = FontWeight.Normal,
    onChanged: (Date) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    DateView(date = date, style = style, fontWeight = fontWeight, onClick = {
        showDatePicker = true
    })
    Box {
        if (showDatePicker) {
            val datePickerState = rememberDatePickerState(initialSelectedDateMillis = date.time)
            Popup {
                Column {
                    DatePicker(
                        modifier = Modifier.background(MaterialTheme.colorScheme.surface),
                        state = datePickerState,
                        dateValidator = {
                            Log.d("DatePickerView", "DatePickerView:  ${Date(it)}")
                            onChanged(Date(it))
                            true
                        })
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let {
                            onChanged(Date(it))
                            showDatePicker = false


                        }
                    }) {
                        Text(
                            text = stringResource(id = R.string.ok),
                            modifier = Modifier
                                .fillMaxWidth(),
                            textAlign = TextAlign.End,
                            style = MaterialTheme.typography.bodyLarge
                        )

                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerView(
    date: Date,
    onChanged: (Date) -> Unit
) {
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = date.time)
    Popup {
        Column {
            DatePicker(
                modifier = Modifier.background(MaterialTheme.colorScheme.surface),
                state = datePickerState,
                dateValidator = {
                    Log.d("DatePickerView", "DatePickerView:  ${Date(it)}")
                    onChanged(Date(it))
                    true
                })
            TextButton(onClick = {
                datePickerState.selectedDateMillis?.let {
                    onChanged(Date(it))
                }
            }) {
                Text(
                    text = stringResource(id = R.string.ok),
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.bodyLarge
                )

            }
        }
    }


}

