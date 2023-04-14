package com.core.common.ext

import java.time.Month
import java.time.format.TextStyle
import java.util.*

/**
 * 특정 월의 Display Name 을 영어로
 */
fun Month.displayName(): String = getDisplayName(TextStyle.FULL, Locale.US)