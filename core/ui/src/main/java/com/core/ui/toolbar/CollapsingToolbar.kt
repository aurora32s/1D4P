package com.core.ui.toolbar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll

@Composable
fun CollapsingToolbar(
    modifier: Modifier = Modifier,
    listState: LazyListState,
    toolbarState: ToolbarState,
    content: @Composable () -> Unit
) {
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                toolbarState.scrollTopLimitReached =
                    listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset == 0
                toolbarState.scrollOffset = toolbarState.scrollOffset - available.y
                return Offset(x = 0f, y = toolbarState.consumed)
            }
        }
    }

    Column(
        modifier = modifier
            .nestedScroll(nestedScrollConnection)
    ) {
        content()
    }
}