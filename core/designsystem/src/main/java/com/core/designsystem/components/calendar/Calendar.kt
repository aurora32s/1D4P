package com.core.designsystem.components.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.core.designsystem.components.HarooSurface
import com.core.designsystem.components.calendar.day.BaseDate
import com.core.designsystem.components.calendar.day.DayState
import com.core.designsystem.components.calendar.week.BaseWeekHeader
import com.core.designsystem.components.calendar.week.WeekContent
import com.core.designsystem.components.calendar.week.getWeeks
import com.core.designsystem.components.calendar.week.rotateRight
import com.core.designsystem.theme.AllForMemoryTheme
import com.core.designsystem.theme.HarooTheme
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.*

@Composable
fun Calendar(
    modifier: Modifier = Modifier,
    currentMonth: YearMonth, // 현재 year & month
    // 한주가 시작 하는 요일
    firstDayOfWeek: DayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek,
    // 금일
    today: LocalDate = LocalDate.now(),
    // 이전, 다음 달의 요일도 component 에 표현 할지 여부
    showAdjacentMonths: Boolean = false,
    // 세로 라인 중간 space 넓이
    space: Dp = 0.dp,
    // 날짜 Component
    dayContent: @Composable BoxScope.(DayState) -> Unit,
    // 요일 component
    weekHeader: @Composable ColumnScope.(List<DayOfWeek>) -> Unit = { BaseWeekHeader(dayOfWeek = it) },
) {
    val daysOfWeek = remember(firstDayOfWeek) {
        DayOfWeek.values().rotateRight(7 - firstDayOfWeek.ordinal)
    }

    HarooSurface(
        modifier = modifier,
        color = Color.Unspecified,
        alpha = 0f
    ) {
        ProvideTextStyle(value = MaterialTheme.typography.subtitle1) {
            Column(
                verticalArrangement = Arrangement.spacedBy(space)
            ) {
                weekHeader(daysOfWeek)
                currentMonth.getWeeks(
                    includeAdjacentMonths = showAdjacentMonths,
                    firstDayOfTheWeek = daysOfWeek.first(),
                    today = today
                ).forEach { week ->
                    WeekContent(
                        week = week,
                        dayContent = dayContent
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun CalendarPreview() {
    AllForMemoryTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(HarooTheme.colors.interactiveBackground)
                )
        ) {
            Calendar(
                modifier = Modifier.padding(
                    horizontal = 10.dp,
                    vertical = 20.dp
                ),
                space = 30.dp,
                currentMonth = YearMonth.now(),
                dayContent = {
                    BaseDate(
                        isAccent = it.isCurrentDay,
                        modifier = Modifier.align(Alignment.Center),
                        state = it
                    )
                }
            )
        }
    }
}