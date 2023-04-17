package com.core.ui.image

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.core.designsystem.components.ImageType
import com.core.model.feature.ImageUiModel

@Composable
fun AsyncImageLazyRow(
    modifier: Modifier = Modifier,
    images: List<ImageUiModel>,
    space: Dp = 0.dp,
    contentPadding: PaddingValues = PaddingValues(),
    content: @Composable (ImageUiModel) -> Unit
) {
    AnimatedVisibility(visible = images.isNotEmpty()) {
        Row(
            modifier = modifier
                .padding(contentPadding)
                .horizontalScroll(
                    rememberScrollState()
                ),
            horizontalArrangement = Arrangement.spacedBy(space),
        ) {
            Spacer(modifier = Modifier.width(space))
            images.forEach {
                content(it)
            }
            Spacer(modifier = Modifier.width(space))
        }
    }
}

@Composable
fun AsyncImageList(
    modifier: Modifier = Modifier,
    images: List<ImageUiModel>,
    space: Dp = 0.dp,
    imageCount: Int = 1,
    contentPadding: Dp = 0.dp,
    content: @Composable (ImageType.AsyncImage) -> Unit
) {
    HarooImageList(
        modifier = modifier,
        images = images.map { ImageType.AsyncImage(image = it) },
        space = space,
        imageCount = imageCount,
        contentPadding = contentPadding,
        content = { content(it as ImageType.AsyncImage) }
    )
}

/**
 * 하루 이미지 4개를 가로 리스트 로 보여 주는 Component
 */
@Composable
fun HarooImageList(
    modifier: Modifier = Modifier,
    images: List<ImageType>, // 이미지 정보 리스트
    space: Dp = 0.dp, // 이미지 사이 넓이
    imageCount: Int = 1, // 화면에 보여줄 이미지 개수
    contentPadding: Dp = 0.dp,
    content: @Composable (ImageType) -> Unit
) {
    Layout(
        modifier = modifier.padding(contentPadding),
        content = {
            images.forEach { content(it) }
        }
    ) { measureables, constraints ->
        val spaceBy = space.toPx().toInt()
        val padding = contentPadding.toPx().toInt()

        val width = (constraints.maxWidth - (imageCount - 1) * spaceBy - padding) / imageCount
        val imageConstraints = Constraints(
            minWidth = width,
            minHeight = width,
            maxWidth = width,
            maxHeight = width
        )
        val placeable = measureables.map {
            it.measure(imageConstraints)
        }
        layout(
            width = constraints.maxWidth,
            height = width
        ) {
            var x = 0
            placeable.forEach {
                it.placeRelative(
                    x = x,
                    y = 0
                )
                x += it.width + spaceBy
            }
        }
    }
}

@Composable
fun HarooGridImages(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    images: List<ImageUiModel>, // 이미지 정보 리스트
    content: @Composable (ImageUiModel) -> Unit
) {
    Layout(
        modifier = modifier.clip(shape),
        content = { images.forEach { content(it) } }
    ) { measureables, constraints ->
        val imageCount = images.size
        val imagesWidth = if (imageCount == 1) constraints.maxWidth else (constraints.maxWidth / 2)
        val firstImage = measureables.getOrNull(0)?.measure(
            constraints.copy(
                minWidth = imagesWidth,
                maxWidth = imagesWidth,
                maxHeight = when (imageCount) {
                    1, 3, 4 -> imagesWidth / 2
                    else -> imagesWidth
                }
            )
        )
        val secondImage = measureables.getOrNull(1)?.measure(
            constraints.copy(
                minWidth = imagesWidth,
                maxWidth = imagesWidth,
                maxHeight = when (imageCount) {
                    3, 4 -> imagesWidth / 2
                    else -> imagesWidth
                }
            )
        )
        val thirdImage = measureables.getOrNull(2)?.measure(
            constraints.copy(
                minWidth = imagesWidth,
                maxWidth = imagesWidth,
                maxHeight = when (imageCount) {
                    3 -> imagesWidth
                    else -> imagesWidth / 2
                }
            )
        )
        val fourthImage = measureables.getOrNull(3)?.measure(
            constraints.copy(
                minWidth = imagesWidth, maxWidth = imagesWidth, maxHeight = imagesWidth / 2
            )
        )

        layout(
            width = constraints.maxWidth,
            height = constraints.maxWidth / 2
        ) {
            firstImage?.placeRelative(x = 0, y = 0)
            secondImage?.placeRelative(
                x = when (imageCount) {
                    3, 4 -> 0
                    else -> imagesWidth
                },
                y = when (imageCount) {
                    3, 4 -> imagesWidth / 2
                    else -> 0
                }
            )
            thirdImage?.placeRelative(x = imagesWidth, y = 0)
            fourthImage?.placeRelative(x = imagesWidth, y = imagesWidth / 2)
        }
    }
}