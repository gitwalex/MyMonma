package com.gerwalex.mymonma.ui.content

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties


@Composable
fun AutoCompleteTextView(
    query: String,
    queryLabel: String,
    count: Int,
    modifier: Modifier = Modifier,
    onQueryChanged: (query: String) -> Unit = {},
    onDoneActionClick: () -> Unit = {},
    onClearClick: () -> Unit = {},
    onItemClick: (position: Int) -> Unit = {},
    onFocusChanged: (isFocused: Boolean) -> Unit = {},
    itemContent: @Composable (position: Int) -> Unit = {}
) {
    val view = LocalView.current
    val lazyListState = rememberLazyListState()
    var showPopUp by remember { mutableStateOf(false) }
    Column(modifier) {

        QuerySearch(
            query = query,
            label = queryLabel,
            onQueryChanged = onQueryChanged,
            onDoneActionClick = {
                view.clearFocus()
                onDoneActionClick()
                showPopUp = false
            },
            onClearClick = {
                onClearClick()
            },
            onFocusChanged = { isFocused ->
                showPopUp = isFocused
                onFocusChanged(isFocused)
            }

        )
        if (showPopUp) {
            Box {
                Popup(
                    properties = PopupProperties(dismissOnClickOutside = true),
                    onDismissRequest = { showPopUp = false }
                ) {
                    LazyColumn(
                        modifier = modifier
                            .border(BorderStroke(1.dp, MaterialTheme.colorScheme.surface))
                            .heightIn(max = TextFieldDefaults.MinHeight * 6)
                            .background(MaterialTheme.colorScheme.surface),
                        state = lazyListState,
                    ) {
                        items(
                            count = count
                        ) { position ->
                            Box(
                                Modifier
                                    .padding(8.dp)
                                    .fillMaxWidth()
                                    .clickable {
                                        view.clearFocus()
                                        onItemClick(position)
                                    }
                            ) {
                                itemContent(position)
                            }
                        }
                    }

                }
            }
        }
    }
}