package com.gerwalex.mymonma.ui.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp


@Composable
fun AutoCompleteTextView(
    modifier: Modifier,
    query: String,
    queryLabel: String,
    count: Int,
    onQueryChanged: (query: String) -> Unit = {},
    onDoneActionClick: () -> Unit = {},
    onClearClick: () -> Unit = {},
    onItemClick: (position: Int) -> Unit = {},
    onFocusChanged: (isFocused: Boolean) -> Unit = {},
    itemContent: @Composable (position: Int) -> Unit = {}
) {
    val view = LocalView.current
    val lazyListState = rememberLazyListState()
    var showSelections by remember { mutableStateOf(false) }
    showSelections = count > 1
    LazyColumn(
        state = lazyListState,
        modifier = modifier.heightIn(max = TextFieldDefaults.MinHeight * 6)
    ) {
        item {
            QuerySearch(
                query = query,
                label = queryLabel,
                onQueryChanged = onQueryChanged,
                onDoneActionClick = {
                    view.clearFocus()
                    onDoneActionClick()
                    showSelections = false
                },
                onClearClick = {
                    onClearClick()
                },
                onFocusChanged = { isFocused ->
                    showSelections = isFocused
                    onFocusChanged(isFocused)
                }

            )
        }
        if (showSelections) {
            items(
                count = count
            ) { position ->
                Row(
                    Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .clickable {
                            view.clearFocus()
                            showSelections = false
                            onItemClick(position)
                        }
                ) {
                    itemContent(position)
                }
            }
        }

    }
}
