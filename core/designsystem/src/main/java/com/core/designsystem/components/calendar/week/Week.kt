package com.core.designsystem.components.calendar.week

import androidx.compose.runtime.Immutable
import com.core.designsystem.components.calendar.day.Day

@Immutable
internal class Week(
    val isFirstWeekOfTheMonth: Boolean = false,
    val days: List<Day>
)