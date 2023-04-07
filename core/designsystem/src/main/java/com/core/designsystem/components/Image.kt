package com.core.designsystem.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
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

/**
 * 하루 이미지 4개를 가로 리스트 로 보여 주는 Component
 */
@Composable
fun HarooImageList(
    modifier: Modifier = Modifier,
    images: List<Image>, // 이미지 정보 리스트
    padding: Dp = 0.dp // 이미지 사이 넓이
) {
    Layout(
        modifier = modifier.fillMaxWidth(),
        content = {
            images.forEach {
                HarooImage(imageType = it)
            }
        }
    ) { measureables, constraints ->
        val children = measureables.size
        val space = padding.toPx().toInt()
        val imageSize = (constraints.maxWidth - space * 2 * (children)) / children
        val imageConstraints = Constraints(
            minWidth = imageSize,
            minHeight = imageSize,
            maxWidth = imageSize,
            maxHeight = imageSize
        )
        val placeable = measureables.map {
            it.measure(imageConstraints)
        }

        layout(
            width = constraints.maxWidth,
            height = constraints.maxHeight
        ) {
            var x = space
            placeable.forEach {
                it.placeRelative(
                    x = x,
                    y = 0
                )
                x += it.width + space * 2
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
            imageType = Image.AsyncImage(image),
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
    imageType: Image // 이미지 정보
) {
    HarooSurface(
        modifier = modifier,
        shape = shape,
        elevation = elevation
    ) {
        when (imageType) {
            is Image.BitmapImage -> Image(
                bitmap = imageType.bitmap,
                contentDescription = imageType.contentDescription,
                contentScale = contentScale
            )
            is Image.ResourceImage -> Image(
                painter = painterResource(id = imageType.resource),
                contentDescription = imageType.contentDescription,
                contentScale = contentScale
            )
            is Image.AsyncImage -> AsyncImage(
                model = imageType.image.imageUrl,
                contentDescription = imageType.contentDescription,
                contentScale = contentScale
            )
        }
    }
}

sealed interface Image {
    data class BitmapImage(
        val bitmap: ImageBitmap,
        val contentDescription: String? = null
    ) : Image

    data class ResourceImage(
        @DrawableRes val resource: Int,
        val contentDescription: String? = null
    ) : Image

    data class AsyncImage(
        val image: ImageUiModel,
        val contentDescription: String? = null
    ) : Image
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
                imageType = Image.ResourceImage(R.drawable.test)
            )

            HarooImageList(
                images = listOf(
                    Image.ResourceImage(R.drawable.test),
                    Image.ResourceImage(R.drawable.test),
                    Image.ResourceImage(R.drawable.test),
                    Image.ResourceImage(R.drawable.test)
                ),
                padding = 8.dp
            )
        }
    }
}

