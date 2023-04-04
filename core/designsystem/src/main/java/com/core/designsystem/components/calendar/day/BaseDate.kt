package com.core.designsystem.components.calendar.day

import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.core.designsystem.theme.HarooTheme
import java.time.LocalDate

/**
 * 기본 날짜가 보이는 Component
 */
@Composable
fun BaseDate(
    modifier: Modifier = Modifier,
    isAccent: Boolean,
    state: DayState,
    dayColor: Color = LocalContentColor.current
) {
    val date = remember(state) { state.date }
    Text(
        modifier = modifier,
        text = date.dayOfMonth.toString(),
        color = dayColor.copy(alpha = if (isAccent) 1f else 0.5f)
    )
}