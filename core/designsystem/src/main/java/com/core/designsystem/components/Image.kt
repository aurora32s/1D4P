package com.core.designsystem.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.core.designsystem.R
import com.core.designsystem.modifiers.noRippleClickable
import com.core.designsystem.theme.AllForMemoryTheme
import com.core.designsystem.theme.HarooTheme
import com.core.model.feature.ImageUiModel


@Composable
fun RemovableImage(
    modifier: Modifier = Modifier,
    isEditMode: () -> Boolean = { true },
    image: ImageUiModel,
    shape: Shape = MaterialTheme.shapes.medium,
    elevation: Dp = 0.dp,
    contentScale: ContentScale = ContentScale.Crop,
    onRemove: (ImageUiModel) -> Unit
) {
    Layout(
        modifier = modifier,
        content = {
            HarooImage(
                modifier = Modifier.layoutId("Image"),
                imageType = ImageType.AsyncImage(image),
                shape = shape,
                contentScale = contentScale,
                elevation = elevation
            )
            HarooButton(
                modifier = Modifier.layoutId("RemoveBtn"),
                onClick = { onRemove(image) },
                backgroundColor = HarooTheme.colors.dim,
                border = null,
                alpha = 0.5f,
                contentPadding = PaddingValues(2.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "remove",
                    tint = HarooTheme.colors.text
                )
            }
        }
    ) { measureables, constraints ->
        val imagePlaceable = measureables.find { it.layoutId == "Image" }?.measure(constraints)
        val imageWidth = imagePlaceable?.width ?: constraints.minWidth

        layout(
            imageWidth,
            constraints.maxHeight
        ) {
            imagePlaceable?.placeRelative(x = 0, y = 0)
            if (isEditMode()) {
                val removeButtonSize = (constraints.maxHeight / 5).coerceIn(0, 25.dp.toPx().toInt())
                val removePlaceable = measureables.find { it.layoutId == "RemoveBtn" }?.measure(
                    Constraints(
                        minWidth = removeButtonSize, maxWidth = removeButtonSize,
                        minHeight = removeButtonSize, maxHeight = removeButtonSize
                    )
                )
                removePlaceable?.placeRelative(
                    x = imageWidth - removeButtonSize - 4,
                    y = 4
                )
            }
        }
    }
}

@Composable
fun SelectableImage(
    modifier: Modifier = Modifier,
    image: ImageUiModel,
    shape: Shape = MaterialTheme.shapes.medium,
    borderWidth: Dp = 1.dp,
    selectedColor: Color = HarooTheme.colors.brand,
    isSelected: Boolean,
    enableSelectFlag: Boolean = false,
    onClick: (ImageUiModel) -> Unit
) {
    Box(
        modifier = modifier
            .noRippleClickable(
                role = Role.Button,
                onClick = { onClick(image) },
                enabled = enableSelectFlag || isSelected
            )
            .then(
                if (isSelected) Modifier.border(
                    borderWidth, selectedColor, shape = shape
                ) else Modifier
            )
    ) {
        if (isSelected) {
            HarooSurface(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(1f),
                shape = shape,
                color = HarooTheme.colors.dim,
                alpha = 0.5f
            ) {
                Icon(
                    imageVector = Icons.Rounded.CheckCircle,
                    contentDescription = "select",
                    tint = selectedColor
                )
            }
        } else if (enableSelectFlag.not()) {
            HarooSurface(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(1f),
                shape = shape,
                color = HarooTheme.colors.uiBackground,
                alpha = 0.4f
            ) {}
        }
        HarooImage(
            imageType = ImageType.AsyncImage(image),
            shape = shape,
            elevation = 0.dp
        )
    }
}

/**
 * 기본 Image Component
 */
@Composable
fun HarooImage(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium, // 이미지 모양
    elevation: Dp = 2.dp, // 그림자 크기
    contentScale: ContentScale = ContentScale.Crop,
    imageType: ImageType // 이미지 정보
) {
    HarooSurface(
        modifier = modifier,
        shape = shape,
        elevation = elevation
    ) {
        when (imageType) {
            is ImageType.BitmapImage -> Image(
                bitmap = imageType.bitmap,
                contentDescription = imageType.contentDescription,
                contentScale = contentScale
            )
            is ImageType.ResourceImage -> Image(
                painter = painterResource(id = imageType.resource),
                contentDescription = imageType.contentDescription,
                contentScale = contentScale
            )
            is ImageType.AsyncImage -> AsyncImage(
                model = imageType.image.imageUrl,
                contentDescription = imageType.contentDescription,
                contentScale = contentScale
            )
        }
    }
}

sealed interface ImageType {
    data class BitmapImage(
        val bitmap: ImageBitmap,
        val contentDescription: String? = null
    ) : ImageType

    data class ResourceImage(
        @DrawableRes val resource: Int,
        val contentDescription: String? = null
    ) : ImageType

    data class AsyncImage(
        val image: ImageUiModel,
        val contentDescription: String? = null
    ) : ImageType
}

@Preview
@Composable
fun ImagePreview() {
    AllForMemoryTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(HarooTheme.colors.interactiveBackground)
                )
        ) {
            HarooImage(
                modifier = Modifier
                    .padding(12.dp)
                    .size(120.dp),
                imageType = ImageType.ResourceImage(R.drawable.test)
            )
        }
    }
}

