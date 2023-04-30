package com.core.common.ext

import java.time.YearMonth

fun YearMonth.fullDisplayName(): String {
    val year = year
    val month = monthValue.padStart(2)
    return "${year}년 ${month}월"
}