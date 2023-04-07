package com.core.ui.gallery

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.BottomDrawerState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.core.designsystem.components.BackAndRightButtonHeader
import com.core.designsystem.components.SelectableImage
import com.core.designsystem.theme.HarooTheme
import com.core.model.feature.ImageUiModel

private const val galleryColumn = 3


@ExperimentalMaterialApi
@Composable
fun DrawerGalleryContainer(
    drawerState: BottomDrawerState,
    images: LazyPagingItems<ImageUiModel>,
    selectedImages: List<ImageUiModel>,
    limit: Int,
    space: Dp = 0.dp,
    onClose: () -> Unit,
    onImageSelect: (List<ImageUiModel>) -> Unit
) {
    val tempSelectedImages = remember(selectedImages) { mutableStateOf(selectedImages) }

    LaunchedEffect(key1 = drawerState.isAnimationRunning) {
        if (drawerState.isExpanded) {
            tempSelectedImages.value = selectedImages
        }
    }

    GalleryContainer(
        images = images,
        selectedImages = tempSelectedImages.value,
        limit = limit,
        space = space,
        onClose = onClose,
        onSelectFinish = { onImageSelect(tempSelectedImages.value) },
        onImageSelect = { image ->
            if (image in tempSelectedImages.value) {
                tempSelectedImages.value = tempSelectedImages.value.filterNot { image == it }
            } else {
                tempSelectedImages.value += image
            }
        }
    )
}

@Composable
fun GalleryContainer(
    images: LazyPagingItems<ImageUiModel>, // gallery image 정보
    selectedImages: List<ImageUiModel>, // 선택된 이미지 정보
    limit: Int = 0, // 선택할 수 이미지 최대 개수
    space: Dp = 0.dp, // 각 이미지 사이 space
    onClose: () -> Unit, // 닫기 버튼 클릭 event
    onSelectFinish: () -> Unit, // 선택 완료 버튼 클릭 event
    onImageSelect: (ImageUiModel) -> Unit = {} // 이미지 선택 event
) {
    Column(
        modifier = Modifier.background(
            Brush.linearGradient(HarooTheme.colors.interactiveBackground)
        )
    ) {
        BackAndRightButtonHeader(
            title = "갤러리",
            showButtonFlag = selectedImages.isNotEmpty(),
            onBackPressed = onClose,
            onClick = onSelectFinish,
            content = {
                Text(text = "${selectedImages.size}개 선택")
            }
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(galleryColumn),
            contentPadding = PaddingValues(space),
            horizontalArrangement = Arrangement.spacedBy(space),
            verticalArrangement = Arrangement.spacedBy(space)
        ) {
            items(count = images.itemCount) { index ->
                images[index]?.let { image ->
                    SelectableImage(
                        modifier = Modifier.aspectRatio(1f),
                        image = image,
                        shape = RectangleShape,
                        borderWidth = 2.dp,
                        isSelected = image in selectedImages,
                        enableSelectFlag = selectedImages.size < limit,
                        onClick = onImageSelect
                    )
                }
            }
        }
    }
}
