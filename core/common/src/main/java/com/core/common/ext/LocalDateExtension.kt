package com.core.common.ext

import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

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

/**
 * 날짜 출력
 */
fun LocalDate.getDate(): String = dayOfMonth.padStart(2)

/**
 * 요일 출력(영어로)
 */
fun LocalDate.dayOfWeek(textStyle: TextStyle = TextStyle.FULL): String =
    dayOfWeek.getDisplayName(textStyle, Locale.US).uppercase()

fun LocalDate.fullDisplayName(): String {
    val year = year
    val month = monthValue.padStart(2)
    val day = dayOfMonth.padStart(2)

    return "${year}년 ${month}월 ${day}일"
}