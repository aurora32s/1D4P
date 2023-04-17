package com.feature.monthly.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.core.designsystem.components.HarooRadioButton
import com.core.designsystem.components.HarooSurface
import com.core.model.feature.PostUiModel
import com.core.ui.post.GridPostItem
import com.core.ui.post.LinearPostItem
import java.time.LocalDate
import java.time.YearMonth

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
                modifier = Modifier.padding(Dimens.paddingRadioBtn),
                selected = listType,
                onSelected = onChangeListType
            )
            LazyColumn(
                state = lazyListState,
                contentPadding = PaddingValues(horizontal = Dimens.postListHorizontalPadding)
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