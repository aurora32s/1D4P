package com.core.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.core.designsystem.theme.AllForMemoryTheme
import com.core.designsystem.theme.HarooTheme

@Composable
fun HarooTextField(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    value: String,
    onValueChange: (String) -> Unit,
    shape: Shape = MaterialTheme.shapes.medium,
    color: Color = HarooTheme.colors.uiBackground,
    contentColor: Color = HarooTheme.colors.text,
    border: BorderStroke? = null,
    placeHolder: String = "",
    singleLine: Boolean = true,
    maxLines: Int = Int.MAX_VALUE
) {
    HarooSurface(
        modifier = modifier,
        shape = shape,
        color = color,
        contentColor = contentColor,
        border = border
    ) {
        BasicTextField(
            modifier = Modifier.padding(16.dp),
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