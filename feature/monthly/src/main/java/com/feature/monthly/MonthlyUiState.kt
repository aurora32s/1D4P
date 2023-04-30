package com.feature.monthly

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.core.model.feature.PostUiModel
import com.core.ui.manager.SnackbarManager
import com.core.ui.toolbar.ToolbarState
import com.core.ui.toolbar.rememberToolbarState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun rememberMonthlyScreenState(
    monthlyViewModel: MonthlyViewModel,
    toolbarMinHeight: Dp = 60.dp,
    toolbarMaxHeight: Dp = 150.dp,
    lazyListState: LazyListState = rememberLazyListState(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    snackbarManager: SnackbarManager = SnackbarManager
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

    return remember(monthlyViewModel, coroutineScope, snackbarManager) {
        MonthlyScreenStateHolder(
            monthlyViewModel = monthlyViewModel,
            date = monthlyViewModel.date.currentYearMonth,
            posts = groupedPost,
            toolbarState = toolbarState,
            lazyListState = lazyListState,
            listType = listType,
            onRemovePost = monthlyViewModel::removePost,
            coroutineScope = coroutineScope,
            snackbarManager = snackbarManager
        )
    }
}

class MonthlyScreenStateHolder(
    monthlyViewModel: MonthlyViewModel,
    val date: YearMonth,
    val posts: State<Map<LocalDate, PostUiModel>>,
    val toolbarState: ToolbarState,
    val lazyListState: LazyListState,
    val listType: MutableState<Boolean>,
    val onRemovePost: (PostUiModel) -> Unit,
    coroutineScope: CoroutineScope,
    snackbarManager: SnackbarManager
) {
    init {
        coroutineScope.launch {
            monthlyViewModel.getPost()
            monthlyViewModel.monthlyUiEvent.collect {
                when (it) {
                    is MonthlyUiEvent.Fail.GetPost -> _isFailToLoadPost.value = true
                    is MonthlyUiEvent.Fail.RemovePost -> snackbarManager.showMessage(it.messageId)
                    is MonthlyUiEvent.Success.RemovePost -> snackbarManager.showMessage(it.messageId)
                }
            }
        }
    }

    private val _isFailToLoadPost = mutableStateOf(false)
    val isFailToLoadPost: Boolean
        get() = _isFailToLoadPost.value

    val dateCount: Int
        get() = date.lengthOfMonth()

    fun toggleListType() {
        listType.value = listType.value.not()
    }

    fun removePost(postUiModel: PostUiModel) {
        onRemovePost(postUiModel)
    }
}