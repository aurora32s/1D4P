package com.core.designsystem.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
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
    selectedIndex: Int,
    enableSelectFlag: Boolean = false,
    onClick: (ImageUiModel) -> Unit
) {
    Box(
        modifier = modifier
            .clickable(
                role = Role.Button,
                onClick = { onClick(image) },
                enabled = enableSelectFlag || selectedIndex >= 0
            )
            .then(
                if (selectedIndex >= 0) Modifier.border(
                    3.dp,
                    HarooTheme.colors.brand,
                    shape = shape
                ) else Modifier
            )
    ) {
        if (selectedIndex >= 0) {
            HarooSurface(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(1f),
                shape = shape,
                color = HarooTheme.colors.dim,
                alpha = 0.5f
            ) {
                HarooChip(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(12.dp),
                    backgroundColor = HarooTheme.colors.brand,
                    alpha = 1f
                ) {
                    Text(text = "${selectedIndex + 1}")
                }
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

