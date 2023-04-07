package com.feature.post

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.core.designsystem.theme.HarooTheme
import com.core.ui.gallery.GalleryContainer
import com.core.ui.gallery.GalleryListContainer

@ExperimentalMaterialApi
@Composable
fun PostScreen(
    postViewModel: PostViewModel = hiltViewModel()
) {
    val images = postViewModel.images.collectAsLazyPagingItems()
    val selectedImages = postViewModel.selectedImages.collectAsState()

    val modalSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    ModalBottomSheetLayout(
        sheetState = modalSheetState,
        sheetContent = {
            GalleryContainer(
                images = images,
                selectedImages = selectedImages.value,
                limit = PostViewModel.IMAGE_SELECT_LIMIT,
                onImageSelect = postViewModel::selectImage
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.linearGradient(HarooTheme.colors.interactiveBackground))
        ) {
            Column(modifier = Modifier.weight(1f)) {

            }
            BottomAppBar(
                modifier = Modifier.padding(vertical = 12.dp),
                backgroundColor = Color.Transparent
            ) {
                GalleryListContainer(
                    modifier = Modifier,
                    images = images,
                    selectedImages = selectedImages.value,
                    limit = PostViewModel.IMAGE_SELECT_LIMIT,
                    onImageSelect = postViewModel::selectImage
                )
            }
        }
    }
}