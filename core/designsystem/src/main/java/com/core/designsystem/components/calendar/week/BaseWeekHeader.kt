package com.core.designsystem.components.calendar.week

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.*

@Composable
fun BaseWeekHeader(
    modifier: Modifier = Modifier,
    dayOfWeek: List<DayOfWeek>
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        dayOfWeek.forEach {
            Text(
                text = it.getDisplayName(TextStyle.SHORT, Locale.getDefault())
            )
        }
    }
}

internal fun <T> Array<T>.rotateRight(n: Int): List<T> = takeLast(n) + dropLast(n)