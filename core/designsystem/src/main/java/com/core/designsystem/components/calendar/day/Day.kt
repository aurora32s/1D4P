package com.core.designsystem.components.calendar.day

import java.time.LocalDate

interface Day {
    val date: LocalDate
    val isCurrentDay: Boolean
    val isFromCurrentMonth: Boolean
}