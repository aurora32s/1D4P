package com.core.designsystem.modifiers

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

fun Modifier.onInteraction(
    interactionSource: MutableInteractionSource
): Modifier = composed {
    clickable(
        interactionSource = interactionSource,
        indication = null,
        onClick = { }
    )
}