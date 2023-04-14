package com.core.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.core.designsystem.theme.AllForMemoryTheme
import com.core.designsystem.theme.HarooTheme

@Composable
fun HarooSurface(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium, // 모양
    color: Color = HarooTheme.colors.uiBackground, // 배경색
    contentColor: Color = HarooTheme.colors.text, // 내용 색
    contentAlignment: Alignment = Alignment.Center,
    contentPadding: PaddingValues = PaddingValues(),
    border: BorderStroke? = null, // 테두리 모양
    elevation: Dp = 0.dp, // 그림자 크기
    alpha: Float = 0.15f, // 배경 투명도
    content: @Composable BoxScope.() -> Unit
) {
    CompositionLocalProvider(
        LocalContentColor provides contentColor
    ) {
        Box(
            modifier = modifier
                .then(
                    if (elevation > 0.dp) Modifier.shadow(
                        color = HarooTheme.colors.dim,
                        elevation = elevation
                    ) else Modifier
                )
                .then(if (border != null) Modifier.border(border, shape) else Modifier)
                .background(color.copy(alpha = alpha), shape)
                .clip(shape)
                .padding(contentPadding),
            contentAlignment = contentAlignment,
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
            HarooSurface(
                modifier = Modifier
                    .padding(10.dp)
                    .size(120.dp),
                elevation = 10.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(HarooTheme.colors.uiBackground)
                )
            }
        }
    }
}

fun Modifier.shadow(
    color: Color, // 그림자 색상
    elevation: Dp = 0.dp, // 그림자 offset
    spread: Float = 30f // 그림자 퍼짐(?) 정도
) = this.drawBehind {
    val transparentColor = android.graphics.Color.toArgb(color.copy(alpha = 0.0f).value.toLong())
    val shadowColor = android.graphics.Color.toArgb(color.copy(alpha = 1f).value.toLong())
    this.drawIntoCanvas {
        val paint = Paint()
        val frameworkPaint = paint.asFrameworkPaint()
        frameworkPaint.color = transparentColor
        // 그림자 크기와 위치 설정
        frameworkPaint.setShadowLayer(
            spread,
            elevation.toPx(),
            elevation.toPx(),
            shadowColor
        )
        // 실제 그리는 부분
        it.drawRect(
            0f,
            0f,
            this.size.width,
            this.size.height,
            paint
        )
    }
}