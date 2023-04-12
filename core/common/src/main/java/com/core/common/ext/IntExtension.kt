package com.core.common.ext

/**
 * 0n 으로 채워진 문자열 반환
 */
fun Int.padStart(length: Int) = toString().padStart(length, '0')