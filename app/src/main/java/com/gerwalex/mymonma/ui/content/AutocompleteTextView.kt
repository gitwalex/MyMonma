package com.gerwalex.mymonma.ui.content

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import com.gerwalex.mymonma.ext.rememberState
import java.lang.Integer.max

@Composable
fun <T> AutoCompleteTextView(
    query: String,
    queryLabel: String,
    list: List<T>,
    modifier: Modifier = Modifier,
    showDropdown: Boolean = true,
    error: String? = null,
    onQueryChanged: (query: String) -> Unit = {},
    onDismissRequest: () -> Unit = {},
    onClearClick: () -> Unit = {},
    onItemClick: (T) -> Unit = {},
    onFocusChanged: (isFocused: Boolean) -> Unit = {},
    itemContent: @Composable (T) -> Unit = {},
) {
    val view = LocalView.current
    val lazyListState = rememberLazyListState()
    var shouldShowDropdown by rememberState(showDropdown) { showDropdown }
    Box(modifier = Modifier.imePadding()) {
        QuerySearch(
            query = query,
            label = queryLabel,
            error = error,
            onQueryChanged = onQueryChanged,
            onDoneActionClick = {
                view.clearFocus()
                onDismissRequest()
            },
            onClearClick = {
                onClearClick()
            },
            onFocusChanged = { focused ->
                onFocusChanged(focused)
                shouldShowDropdown = focused
            }

        )
        if (shouldShowDropdown) {
            Popup(popupPositionProvider = WindowCenterOffsetPositionProvider(),
                properties = PopupProperties(dismissOnClickOutside = true),
                onDismissRequest = { onDismissRequest() }
            ) {
                LazyColumn(
                    modifier = modifier
                        .imePadding()
                        .heightIn(max = TextFieldDefaults.MinHeight * 6)
                        .border(BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface))
                        .background(MaterialTheme.colorScheme.surface),
                    state = lazyListState,
                ) {
                    items(list) {
                        Box(
                            Modifier
                                .padding(8.dp)
                                .imePadding()
                                .clickable {
                                    view.clearFocus()
                                    onItemClick(it)
                                }) {
                            itemContent(it)
                        }
                    }
                }

            }
        }
    }
}

class WindowCenterOffsetPositionProvider : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset {
        return if (anchorBounds.bottom + popupContentSize.height > windowSize.height) {
            IntOffset(anchorBounds.left, max(anchorBounds.top - popupContentSize.height, 0))
        } else {
            IntOffset(anchorBounds.left, anchorBounds.bottom)

        }
    }
}