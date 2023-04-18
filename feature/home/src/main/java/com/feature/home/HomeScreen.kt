package com.feature.home

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.core.designsystem.components.HarooButton
import com.core.designsystem.components.HarooDashLine
import com.core.designsystem.components.HarooDivider
import com.core.designsystem.components.calendar.Calendar
import com.core.designsystem.modifiers.pagerHingeTransition
import com.core.designsystem.theme.HarooTheme
import com.core.designsystem.util.getString
import com.core.model.feature.PostUiModel
import com.core.model.feature.PostsUiModel
import com.core.ui.date.DateWithImage
import com.core.ui.date.RowMonthAndName
import com.core.ui.post.SimplePostItem
import java.time.LocalDate
import java.time.YearMonth

private val componentVerticalSpacer = 16.dp

@Composable
fun HomeRoute(
    onDailyClick: (LocalDate) -> Unit,
    onMonthlyClick: (YearMonth) -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel(),
    configuration: Configuration = LocalConfiguration.current
) {
    val postPagingItems = homeViewModel.posts.collectAsLazyPagingItems()
    val scrollState = rememberLazyListState(
        initialFirstVisibleItemIndex = 1,
        initialFirstVisibleItemScrollOffset = -configuration.screenHeightDp / 3
    )

    LaunchedEffect(key1 = Unit) {
        homeViewModel.homeUiEvent.collect {
            when (it) {
                HomeUiEvent.Initialized -> {}
                is HomeUiEvent.Success.RemovePost -> postPagingItems.refresh()
            }
        }
    }

    HomeScreen(
        postPagingItems = postPagingItems,
        scrollState = scrollState,
        toPostScreen = onDailyClick,
        toMonthlyScreen = onMonthlyClick,
        onRemovePost = homeViewModel::removePost
    )
}

@Composable
fun HomeScreen(
    postPagingItems: LazyPagingItems<PostsUiModel>,
    scrollState: LazyListState,
    toPostScreen: (LocalDate) -> Unit,
    toMonthlyScreen: (YearMonth) -> Unit,
    onRemovePost: (PostUiModel) -> Unit
) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        state = scrollState
    ) {
        items(
            items = postPagingItems,
            key = { posts -> posts.date }
        ) { posts ->
            posts?.let { postsUiModel ->
                MonthlyContainer(
                    date = postsUiModel.date,
                    posts = postsUiModel.posts,
                    onClickPost = toPostScreen,
                    onClickMonth = toMonthlyScreen,
                    onRemovePost = onRemovePost
                )
            }
        }
    }
}

/**
 * 각 월별 Component
 */
@Composable
fun MonthlyContainer(
    date: YearMonth,
    modifier: Modifier = Modifier,
    posts: List<PostUiModel>,
    onClickPost: (LocalDate) -> Unit,
    onClickMonth: (YearMonth) -> Unit,
    onRemovePost: (PostUiModel) -> Unit
) {
    val groupedPosts = remember(posts) { posts.associateBy { it.date } }
    Column(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(horizontal = componentVerticalSpacer),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RowMonthAndName(
                modifier = Modifier.padding(vertical = componentVerticalSpacer),
                date = date
            )
            MonthlyCalendar(date = date, posts = groupedPosts)
            HarooDashLine()
            DailyContainer(
                date = date, posts = groupedPosts,
                onClickPost = onClickPost,
                onRemovePost = onRemovePost
            )
        }
        MonthlyDivider(date = date, onClickMonthBtn = onClickMonth)
    }
}

/**
 * 달력
 */
@Composable
fun MonthlyCalendar(
    date: YearMonth,
    modifier: Modifier = Modifier,
    verticalSpace: Dp = 10.dp,
    horizontalSpace: Dp = 4.dp,
    posts: Map<LocalDate, PostUiModel>
) {
    Calendar(
        modifier = modifier.padding(vertical = componentVerticalSpacer),
        space = verticalSpace,
        currentMonth = date,
        dayContent = {
            DateWithImage(
                modifier = Modifier.padding(horizontal = horizontalSpace),
                state = it,
                image = posts[it.date]?.images?.firstOrNull()
            )
        }
    )
}

/**
 * 일별 View Pager
 */
@Composable
@OptIn(ExperimentalFoundationApi::class)
fun DailyContainer(
    date: YearMonth,
    modifier: Modifier = Modifier,
    posts: Map<LocalDate, PostUiModel>,
    onClickPost: (LocalDate) -> Unit,
    onRemovePost: (PostUiModel) -> Unit
) {
    val pagerState = rememberPagerState()
    val dateCount = remember(date) { date.lengthOfMonth() }
    HorizontalPager(
        modifier = modifier.padding(vertical = componentVerticalSpacer),
        pageCount = dateCount,
        key = { it },
        beyondBoundsPageCount = 3,
        state = pagerState
    ) {
        val day = date.atDay(it + 1)
        SimplePostItem(
            modifier = Modifier.pagerHingeTransition(it, pagerState),
            date = day,
            post = posts[day],
            onClickPost = onClickPost,
            onRemovePost = onRemovePost
        )
    }
}

@Composable
fun MonthlyDivider(
    date: YearMonth,
    modifier: Modifier = Modifier,
    onClickMonthBtn: (YearMonth) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HarooDivider(modifier = Modifier.weight(1f))
        HarooButton(
            shape = RoundedCornerShape(
                topStart = 100f, bottomStart = 100f
            ),
            onClick = { onClickMonthBtn(date) }
        ) {
            Text(text = getString(id = R.string.btn_view_month, date.monthValue))
        }
    }
}