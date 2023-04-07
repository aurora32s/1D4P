package com.core.ui.gallery

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.core.designsystem.components.HarooButton
import com.core.designsystem.components.SelectableImage
import com.core.model.feature.ImageUiModel

private const val galleryColumn = 3

@Composable
fun GalleryContainer(
    images: LazyPagingItems<ImageUiModel>, // gallery image 정보
    selectedImages: List<ImageUiModel>, // 선택된 이미지 정보
    limit: Int, // 선택할 수 이미지 최대 개수
    onImageSelect: (ImageUiModel) -> Unit // 이미지 선택 event
) {
    Column {
        Row {
            if (selectedImages.isNotEmpty()) {
                HarooButton(onClick = {}) {
                    Text(text = "${selectedImages.size} 선택")
                }
            }
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(galleryColumn),
            contentPadding = PaddingValues(1.dp),
            horizontalArrangement = Arrangement.spacedBy(1.dp),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            items(count = images.itemCount) { index ->
                images[index]?.let {
                    SelectableImage(
                        modifier = Modifier.aspectRatio(1f),
                        image = it,
                        shape = RectangleShape,
                        selectedIndex = selectedImages.indexOf(it),
                        enableSelectFlag = selectedImages.size < limit,
                        onClick = onImageSelect
                    )
                }
            }
        }
    }
}