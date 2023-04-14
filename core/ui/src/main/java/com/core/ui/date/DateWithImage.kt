package com.core.ui.date

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.core.designsystem.components.HarooImage
import com.core.designsystem.components.HarooSurface
import com.core.designsystem.components.ImageType
import com.core.designsystem.components.calendar.day.DayState
import com.core.designsystem.theme.HarooTheme
import com.core.model.feature.ImageUiModel

/**
 * 이미지 와 함께 표시 되는 date component
 */
@Composable
fun DateWithImage(
    modifier: Modifier = Modifier,
    state: DayState,
    image: ImageUiModel?,
    dayColor: Color = LocalContentColor.current
) {
    val date = remember(state) { state.date }
    Box(
        modifier = modifier.fillMaxWidth().aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {
        image?.let {
            HarooImage(
                shape = MaterialTheme.shapes.small,
                imageType = ImageType.AsyncImage(it),
                elevation = 0.dp
            )
            HarooSurface(
                modifier = Modifier.fillMaxSize(),
                shape = MaterialTheme.shapes.small,
                color = HarooTheme.colors.dim,
                alpha = 0.5f
            ) {}
        }
        Text(
            text = date.dayOfMonth.toString(),
            color = dayColor.copy(alpha = if (image != null) 1f else 0.5f)
        )
    }
}