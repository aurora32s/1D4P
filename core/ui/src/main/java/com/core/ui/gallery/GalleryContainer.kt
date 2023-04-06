package com.core.ui.gallery

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.paging.compose.LazyPagingItems
import com.core.designsystem.components.SelectableImage
import com.core.model.feature.ImageUiModel

private const val galleryColumn = 3

@Composable
fun GalleryContainer(
    images: LazyPagingItems<ImageUiModel>, // gallery image 정보
    selectedImages: List<ImageUiModel>, // 선택된 이미지 정보
    onImageSelect: (ImageUiModel) -> Unit // 이미지 선택 event
) {
    LazyVerticalGrid(columns = GridCells.Fixed(galleryColumn)) {
        items(count = images.itemCount) { index ->
            images[index]?.let {
                SelectableImage(
                    modifier = Modifier.aspectRatio(1f),
                    image = it,
                    shape = RectangleShape,
                    isSelected = it in selectedImages,
                    onClick = onImageSelect
                )
            }
        }
    }
}