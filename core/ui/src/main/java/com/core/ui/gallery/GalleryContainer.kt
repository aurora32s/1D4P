package com.core.ui.gallery

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImage
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
                    image = it,
                    isSelected = it in selectedImages,
                    onClick = onImageSelect
                )
            }
        }
    }
}

@Composable
fun SelectableImage(
    image: ImageUiModel, // 이미지
    isSelected: Boolean, // 선택 여부
    onClick: (ImageUiModel) -> Unit
) {
    AsyncImage(model = image.imageUrl, contentDescription = image.id)
}