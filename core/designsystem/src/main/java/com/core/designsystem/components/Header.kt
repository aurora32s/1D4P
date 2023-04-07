package com.core.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.core.designsystem.theme.HarooTheme

@Composable
fun BackAndRightButtonHeader(
    modifier: Modifier = Modifier,
    title: String = "",
    showButtonFlag: Boolean = false,
    onBackPressed: () -> Unit, // back button click event
    onClick: () -> Unit, // 오른쪽 버튼 클릭 event
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 4.dp, bottom = 4.dp, end = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CompositionLocalProvider(
            LocalContentColor provides HarooTheme.colors.text
        ) {
            IconButton(onClick = onBackPressed) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "back"
                )
            }
            Text(
                modifier = Modifier.weight(1f),
                text = title,
                style = MaterialTheme.typography.subtitle1
            )
            if (showButtonFlag) {
                HarooButton(
                    border = BorderStroke(
                        width = 1.dp,
                        color = HarooTheme.colors.uiBackground
                    ),
                    onClick = onClick,
                    alpha = 0f,
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    content = content
                )
            }
        }
    }
}