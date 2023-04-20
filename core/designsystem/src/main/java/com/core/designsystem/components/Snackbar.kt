package com.core.designsystem.components

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarData
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.core.designsystem.theme.HarooTheme

@Composable
fun HarooSnackbar(
    modifier: Modifier = Modifier,
    snackbarData: SnackbarData,
    actionOnNewLine: Boolean = false,
    shape: Shape = MaterialTheme.shapes.medium,
    backgroundColor: Color = HarooTheme.colors.uiBackground,
    contentColor: Color = HarooTheme.colors.text,
    actionColor: Color = HarooTheme.colors.brand,
    elevation: Dp = 6.dp
) {
    Snackbar(
        modifier = modifier,
        snackbarData = snackbarData,
        actionOnNewLine = actionOnNewLine,
        shape = shape,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        actionColor = actionColor,
        elevation = elevation
    )
}