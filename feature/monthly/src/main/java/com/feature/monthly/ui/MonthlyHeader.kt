package com.feature.monthly.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import com.core.designsystem.components.HarooVerticalDivider
import com.core.designsystem.components.calendar.Calendar
import com.core.designsystem.util.getString
import com.core.model.feature.PostUiModel
import com.core.ui.date.DateWithImage
import com.core.ui.date.RowMonthAndName
import java.time.LocalDate
import java.time.YearMonth
import com.feature.monthly.R

@Composable
fun MonthlyHeader(
    modifier: Modifier = Modifier,
    posts: Map<LocalDate, PostUiModel>,
    date: YearMonth,
    progressProvider: () -> Float
) {
    Layout(
        modifier = modifier,
        content = {
            MonthlyHeaderContent(
                date = date,
                posts = posts,
                progressProvider = progressProvider
            )
        }
    ) { measureables, constraints ->
        val progress = progressProvider()
        val day = measureables.find { it.layoutId == "Date" }!!.measure(constraints)
        val calendar = measureables.find { it.layoutId == "Calendar" }!!.measure(constraints)
        val info = measureables.find { it.layoutId == "Info" }!!.measure(constraints)
        val height = lerp(
            start = (day.height + info.height).toDp(),
            stop = (day.height + calendar.height).toDp(),
            fraction = progress
        )
        val dayX = lerp(
            start = ((constraints.maxWidth - day.width) / 2).toDp(),
            stop = 0f.dp,
            fraction = progress
        )
        layout(
            width = constraints.maxWidth,
            height = height.roundToPx()
        ) {
            day.placeRelative(x = dayX.roundToPx(), y = 0)
            calendar.placeRelative(x = 0, y = day.height)
            info.placeRelative(x = 0, y = day.height)
        }
    }
}

@Composable
fun MonthlyHeaderContent(
    date: YearMonth,
    posts: Map<LocalDate, PostUiModel>,
    monthAndNamePadding: Dp = Dimens.monthAndNameDefaultPadding,
    progressProvider: () -> Float
) {
    RowMonthAndName(
        modifier = Modifier
            .layoutId("Date")
            .padding(monthAndNamePadding),
        date = date
    )
    MonthlyCalendar(
        modifier = Modifier.layoutId("Calendar"),
        posts = posts,
        date = date,
        progressProvider = progressProvider
    )
    MonthlyInfosContainer(
        modifier = Modifier.layoutId("Info"),
        posts = posts,
        date = date,
        progressProvider = progressProvider
    )
}

@Composable
fun MonthlyCalendar(
    modifier: Modifier = Modifier,
    posts: Map<LocalDate, PostUiModel>,
    date: YearMonth,
    verticalSpace: Dp = Dimens.monthlyCalendarVerticalSpace,
    horizontalSpace: Dp = Dimens.monthlyCalendarHorizontalSpace,
    progressProvider: () -> Float
) {
    Calendar(
        currentMonth = date,
        space = verticalSpace,
        modifier = modifier
            .graphicsLayer {
                val progress = progressProvider()
                alpha = progress
                scaleY = progress
                translationY = (progress - 1) * (size.height / 2)
            },
        dayContent = {
            DateWithImage(
                modifier = Modifier.padding(horizontal = horizontalSpace),
                state = it,
                image = posts[it.date]?.images?.firstOrNull()
            )
        }
    )
}

@Composable
fun MonthlyInfosContainer(
    modifier: Modifier = Modifier,
    posts: Map<LocalDate, PostUiModel>,
    date: YearMonth,
    space: Dp = Dimens.monthlyInfoHorizontalSpace,
    progressProvider: () -> Float
) {
    Row(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .fillMaxWidth()
            .graphicsLayer { alpha = 1 - progressProvider() },
        horizontalArrangement = Arrangement.Center
    ) {
        MonthlyCountContainer(
            name = getString(id = R.string.total_date),
            count = date.lengthOfMonth()
        )
        HarooVerticalDivider(modifier = Modifier.padding(horizontal = space))
        MonthlyCountContainer(
            name = getString(id = R.string.post_count),
            count = posts.size
        )
        HarooVerticalDivider(modifier = Modifier.padding(horizontal = space))
        MonthlyCountContainer(
            name = getString(id = R.string.image_count),
            count = posts.values.sumOf { it.images.size })
    }
}

@Composable
fun MonthlyCountContainer(
    name: String, // info name
    count: Int, // info 개수
    verticalSpace: Dp = Dimens.monthlyInfoVerticalSpace
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(verticalSpace)
    ) {
        Text(text = name, style = MaterialTheme.typography.body1)
        Text(text = count.toString(), style = MaterialTheme.typography.body1)
    }
}