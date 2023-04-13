package com.core.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
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
    autoFocus: Boolean = false, // 자동 focus 여부
    shape: Shape = MaterialTheme.shapes.medium,
    color: Color = HarooTheme.colors.uiBackground, // 배경 색
    contentColor: Color = HarooTheme.colors.text, // 내부 색
    selectionColor: Color = HarooTheme.colors.brand,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    alpha: Float = 0f,
    border: BorderStroke? = null, // 테두리 모양
    placeHolder: String = "", // hint
    singleLine: Boolean = true, // 한줄 여부
    contentPadding: PaddingValues = PaddingValues(), // Surface 와 TextField padding
    maxLines: Int = Int.MAX_VALUE // 최대 입력 가능 라인
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(key1 = autoFocus) {
        if (autoFocus) {
            focusRequester.requestFocus()
        }
    }

    HarooSurface(
        modifier = modifier,
        shape = shape,
        color = color,
        alpha = alpha,
        contentColor = contentColor,
        contentAlignment = Alignment.CenterStart,
        border = border,
        contentPadding = contentPadding
    ) {

        CompositionLocalProvider(
            LocalContentColor provides contentColor,
            LocalTextSelectionColors provides TextSelectionColors(
                handleColor = selectionColor,
                backgroundColor = selectionColor.copy(alpha = 0.3f)
            )
        ) {
            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                textStyle = MaterialTheme.typography.body2.copy(
                    color = contentColor
                ),
                value = value,
                onValueChange = onValueChange,
                readOnly = enabled.not(),
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                singleLine = singleLine,
                maxLines = maxLines,
                decorationBox = { innerTextField ->
                    if (value.isEmpty()) Text(text = placeHolder)
                    innerTextField()
                },
                cursorBrush = SolidColor(contentColor)
            )
        }
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