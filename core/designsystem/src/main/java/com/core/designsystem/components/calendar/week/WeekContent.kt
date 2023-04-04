package com.core.designsystem.components.calendar.week

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.core.designsystem.components.calendar.day.DayState

@Composable
internal fun WeekContent(
    modifier: Modifier = Modifier,
    week: Week,
    dayContent: @Composable BoxScope.(DayState) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = if (week.isFirstWeekOfTheMonth) Arrangement.End else Arrangement.Start
    ) {
        week.days.forEachIndexed { index, day ->
            Box(modifier = Modifier.fillMaxWidth(1f / (7 - index))) {
                dayContent(
                    DayState(day)
                )
            }
        }
    }
}
