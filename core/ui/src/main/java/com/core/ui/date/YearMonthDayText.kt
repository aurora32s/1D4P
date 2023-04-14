package com.core.ui.date

import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.core.common.ext.*
import com.core.designsystem.theme.AllForMemoryTheme
import com.core.designsystem.theme.HarooTheme
import java.time.LocalDate
import kotlin.math.max

@Composable
fun ColumnDayAndDate(
    modifier: Modifier = Modifier,
    date: LocalDate,
    dayTextStyle: TextStyle = MaterialTheme.typography.h4,
    dateTextStyle: TextStyle = MaterialTheme.typography.h5,
    dayTextColor: Color = HarooTheme.colors.text,
    dateTextColor: Color = HarooTheme.colors.text
) {
    Layout(
        modifier = modifier,
        content = {
            Text(
                modifier = Modifier
                    .layoutId("Day"),
                text = date.dayOfMonth.padStart(2),
                style = dayTextStyle,
                color = dayTextColor
            )
            Text(
                modifier = Modifier
                    .layoutId("DayOfWeek"),
                text = date.dayOfWeek(java.time.format.TextStyle.SHORT),
                style = dateTextStyle,
                color = dateTextColor
            )
        }
    ) { measureables, constraints ->
        val day = measureables.find { it.layoutId == "Day" }!!.measure(constraints)
        val dayOfWeek = measureables.find { it.layoutId == "DayOfWeek" }!!.measure(constraints)

        val dayX = if (day.width <= dayOfWeek.width) (dayOfWeek.width - day.width) / 2 else 0
        val dayOfWeekX = if (day.width < dayOfWeek.width) 0 else (day.width - dayOfWeek.width) / 2
        val dayOfWeekY = day[LastBaseline] + 30
        layout(
            width = max(day.width, dayOfWeek.width),
            height = dayOfWeekY + dayOfWeek.measuredHeight
        ) {
            day.placeRelative(x = dayX, y = 0)
            dayOfWeek.placeRelative(
                x = dayOfWeekX,
                y = dayOfWeekY
            )
        }
    }
}

/**
 * 일, 요일 포함된 Component
 */
@Composable
fun RowMonthAndName(
    modifier: Modifier = Modifier,
    date: LocalDate,
    monthTextStyle: TextStyle = MaterialTheme.typography.h2,
    nameTextStyle: TextStyle = MaterialTheme.typography.h5,
    contentColor: Color = HarooTheme.colors.text
) {
    Layout(
        modifier = modifier,
        content = {
            CompositionLocalProvider(
                LocalContentColor provides contentColor
            ) {
                Text(
                    modifier = Modifier
                        .layoutId("Month"),
                    text = date.monthValue.padStart(2),
                    style = monthTextStyle
                )
                Text(
                    modifier = Modifier
                        .layoutId("DisplayName"),
                    text = date.month.displayName(),
                    style = nameTextStyle
                )
            }
        }
    ) { measureables, constraints ->
        val month = measureables.find { it.layoutId == "Month" }!!.measure(constraints)
        val displayName = measureables.find { it.layoutId == "DisplayName" }!!.measure(constraints)

        val displayNameY = month[LastBaseline] - displayName[FirstBaseline]
        layout(
            width = constraints.maxWidth,
            height = displayNameY + displayName.measuredHeight
        ) {
            month.placeRelative(x = 0, y = 0)
            displayName.placeRelative(
                x = month.measuredWidth,
                y = month[LastBaseline] - displayName[FirstBaseline]
            )
        }
    }
}

@Composable
fun ColumnMonthAndName(
    modifier: Modifier = Modifier,
    date: LocalDate,
    monthTextStyle: TextStyle = MaterialTheme.typography.h2,
    nameTextStyle: TextStyle = MaterialTheme.typography.h5,
    contentColor: Color = HarooTheme.colors.text
) {
    Layout(
        modifier = modifier,
        content = {
            CompositionLocalProvider(
                LocalContentColor provides contentColor
            ) {
                Text(
                    modifier = Modifier
                        .layoutId("Month"),
                    text = date.monthValue.padStart(2),
                    style = monthTextStyle
                )
                Text(
                    modifier = Modifier
                        .layoutId("DisplayName"),
                    text = date.month.displayName(),
                    style = nameTextStyle
                )
            }
        }
    ) { measureables, constraints ->
        val month = measureables.find { it.layoutId == "Month" }!!.measure(constraints)
        val displayName = measureables.find { it.layoutId == "DisplayName" }!!.measure(constraints)

        val monthX = if (month.width <= displayName.width) (displayName.width - month.width) / 2 else 0
        val displayNameX = if (month.width < displayName.width) 0 else (month.width - displayName.width) / 2
        val displayNameY = month[LastBaseline] + 30
        layout(
            width = constraints.maxWidth,
            height = displayNameY + displayName.measuredHeight
        ) {
            month.placeRelative(x = monthX, y = 0)
            displayName.placeRelative(
                x = displayNameX,
                y = displayNameY
            )
        }
    }
}

/**
 * 연도, 월, 일이 포함된 Component
 */
@Composable
fun YearMonthDayText(
    modifier: Modifier = Modifier,
    date: LocalDate,
    contentColor: Color = HarooTheme.colors.text
) {
    Layout(
        modifier = modifier,
        content = {
            CompositionLocalProvider(
                LocalContentColor provides contentColor
            ) {
                Text(
                    modifier = Modifier
                        .layoutId("YearMonth"),
                    text = date.getYearMonth(),
                    style = MaterialTheme.typography.body1
                )
                Text(
                    modifier = Modifier
                        .layoutId("Day"),
                    text = date.getDate(),
                    style = MaterialTheme.typography.h4
                )
                Text(
                    modifier = Modifier
                        .layoutId("DayOfWeek"),
                    text = date.dayOfWeek(),
                    style = MaterialTheme.typography.body1
                )
            }
        }
    ) { measurables, constraints ->
        val yearMonth = measurables.find { it.layoutId == "YearMonth" }!!.measure(constraints)
        val day = measurables.find { it.layoutId == "Day" }!!.measure(constraints)
        val dayOfWeek = measurables.find { it.layoutId == "DayOfWeek" }!!.measure(constraints)

        val yearMonthHeight = yearMonth.height
        val dayHeight = day[FirstBaseline]
        layout(
            width = constraints.maxWidth,
            height = yearMonthHeight + dayHeight + 10
        ) {
            yearMonth.placeRelative(x = 0, y = 0)
            day.placeRelative(x = 0, y = yearMonth[LastBaseline])
            dayOfWeek.placeRelative(
                x = day.measuredWidth,
                y = yearMonth[LastBaseline] + dayHeight - dayOfWeek[FirstBaseline]
            )
        }
    }
}

@Composable
@Preview(name = "year, month, day preview")
fun YearMonthDayTextPreview() {
    AllForMemoryTheme {
        YearMonthDayText(
            date = LocalDate.now()
        )
    }
}

@Composable
@Preview(name = "horizontally day preview")
fun RowMonthAndNamePreview() {
    AllForMemoryTheme {
        RowMonthAndName(
            date = LocalDate.now()
        )
    }
}

@Composable
@Preview(name = "vertically day preview")
fun ColumnMonthAndNamePreview() {
    AllForMemoryTheme {
        ColumnMonthAndName(
            date = LocalDate.now()
        )
    }
}

@Composable
@Preview(name = "vertically dayAndDate preview")
fun ColumnDayAndDatePreview() {
    AllForMemoryTheme {
        ColumnDayAndDate(
            date = LocalDate.now().minusDays(10)
        )
    }
}