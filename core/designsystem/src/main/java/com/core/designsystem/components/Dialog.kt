package com.core.designsystem.components

import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.core.designsystem.theme.HarooTheme

@Composable
fun HarooDialog(
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        (LocalView.current.parent as? DialogWindowProvider)?.window?.setDimAmount(0.7f)
        CompositionLocalProvider(
            LocalContentColor provides HarooTheme.colors.text
        ) {
            content()
        }
    }
}