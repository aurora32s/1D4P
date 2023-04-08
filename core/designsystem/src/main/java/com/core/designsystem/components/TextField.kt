package com.core.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import com.core.designsystem.theme.AllForMemoryTheme
import com.core.designsystem.theme.HarooTheme

@Composable
fun HarooTextField(
    modifier: Modifier = Modifier,
    enabled: Boolean = true, // 사용 가능 여부
    value: String, // 입력 값
    onValueChange: (String) -> Unit, // 입력값 변경
    shape: Shape = MaterialTheme.shapes.medium,
    color: Color = HarooTheme.colors.uiBackground, // 배경 색
    contentColor: Color = HarooTheme.colors.text, // 내부 색
    border: BorderStroke? = null, // 테두리 모양
    placeHolder: String = "", // hint
    singleLine: Boolean = true, // 한줄 여부
    contentPadding: PaddingValues = PaddingValues(), // Surface 와 TextField padding
    maxLines: Int = Int.MAX_VALUE // 최대 입력 가능 라인
) {
    HarooSurface(
        modifier = modifier
            .padding(contentPadding),
        shape = shape,
        color = color,
        contentColor = contentColor,
        contentAlignment = Alignment.CenterStart,
        border = border
    ) {
        BasicTextField(
            textStyle = MaterialTheme.typography.body1,
            value = value,
            onValueChange = onValueChange,
            readOnly = enabled,
            singleLine = singleLine,
            maxLines = maxLines,
            decorationBox = { innerTextField ->
                if (value.isEmpty()) Text(text = placeHolder)
                innerTextField()
            },
            cursorBrush = SolidColor(color)
        )
    }
}

@Preview(name = "TextField")
@Composable
fun TextFieldPreview() {
    AllForMemoryTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(HarooTheme.colors.interactiveBackground)
                )
        ) {
            var value by remember { mutableStateOf("") }
            HarooTextField(
                value = value,
                onValueChange = { value = it },
                placeHolder = "내용을 입력해주세요."
            )
        }
    }
}