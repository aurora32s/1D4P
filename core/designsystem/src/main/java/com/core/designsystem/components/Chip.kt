package com.core.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.core.designsystem.modifiers.noRippleClickable
import com.core.designsystem.theme.AllForMemoryTheme
import com.core.designsystem.theme.HarooTheme

@Composable
fun HarooChip(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    shape: Shape = MaterialTheme.shapes.small,
    backgroundColor: Color = HarooTheme.colors.uiBackground,
    // 테두리 색
    border: BorderStroke = BorderStroke(width = 1.dp, color = HarooTheme.colors.uiBorder),
    contentColor: Color = HarooTheme.colors.text,
    alpha: Float = 0f,
    content: @Composable RowScope.() -> Unit
) {
    HarooSurface(
        modifier = modifier.then(
            onClick?.let {
                Modifier.noRippleClickable(
                    role = Role.Button,
                    onClick = it
                )
            } ?: Modifier
        ),
        color = backgroundColor,
        shape = shape,
        alpha = alpha,
        border = border,
        contentColor = contentColor
    ) {
        ProvideTextStyle(value = MaterialTheme.typography.body1) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                content = content
            )
        }
    }
}

@Preview
@Composable
fun ChipPreview() {
    AllForMemoryTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(HarooTheme.colors.interactiveBackground)
                )
        ) {
            HarooChip {
                Text(text = "#자유여행")
            }
        }
    }
}