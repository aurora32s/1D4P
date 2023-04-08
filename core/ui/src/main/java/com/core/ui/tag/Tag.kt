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
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.core.designsystem.components.HarooButton
import com.core.designsystem.components.HarooChip
import com.core.designsystem.components.HarooDivider
import com.core.designsystem.components.HarooTextField
import com.core.designsystem.theme.AllForMemoryTheme
import com.core.model.feature.TagUiModel
import com.google.accompanist.flowlayout.FlowRow


@Composable
fun TagContainer(
    modifier: Modifier = Modifier,
    tags: List<TagUiModel>,
    tagSpace: Dp = 2.dp,
    onAddTag: (String) -> Unit,
    onRemoveTag: (TagUiModel) -> Unit
) {
    val showTagTextField = remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            mainAxisSpacing = tagSpace,
            crossAxisSpacing = tagSpace
        ) {
            tags.forEach { tag ->
                TagChip(name = "#${tag.name}", onClick = { onRemoveTag(tag) })
            }
            TagChip(
                name = "+태그추가",
                onClick = { showTagTextField.value = true }
            )
        }
        AnimatedVisibility(
            visible = showTagTextField.value,
            enter = expandVertically(
                animationSpec = tween(durationMillis = 250, easing = LinearEasing)
            )
        ) {
            TagTextField(
                onAddTag = onAddTag
            )
        }
    }
}

@Composable
fun TagChip(
    name: String,
    onClick: () -> Unit
) {
    HarooChip(
        onClick = onClick,
        border = null,
        alpha = 0.25f
    ) { Text(text = name) }
}

@Composable
fun TagTextField(
    modifier: Modifier = Modifier,
    onAddTag: (String) -> Unit
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
                autoFocus = true,
                color = Color.Transparent,
                placeHolder = "태그 입력...",
                contentPadding = PaddingValues(horizontal = 8.dp)
            )
            HarooButton(
                alpha = 0f,
                onClick = {
                    onAddTag(tag.value)
                    tag.value = ""
                }
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
        TagContainer(
            tags = listOf(
                TagUiModel(name = "자유여행"),
                TagUiModel(name = "친구와함께"),
                TagUiModel(name = "제주도"),
                TagUiModel(name = "test1"),
                TagUiModel(name = "test2"),
                TagUiModel(name = "test3"),
                TagUiModel(name = "test4"),
                TagUiModel(name = "test5"),
                TagUiModel(name = "test6")
            ),
            onAddTag = { },
            onRemoveTag = {}
        )
    }
}