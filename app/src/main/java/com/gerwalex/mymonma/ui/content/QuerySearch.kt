package com.gerwalex.mymonma.ui.content

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue

@Composable
fun QuerySearch(
    modifier: Modifier = Modifier,
    query: String,
    label: String,
    error: String? = null,
    onDoneActionClick: () -> Unit = {},
    onClearClick: () -> Unit = {},
    onQueryChanged: (String) -> Unit,
    onFocusChanged: (isFocused: Boolean) -> Unit = {}
) {


    var showClearButton by remember { mutableStateOf(false) }
    var textFieldValueState by remember(query) {
        mutableStateOf(
            TextFieldValue(
                text = query,
            )
        )
    }

    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                if (focusState.isFocused) {
                    showClearButton = true
                    textFieldValueState = textFieldValueState.copy(
                        selection = TextRange(0, query.length)
                    )
                }
                onFocusChanged(focusState.isFocused)
            },

        value = query,
        onValueChange = {
            Log.d("QuerySearch", "QuerySearch: $it")
//            textFieldValueState = it
            onQueryChanged(it)
        },
        label = { Text(text = label) },
        isError = error == null,
        supportingText = {
            error?.let {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = error,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        textStyle = MaterialTheme.typography.labelSmall,
        singleLine = true,
        trailingIcon = {
            if (showClearButton) {
                IconButton(onClick = { onClearClick() }) {
                    Icon(imageVector = Icons.Filled.Close, contentDescription = "Clear")
                }
            }

        },
        keyboardActions = KeyboardActions(onDone = {
            onDoneActionClick()
        }),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Text
        )
    )


}