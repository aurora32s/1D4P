package com.core.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ButtonDefaults
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
import com.core.designsystem.theme.AllForMemoryTheme
import com.core.designsystem.theme.HarooTheme

@Composable
fun HarooButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    shape: Shape = MaterialTheme.shapes.small,
    border: BorderStroke? = null,
    backgroundColor: Color = Color.Unspecified,
    disableBackgroundColor: Color = Color.Unspecified,
    contentColor: Color = HarooTheme.colors.text,
    disableContentColor: Color = HarooTheme.colors.text.copy(alpha = 0.3f),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit
) {
    HarooSurface(
        modifier = modifier.clickable(
            enabled = enabled,
            role = Role.Button,
            onClick = onClick
        ),
        shape = shape,
        color = if (enabled) backgroundColor else disableBackgroundColor,
        contentColor = if (enabled) contentColor else disableContentColor,
        border = border
    ) {
        ProvideTextStyle(value = MaterialTheme.typography.button) {
            Row(
                modifier = Modifier.padding(contentPadding),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                content = content
            )
        }
    }
}

@Preview(name = "Button")
@Composable
fun ButtonPreview() {
    AllForMemoryTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(HarooTheme.colors.interactiveBackground)
                )
        ) {
            HarooButton(
                border = BorderStroke(
                    width = 1.dp,
                    color = HarooTheme.colors.uiBackground
                ),
                onClick = {},
                contentPadding = PaddingValues(4.dp)
            ) {
                Text(text = "추가")
            }
        }
    }
}