package com.feature.post

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.core.ui.gallery.GalleryContainer

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

    }
}