package com.core.common.ext

import java.time.LocalDate

/**
 * 연도와 월 출력
 */
fun LocalDate.getYearMonth(
    separator: String = "."
): String {
    val year = year
    val month = monthValue

    return "$year${separator}${month.padStart(2)}"
}