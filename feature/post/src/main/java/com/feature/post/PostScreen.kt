package com.feature.post

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.core.ui.gallery.GalleryContainer

@Composable
fun PostScreen(
    postViewModel: PostViewModel = hiltViewModel()
) {
    val images = postViewModel.images.collectAsLazyPagingItems()
    val selectedImages = postViewModel.selectedImages.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        GalleryContainer(
            images = images,
            selectedImages = selectedImages.value,
            limit = PostViewModel.IMAGE_SELECT_LIMIT,
            onImageSelect = postViewModel::selectImage
        )
    }
}