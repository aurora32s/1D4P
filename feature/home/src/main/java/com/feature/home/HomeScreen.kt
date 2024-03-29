package com.feature.home

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.core.designsystem.components.HarooButton
import com.core.designsystem.components.HarooDashLine
import com.core.designsystem.components.HarooDivider
import com.core.designsystem.components.calendar.Calendar
import com.core.designsystem.modifiers.noRippleClickable
import com.core.designsystem.modifiers.pagerHingeTransition
import com.core.designsystem.util.getString
import com.core.model.feature.PostUiModel
import com.core.model.feature.PostsUiModel
import com.core.ui.date.DateWithImage
import com.core.ui.date.RowMonthAndName
import com.core.ui.manager.SnackbarManager
import com.core.ui.post.SimplePostItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth

private val componentVerticalSpacer = 16.dp

@Composable
fun HomeRoute(
    onDailyClick: (LocalDate) -> Unit,
    onMonthlyClick: (YearMonth) -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel(),
    configuration: Configuration = LocalConfiguration.current,
    snackBarManager: SnackbarManager = SnackbarManager
) {
    val postPagingItems = homeViewModel.posts.collectAsLazyPagingItems()
    val scrollState = rememberLazyListState(
        initialFirstVisibleItemIndex = 1,
        initialFirstVisibleItemScrollOffset = -configuration.screenHeightDp / 3
    )
    val isItem = remember { derivedStateOf { postPagingItems.itemCount == HomeViewModel.PAGE_SIZE } }

    LaunchedEffect(key1 = Unit) {
        homeViewModel.homeUiEvent.collect {
            when (it) {
                is HomeUiEvent.Fail.RemovePost -> snackBarManager.showMessage(it.messageId)
                is HomeUiEvent.Success.RemovePost -> snackBarManager.showMessage(it.messageId)
            }
        }
    }
    LaunchedEffect(key1 = isItem.value) {
        if (isItem.value && homeViewModel.recentVisibleItemOffset != null) {
            val index = postPagingItems.itemSnapshotList.indexOfFirst { it?.date == homeViewModel.recentVisibleItem }
            if (index >= 0) scrollState.scrollToItem(
                index = index,
                scrollOffset = homeViewModel.recentVisibleItemOffset!!
            )
        }
    }
    DisposableEffect(key1 = Unit) {
        onDispose {
            homeViewModel.recentVisibleItem = postPagingItems.get(scrollState.firstVisibleItemIndex)?.date
            homeViewModel.recentVisibleItemOffset = scrollState.firstVisibleItemScrollOffset
        }
    }

    HomeScreen(
        postPagingItems = postPagingItems,
        scrollState = scrollState,
        toPostScreen = onDailyClick,
        toMonthlyScreen = onMonthlyClick,
        onRemovePost = homeViewModel::removePost,
        snackBarManager = snackBarManager
    )
}

@Composable
fun HomeScreen(
    postPagingItems: LazyPagingItems<PostsUiModel>,
    scrollState: LazyListState,
    toPostScreen: (LocalDate) -> Unit,
    toMonthlyScreen: (YearMonth) -> Unit,
    onRemovePost: (PostUiModel) -> Unit,
    snackBarManager: SnackbarManager
) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (postPagingItems.loadState.refresh is LoadState.Error) {
            snackBarManager.showMessage(R.string.fail_to_refresh_posts)
        } else if (postPagingItems.loadState.append is LoadState.Error) {
            snackBarManager.showMessage(R.string.fail_to_append_posts)
        } else if (postPagingItems.loadState.prepend is LoadState.Error) {
            snackBarManager.showMessage(R.string.fail_to_prepend_posts)
        }
//        if (postPagingItems.itemCount != 0) {
        MonthlyList(
            postPagingItems = postPagingItems,
            scrollState = scrollState,
            toPostScreen = toPostScreen,
            toMonthlyScreen = toMonthlyScreen,
            onRemovePost = onRemovePost
        )
//        }
    }
}

@Composable
fun MonthlyList(
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
@OptIn(ExperimentalFoundationApi::class)
fun MonthlyContainer(
    date: YearMonth,
    modifier: Modifier = Modifier,
    posts: List<PostUiModel>,
    coroutine: CoroutineScope = rememberCoroutineScope(),
    onClickPost: (LocalDate) -> Unit,
    onClickMonth: (YearMonth) -> Unit,
    onRemovePost: (PostUiModel) -> Unit
) {
    val pagerState = rememberPagerState()
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
            MonthlyCalendar(
                date = date,
                posts = groupedPosts,
                onClickPost = {
                    coroutine.launch {
                        pagerState.animateScrollToPage(page = it.dayOfMonth - 1)
                    }
                }
            )
            HarooDashLine()
            DailyContainer(
                pagerState = pagerState,
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
    posts: Map<LocalDate, PostUiModel>,
    onClickPost: (LocalDate) -> Unit
) {
    Calendar(
        modifier = modifier.padding(vertical = componentVerticalSpacer),
        space = verticalSpace,
        currentMonth = date,
        dayContent = {
            DateWithImage(
                modifier = Modifier
                    .padding(horizontal = horizontalSpace)
                    .noRippleClickable { onClickPost(it.date) },
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
    pagerState: PagerState,
    date: YearMonth,
    modifier: Modifier = Modifier,
    posts: Map<LocalDate, PostUiModel>,
    onClickPost: (LocalDate) -> Unit,
    onRemovePost: (PostUiModel) -> Unit
) {
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