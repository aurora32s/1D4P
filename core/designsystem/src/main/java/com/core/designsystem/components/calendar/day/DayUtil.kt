package com.core.designsystem.components.calendar.day

import java.time.DayOfWeek
import java.time.LocalDate

internal infix fun DayOfWeek.daysUtil(other: DayOfWeek) = (7 + (value - other.value)) % 7