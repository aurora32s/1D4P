package com.core.ui.tag

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.core.designsystem.components.HarooButton
import com.core.designsystem.components.HarooDivider
import com.core.designsystem.components.HarooTextField
import com.core.designsystem.theme.AllForMemoryTheme
import com.core.model.feature.TagUiModel


@Composable
fun TagContainer(
    modifier: Modifier = Modifier,
    tags: List<TagUiModel>,
    onAddTag: (TagUiModel) -> Unit
) {
    val showTagTextField = remember { mutableStateOf(true) }

    Column {
        AnimatedVisibility(
            visible = showTagTextField.value,
            enter = expandVertically(
                animationSpec = tween(durationMillis = 250, easing = LinearEasing)
            )
        ) {
            TagTextField(onAddTag = onAddTag)
        }
    }
}

@Composable
fun TagTextField(
    modifier: Modifier = Modifier,
    onAddTag: (TagUiModel) -> Unit
) {
    val tag = remember { mutableStateOf("") }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            HarooTextField(
                modifier = Modifier.weight(1f),
                value = tag.value,
                onValueChange = { tag.value = it },
                color = Color.Transparent,
                placeHolder = "태그 입력...",
                contentPadding = PaddingValues(horizontal = 8.dp)
            )
            HarooButton(
                alpha = 0f,
                onClick = { onAddTag(TagUiModel(name = tag.value)) }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = "Add Tag"
                )
            }
        }
        HarooDivider()
    }
}

@Composable
@Preview(name = "basic tag text field")
fun TagTextFieldPreview() {
    AllForMemoryTheme {
        TagTextField(
            onAddTag = {}
        )
    }
}

@Composable
@Preview(name = "basic tag container")
fun TagContainerPreview() {
    AllForMemoryTheme {
        TagContainer(tags = emptyList(), onAddTag = {})
    }
}