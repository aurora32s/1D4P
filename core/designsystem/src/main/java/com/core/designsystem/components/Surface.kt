package com.core.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.core.designsystem.theme.AllForMemoryTheme
import com.core.designsystem.theme.HarooTheme

@Composable
fun HarooSurface(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    color: Color = HarooTheme.colors.uiBackground,
    contentColor: Color = HarooTheme.colors.text,
    border: BorderStroke? = null,
    elevation: Dp = 0.dp,
    alpha: Float = 0.15f,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .shadow(elevation = elevation, shape = shape, clip = false)
            .zIndex(elevation.value)
            .then(if (border != null) Modifier.border(border, shape) else Modifier)
            .background(color.copy(alpha = alpha), shape)
            .clip(shape)
    ) {
        CompositionLocalProvider(
            LocalContentColor provides contentColor,
            content = content
        )
    }
}

@Preview(name = "Surface")
@Composable
fun SurfacePreview() {
    AllForMemoryTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.linearGradient(HarooTheme.colors.interactiveBackground))
        ) {
            HarooSurface {}
        }
    }
}