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
import com.core.common.ext.dayOfWeek
import com.core.common.ext.getDate
import com.core.common.ext.getYearMonth
import com.core.designsystem.theme.AllForMemoryTheme
import com.core.designsystem.theme.HarooTheme
import java.time.LocalDate

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
        val dayHeight = day[LastBaseline]
        layout(
            width = constraints.maxWidth,
            height = yearMonthHeight + dayHeight
        ) {
            yearMonth.placeRelative(x = 0, y = 0)
            day.placeRelative(x = 0, y = yearMonth[LastBaseline])
            dayOfWeek.placeRelative(
                x = day.measuredWidth,
                y = day[FirstBaseline] + (dayOfWeek.height - dayOfWeek[FirstBaseline])
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