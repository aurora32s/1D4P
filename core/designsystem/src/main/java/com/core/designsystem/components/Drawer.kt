package com.core.designsystem.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.DrawerDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import com.core.designsystem.theme.HarooTheme

private fun <T> drawerAnimationSpec() = tween<T>(durationMillis = 150, easing = LinearEasing)

@Composable
fun HarooBottomDrawer(
    modifier: Modifier = Modifier,
    drawerState: HarooBottomDrawerState = rememberHarooBottomDrawerState(),
    drawerContent: @Composable BoxScope.() -> Unit,
    drawerShape: Shape = MaterialTheme.shapes.large,
    drawerElevation: Dp = DrawerDefaults.Elevation,
    enter: FiniteAnimationSpec<IntOffset> = drawerAnimationSpec(),
    exit: FiniteAnimationSpec<IntOffset> = drawerAnimationSpec(),
    scrimColor: Color = HarooTheme.colors.dim,
    scrimAlpha: Float = 0.5f,
    content: @Composable BoxScope.() -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        content()
        AnimatedVisibility(
            modifier = Modifier.fillMaxSize(),
            visible = drawerState.isShow.value,
            enter = fadeIn(drawerAnimationSpec())
        ) {
            HarooSurface(
                modifier = Modifier.fillMaxSize(),
                color = scrimColor,
                alpha = scrimAlpha,
                content = {}
            )
        }
        AnimatedVisibility(
            modifier = Modifier.align(Alignment.BottomCenter),
            visible = drawerState.isShow.value,
            enter = slideInVertically(
                initialOffsetY = { fullHeight -> fullHeight },
                animationSpec = enter
            ),
            exit = slideOutVertically(
                targetOffsetY = { fullHeight -> fullHeight },
                animationSpec = exit
            ),
            content = {
                HarooSurface(
                    shape = drawerShape,
                    elevation = drawerElevation,
                    content = drawerContent
                )
            }
        )
    }
}

@Composable
fun rememberHarooBottomDrawerState(
    initialValue: Boolean = false
) = remember(initialValue) {
    HarooBottomDrawerState(initialValue)
}

class HarooBottomDrawerState(
    initialValue: Boolean
) {
    val isShow = mutableStateOf(initialValue)

    fun hide() {
        isShow.value = false
    }

    fun show() {
        isShow.value = true
    }
}