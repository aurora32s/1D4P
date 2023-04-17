package com.feature.monthly

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.hilt.navigation.compose.hiltViewModel
import com.core.designsystem.R
import com.core.designsystem.components.HarooHeader
import com.core.designsystem.theme.HarooTheme
import com.core.designsystem.util.getString
import com.core.ui.toolbar.CollapsingToolbar
import com.feature.monthly.ui.Dimens
import com.feature.monthly.ui.MonthlyBody
import com.feature.monthly.ui.MonthlyHeader

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
            HarooHeader(title = getString(id = R.string.app_name), onBackPressed = {})
            MonthlyHeader(
                modifier = Modifier.padding(bottom = Dimens.spaceBetweenHeaderAndBody),
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