package com.feature.monthly

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import com.core.designsystem.components.HarooSurface
import com.core.designsystem.components.HarooVerticalDivider
import com.core.designsystem.components.calendar.Calendar
import com.core.designsystem.theme.HarooTheme
import com.core.model.feature.PostUiModel
import com.core.ui.date.DateWithImage
import com.core.ui.date.RowMonthAndName
import com.feature.monthly.ui.rememberToolbarState
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun MonthlyScreen() {
    val today = YearMonth.now()
    val dateCount = remember(today) { today.lengthOfMonth() }

    // Toolbar 가능 높이
    val toolbarHeightRange = with(LocalDensity.current) {
        60.dp.roundToPx()..150.dp.roundToPx()
    }

    val toolbarState = rememberToolbarState(toolbarHeightRange = toolbarHeightRange)
    val listState = rememberLazyListState()

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            // Scroll 이 발생하였을 떄
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                toolbarState.scrollTopLimitReached =
                    listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset == 0
                toolbarState.scrollOffset = toolbarState.scrollOffset - available.y
                return Offset(0f, toolbarState.consumed)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
            .background(Brush.linearGradient(HarooTheme.colors.interactiveBackground))
            .nestedScroll(nestedScrollConnection),
    ) {
        CompositionLocalProvider(
            LocalContentColor provides HarooTheme.colors.text
        ) {
            MonthlyHeader(
                date = today,
                posts = emptyMap(),
                heightProvider = { toolbarState.height },
                progressProvider = { toolbarState.progress })

            HarooSurface(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                alpha = 0.08f
            ) {
                LazyColumn(
                    state = listState
                ) {
                    items(count = dateCount) {
                        Text(text = "day $it", style = MaterialTheme.typography.h2)
                    }
                }
            }
        }
    }
}

@Composable
fun MonthlyHeader(
    date: YearMonth,
    modifier: Modifier = Modifier,
    verticalSpace: Dp = 4.dp,
    horizontalSpace: Dp = 4.dp,
    posts: Map<LocalDate, PostUiModel>,
    heightProvider: () -> Float,
    progressProvider: () -> Float
) {
    Layout(
        modifier = modifier,
        content = {
            RowMonthAndName(
                modifier = Modifier
                    .layoutId("Date")
                    .padding(16.dp),
                date = date
            )
            Calendar(
                modifier = Modifier
                    .layoutId("Calendar")
                    .graphicsLayer {
                        val progress = progressProvider()
                        alpha = progress
                        scaleY = progress
                        translationY = (progressProvider() - 1) * (size.height / 2)
                    },
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
            Row(
                modifier = Modifier
                    .layoutId("Info")
                    .graphicsLayer { alpha = 1 - progressProvider() }
                    .fillMaxWidth()
                    .height(50.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                MonthlyCountContainer(name = "전체일수", count = date.lengthOfMonth())
                HarooVerticalDivider(modifier = Modifier.padding(horizontal = 30.dp))
                MonthlyCountContainer(name = "기록일수", count = posts.size)
                HarooVerticalDivider(modifier = Modifier.padding(horizontal = 30.dp))
                MonthlyCountContainer(name = "이미지", count = posts.values.sumOf { it.images.size })
            }
        }
    ) { measureables, constraints ->
        val progress = progressProvider()
        val date = measureables.find { it.layoutId == "Date" }!!.measure(constraints)
        val calendar = measureables.find { it.layoutId == "Calendar" }!!.measure(constraints)
        val info = measureables.find { it.layoutId == "Info" }!!.measure(constraints)

        val height = lerp(
            start = (date.height + info.height).toDp(),
            stop = (date.height + calendar.height).toDp(),
            fraction = progress
        )
        val dateX = lerp(
            start = ((constraints.maxWidth - date.width) / 2).toDp(),
            stop = 0f.toDp(),
            fraction = progress
        )
        layout(
            constraints.maxWidth,
            height = height.roundToPx()
        ) {
            date.placeRelative(x = dateX.roundToPx(), y = 0)
            calendar.placeRelative(x = 0, y = date.height)
            info.placeRelative(x = 0, y = date.height)
        }
    }
}

@Composable
fun MonthlyCountContainer(
    name: String, // info name
    count: Int // info 개수
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = name, style = MaterialTheme.typography.body1)
        Text(text = count.toString(), style = MaterialTheme.typography.body1)
    }
}