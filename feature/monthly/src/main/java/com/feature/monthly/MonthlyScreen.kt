package com.feature.monthly

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.core.designsystem.components.HarooHeader
import com.core.designsystem.components.HarooRadioButton
import com.core.designsystem.components.HarooSurface
import com.core.designsystem.theme.HarooTheme
import com.core.model.feature.PostUiModel
import com.core.ui.post.GridPostItem
import com.core.ui.post.LinearPostItem
import com.core.ui.toolbar.CollapsingToolbar
import com.feature.monthly.ui.MonthlyHeader
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun MonthlyScreen(
    year: Int, month: Int,
    monthlyViewModel: MonthlyViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) { monthlyViewModel.init(year, month) }

    MonthlyScreen(
        monthlyScreenStateHolder = rememberMonthlyScreenState(
            year = year,
            month = month,
            monthlyViewModel = monthlyViewModel
        )
    )
}

@Composable
fun MonthlyScreen(
    monthlyScreenStateHolder: MonthlyScreenStateHolder
) {
    CollapsingToolbar(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
            .background(Brush.linearGradient(HarooTheme.colors.interactiveBackground)),
        listState = monthlyScreenStateHolder.lazyListState,
        toolbarState = monthlyScreenStateHolder.toolbarState
    ) {
        CompositionLocalProvider(
            LocalContentColor provides HarooTheme.colors.text
        ) {
            HarooHeader(title = "하루네컷", onBackPressed = {})
            MonthlyHeader(
                modifier = Modifier.padding(bottom = 26.dp),
                date = monthlyScreenStateHolder.date,
                posts = monthlyScreenStateHolder.groupedPost,
                progressProvider = { monthlyScreenStateHolder.toolbarState.progress }
            )
            MonthlyBody(
                lazyListState = monthlyScreenStateHolder.lazyListState,
                groupedPosts = monthlyScreenStateHolder.groupedPost,
                listType = monthlyScreenStateHolder.listType.value,
                date = monthlyScreenStateHolder.date,
                dateCount = monthlyScreenStateHolder.dateCount,
                onChangeListType = monthlyScreenStateHolder::toggleListType,
                onRemovePost = monthlyScreenStateHolder::removePost
            )
        }
    }
}

@Composable
fun MonthlyBody(
    lazyListState: LazyListState,
    groupedPosts: Map<LocalDate, PostUiModel>,
    listType: Boolean,
    date: YearMonth,
    dateCount: Int,
    onChangeListType: () -> Unit,
    onRemovePost: (PostUiModel) -> Unit
) {
    HarooSurface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        alpha = 0.08f
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HarooRadioButton(
                modifier = Modifier.padding(16.dp),
                selected = listType,
                onSelected = onChangeListType
            )
            LazyColumn(
                state = lazyListState,
                contentPadding = PaddingValues(horizontal = 36.dp)
            ) {
                items(count = dateCount) {
                    val day = date.atDay(it + 1)
                    MonthlyPostItem(
                        isFirstItem = it == 0,
                        isLastItem = it == dateCount - 1,
                        date = day,
                        post = groupedPosts[day],
                        listType = listType,
                        onRemovePost = onRemovePost
                    )
                }
            }
        }
    }
}

@Composable
fun MonthlyPostItem(
    isFirstItem: Boolean,
    isLastItem: Boolean,
    date: LocalDate,
    post: PostUiModel?,
    listType: Boolean,
    onRemovePost: (PostUiModel) -> Unit
) {
    if (listType) {
        GridPostItem(
            isFirstItem = isFirstItem,
            isLastItem = isLastItem,
            date = date,
            post = post,
            onRemovePost = onRemovePost
        )
    } else {
        LinearPostItem(
            isFirstItem = isFirstItem,
            isLastItem = isLastItem,
            date = date,
            post = post,
            onRemovePost = onRemovePost
        )
    }
}