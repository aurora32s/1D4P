package com.feature.monthly

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.core.model.feature.PostUiModel
import com.core.ui.toolbar.ToolbarState
import com.core.ui.toolbar.rememberToolbarState
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun rememberMonthlyScreenState(
    yearMonth: YearMonth,
    monthlyViewModel: MonthlyViewModel,
    toolbarMinHeight: Dp = 60.dp,
    toolbarMaxHeight: Dp = 150.dp,
    lazyListState: LazyListState = rememberLazyListState()
): MonthlyScreenStateHolder {
    val posts = monthlyViewModel.posts.collectAsState()
    val listType = rememberSaveable { mutableStateOf(false) }

    val toolbarHeightRange = with(LocalDensity.current) {
        toolbarMinHeight.roundToPx()..toolbarMaxHeight.roundToPx()
    }
    val toolbarState: ToolbarState = rememberToolbarState(toolbarHeightRange)
    val groupedPost = remember {
        derivedStateOf { posts.value.associateBy { it.date } }
    }

    return remember(yearMonth) {
        MonthlyScreenStateHolder(
            date = yearMonth,
            posts = groupedPost,
            toolbarState = toolbarState,
            lazyListState = lazyListState,
            listType = listType,
            onRemovePost = monthlyViewModel::removePost
        )
    }
}

class MonthlyScreenStateHolder(
    val date: YearMonth,
    val posts: State<Map<LocalDate,PostUiModel>>,
    val toolbarState: ToolbarState,
    val lazyListState: LazyListState,
    val listType: MutableState<Boolean>,
    val onRemovePost: (PostUiModel) -> Unit
) {
    val dateCount: Int
        get() = date.lengthOfMonth()

    fun toggleListType() {
        listType.value = listType.value.not()
    }

    fun removePost(postUiModel: PostUiModel) {
        onRemovePost(postUiModel)
    }
}