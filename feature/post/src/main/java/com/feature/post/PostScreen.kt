package com.feature.post

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.core.designsystem.components.BackAndRightButtonHeader
import com.core.designsystem.components.HarooBottomDrawer
import com.core.designsystem.components.rememberHarooBottomDrawerState
import com.core.designsystem.theme.HarooTheme
import com.core.ui.date.YearMonthDayText
import com.core.ui.gallery.DrawerGalleryContainer
import com.core.ui.gallery.GalleryListContainer
import com.core.ui.tag.TagContainer
import java.time.LocalDate

@Composable
fun PostScreen(
    postViewModel: PostViewModel = hiltViewModel()
) {
    val images = postViewModel.images.collectAsLazyPagingItems()
    val selectedImages = postViewModel.selectedImages.collectAsState()
    val tags = postViewModel.tags.collectAsState()
    val bottomDrawerState = rememberHarooBottomDrawerState()

    BackHandler(enabled = bottomDrawerState.isShow.value) {
        bottomDrawerState.hide()
    }

    HarooBottomDrawer(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding(),
        drawerState = bottomDrawerState,
        drawerContent = {
            DrawerGalleryContainer(
                images = images,
                selectedImages = selectedImages.value,
                limit = PostViewModel.IMAGE_SELECT_LIMIT,
                space = 2.dp,
                onClose = { bottomDrawerState.hide() },
                onImageSelect = {
                    postViewModel.setImages(it)
                    bottomDrawerState.hide()
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.linearGradient(HarooTheme.colors.interactiveBackground))
        ) {
            Column(modifier = Modifier.weight(1f)) {
                BackAndRightButtonHeader(
                    title = "글 작성",
                    onBackPressed = { },
                    onClick = { }
                ) {
                    // TODO 새 글 작성 시에는 저장, 기존 글인 경우 수정정
                    Text(text = "저장")
                }
                YearMonthDayText(
                    modifier = Modifier
                        .padding(top = 26.dp, start = 16.dp),
                    date = LocalDate.now()
                )
            }
            TagContainer(
                modifier = Modifier.padding(horizontal = 12.dp),
                tags = tags.value,
                onAddTag = postViewModel::addTag,
                onRemoveTag = postViewModel::removeTag
            )
            BottomAppBar(
                modifier = Modifier.padding(vertical = 12.dp),
                backgroundColor = Color.Transparent
            ) {
                GalleryListContainer(
                    modifier = Modifier,
                    images = images,
                    selectedImages = selectedImages.value,
                    limit = PostViewModel.IMAGE_SELECT_LIMIT,
                    onClickAddButton = { bottomDrawerState.show() },
                    onImageSelect = postViewModel::selectImage
                )
            }
        }
    }
}
//}