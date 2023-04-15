package com.feature.home

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.core.designsystem.components.HarooButton
import com.core.designsystem.components.HarooDashLine
import com.core.designsystem.components.HarooDivider
import com.core.designsystem.components.calendar.Calendar
import com.core.designsystem.modifiers.pagerHingeTransition
import com.core.designsystem.theme.HarooTheme
import com.core.model.feature.PostUiModel
import com.core.ui.date.DateWithImage
import com.core.ui.date.RowMonthAndName
import com.core.ui.post.SimplePostItem
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun HomeScreen(
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
                is HomeUiEvent.Success.RemovePost -> {
                    postPagingItems.refresh()
                }
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
            .background(Brush.linearGradient(HarooTheme.colors.interactiveBackground)),
        horizontalAlignment = Alignment.CenterHorizontally,
        state = scrollState
    ) {
        itemsIndexed(items = postPagingItems, key = { index, posts -> posts.date }) { index, posts ->
            posts?.let { postsUiModel ->
                MonthlyContainer(
                    date = postsUiModel.date,
                    posts = postsUiModel.posts,
                    onClickPost = {},
                    onRemovePost = {
                        homeViewModel.removePost(index, it)
                    }
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
    onRemovePost: (PostUiModel) -> Unit
) {
    val groupedPosts = remember(posts) { posts.associateBy { it.date } }
    Column(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RowMonthAndName(
                modifier = Modifier.padding(vertical = 24.dp),
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
        MonthlyDivider(date = date)
    }
}

/**
 * 달력
 */
@Composable
fun MonthlyCalendar(
    date: YearMonth,
    modifier: Modifier = Modifier,
    posts: Map<LocalDate, PostUiModel>
) {
    Calendar(
        modifier = modifier.padding(vertical = 16.dp),
        space = 10.dp,
        currentMonth = date,
        dayContent = {
            DateWithImage(
                modifier = Modifier.padding(horizontal = 4.dp),
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
        modifier = modifier.padding(vertical = 16.dp),
        pageCount = dateCount, key = { it },
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
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HarooDivider(
            modifier = Modifier.weight(1f)
        )
        HarooButton(
            shape = RoundedCornerShape(topStart = 100f, bottomStart = 100f),
            onClick = { }
        ) {
            Text(text = "${date.monthValue}월 보기")
        }
    }
}