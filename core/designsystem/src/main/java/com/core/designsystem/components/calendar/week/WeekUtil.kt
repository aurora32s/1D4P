package com.core.designsystem.components.calendar.week

import com.core.designsystem.components.calendar.day.WeekDay
import com.core.designsystem.components.calendar.day.daysUtil
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth


private const val DaysInAWeek = 7

internal fun YearMonth.getWeeks(
    includeAdjacentMonths: Boolean,
    firstDayOfTheWeek: DayOfWeek,
    today: LocalDate = LocalDate.now(),
): List<Week> {
    val daysLength = lengthOfMonth()

    val starOffset = atDay(1).dayOfWeek daysUtil firstDayOfTheWeek
    val endOffset =
        DaysInAWeek - (atDay(daysLength).dayOfWeek daysUtil firstDayOfTheWeek) - 1

    return (1 - starOffset..daysLength + endOffset).chunked(DaysInAWeek).mapIndexed { index, days ->
        Week(
            isFirstWeekOfTheMonth = index == 0,
            days = days.mapNotNull { dayOfMonth ->
                val (date, isFromCurrentMonth) = when (dayOfMonth) {
                    in Int.MIN_VALUE..0 -> if (includeAdjacentMonths) {
                        val previousMonth = this.minusMonths(1)
                        previousMonth.atDay(previousMonth.lengthOfMonth() + dayOfMonth) to false
                    } else {
                        return@mapNotNull null
                    }
                    in 1..daysLength -> atDay(dayOfMonth) to true
                    else -> if (includeAdjacentMonths) {
                        val previousMonth = this.plusMonths(1)
                        previousMonth.atDay(dayOfMonth - daysLength) to false
                    } else {
                        return@mapNotNull null
                    }
                }
                WeekDay(
                    date = date,
                    isFromCurrentMonth = isFromCurrentMonth,
                    isCurrentDay = date.equals(today),
                )
            }
        )
    }
}