package com.feature.post

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.core.designsystem.theme.HarooTheme
import com.core.ui.gallery.DrawerGalleryContainer
import com.core.ui.gallery.GalleryContainer
import com.core.ui.gallery.GalleryListContainer
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun PostScreen(
    postViewModel: PostViewModel = hiltViewModel()
) {
    val images = postViewModel.images.collectAsLazyPagingItems()
    val selectedImages = postViewModel.selectedImages.collectAsState()

//    val modalSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()
    val bottomDrawerState = rememberBottomDrawerState(initialValue = BottomDrawerValue.Closed)

    BackHandler(enabled = bottomDrawerState.isOpen) {
        coroutineScope.launch {
            bottomDrawerState.close()
        }
    }

    BottomDrawer(
        drawerState = bottomDrawerState,
        drawerContent = {
            DrawerGalleryContainer(
                drawerState = bottomDrawerState,
                images = images,
                selectedImages = selectedImages.value,
                limit = PostViewModel.IMAGE_SELECT_LIMIT,
                onClose = {
                    coroutineScope.launch { bottomDrawerState.close() }
                },
                onImageSelect = {
                    postViewModel.setImages(it)
                    coroutineScope.launch { bottomDrawerState.close() }
                }
            )
        },
        gesturesEnabled = false,
        scrimColor = HarooTheme.colors.dim
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
                    onClickAddButton = {
                        coroutineScope.launch { bottomDrawerState.expand() }
                    },
                    onImageSelect = postViewModel::selectImage
                )
            }
        }
    }
}