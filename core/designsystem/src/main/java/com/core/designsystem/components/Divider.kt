package com.core.designsystem.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.core.designsystem.theme.AllForMemoryTheme
import com.core.designsystem.theme.HarooTheme

@Composable
fun HarooDivider(
    modifier: Modifier = Modifier,
    color: Color = HarooTheme.colors.uiBorder,
    alpha: Float = 1f,
    thickness: Dp = 1.dp,
    startIndent: Dp = 0.dp // start offset
) {
    Divider(
        modifier = modifier,
        color = color.copy(alpha = alpha),
        thickness = thickness,
        startIndent = startIndent
    )
}

@Composable
fun HarooDashLine(
    modifier: Modifier = Modifier,
    color: Color = HarooTheme.colors.uiBorder,
    alpha: Float = 1f,
    thickness: Float = 3f,
    dash: Float = 10f
) {
    val pathEffect = PathEffect.dashPathEffect(
        floatArrayOf(dash, dash), 0f
    )
    Canvas(
        modifier = modifier.fillMaxWidth()
    ) {
        drawLine(
            color = color,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            strokeWidth = thickness,
            alpha = alpha,
            pathEffect = pathEffect
        )
    }
}

@Composable
@Preview(name = "basic divider")
fun HarooDividerPreview() {
    AllForMemoryTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .background(
                    Brush.linearGradient(HarooTheme.colors.gradient4_1)
                ),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            HarooDivider()
            HarooDashLine()
        }
    }
}