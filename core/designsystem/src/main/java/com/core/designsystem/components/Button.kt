package com.core.designsystem.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.core.designsystem.modifiers.noRippleClickable
import com.core.designsystem.theme.AllForMemoryTheme
import com.core.designsystem.theme.HarooTheme
import kotlin.math.abs

@Composable
fun HarooButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true, // 사용 가능 여부
    shape: Shape = MaterialTheme.shapes.small,
    border: BorderStroke? = BorderStroke(
        width = 1.dp,
        color = HarooTheme.colors.uiBorder
    ), // 테두리 모양
    alpha: Float = 0f, // 배경색 투명도
    backgroundColor: Color = Color.Unspecified, // 배경 색
    disableBackgroundColor: Color = Color.Unspecified, // disable 일때 배경 색
    contentColor: Color = HarooTheme.colors.text, // 내부 색
    // disable 일때 배경 색
    disableContentColor: Color = HarooTheme.colors.text.copy(alpha = 0.3f),
    // 내부 padding
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit
) {
    HarooSurface(
        modifier = modifier.noRippleClickable(
            enabled = enabled,
            role = Role.Button,
            onClick = onClick
        ),
        shape = shape,
        color = if (enabled) backgroundColor else disableBackgroundColor,
        contentColor = if (enabled) contentColor else disableContentColor,
        alpha = alpha,
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
                    color = HarooTheme.colors.uiBorder
                ),
                onClick = {},
                alpha = 0f,
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(text = "추가")
            }
        }
    }
}

@Composable
fun HarooRadioButton(
    modifier: Modifier = Modifier,
    contentColor: Color = HarooTheme.colors.text,
    radioSize: Dp = 32.dp,
    selected: Boolean = false,
    onSelected: () -> Unit
) {
    val currentState = remember(selected) {
        MutableTransitionState(selected).apply { targetState = selected.not() }
    }
    val transition = updateTransition(targetState = currentState, label = null)
    val radioOffset = transition.animateFloat(
        transitionSpec = {
            tween(delayMillis = 0, durationMillis = 300, easing = LinearEasing)
        }, label = ""
    ) {
        if (it.currentState) 1f else 0f
    }

    HarooButton(
        modifier = modifier
            .height(radioSize)
            .width(radioSize * 2),
        onClick = onSelected,
        contentPadding = PaddingValues()
    ) {
        HarooSurface(
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
                .graphicsLayer {
                    translationX = radioOffset.value * radioSize.value * 2 - radioSize.value
                    scaleX = 1 - abs(0.5 - radioOffset.value).toFloat()
                    scaleY = 1 - abs(0.5 - radioOffset.value).toFloat()
                },
            shape = MaterialTheme.shapes.small,
            alpha = 1f
        ) {

        }
    }
}