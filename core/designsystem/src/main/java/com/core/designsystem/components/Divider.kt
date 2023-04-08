package com.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
@Preview(name = "basic divider")
fun HarooDividerPreview() {
    AllForMemoryTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .background(
                    Brush.linearGradient(HarooTheme.colors.gradient4_1)
                )
        ) {
            HarooDivider(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}