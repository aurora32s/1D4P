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
import androidx.compose.ui.tooling.preview.Preview
import com.core.common.ext.*
import com.core.designsystem.theme.AllForMemoryTheme
import com.core.designsystem.theme.HarooTheme
import java.time.LocalDate

/**
 * 일, 요일 포함된 Component
 */
@Composable
fun RowMonthAndName(
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
                        .layoutId("Month"),
                    text = date.monthValue.padStart(2),
                    style = MaterialTheme.typography.h4
                )
                Text(
                    modifier = Modifier
                        .layoutId("DisplayName"),
                    text = date.month.displayName(),
                    style = MaterialTheme.typography.body1
                )
            }
        }
    ) { measureables, constraints ->
        val month = measureables.find { it.layoutId == "Month" }!!.measure(constraints)
        val displayName = measureables.find { it.layoutId == "DisplayName" }!!.measure(constraints)
        layout(
            width = constraints.maxWidth,
            height = month[LastBaseline] + 10
        ) {
            month.placeRelative(x = 0, y = 0)
            displayName.placeRelative(
                x = month.measuredWidth,
                y = month[LastBaseline] - displayName[FirstBaseline]
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
fun RowDayAndDatePreview() {
    AllForMemoryTheme {
        RowMonthAndName(
            date = LocalDate.now()
        )
    }
}