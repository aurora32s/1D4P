package com.feature.post

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.core.designsystem.components.BackAndRightButtonHeader
import com.core.designsystem.components.HarooBottomDrawer
import com.core.designsystem.components.HarooTextField
import com.core.designsystem.components.RemovableImage
import com.core.designsystem.modifiers.onInteraction
import com.core.designsystem.theme.HarooTheme
import com.core.ui.date.YearMonthDayText
import com.core.ui.gallery.DrawerGalleryContainer
import com.core.ui.gallery.GalleryListContainer
import com.core.ui.image.AsyncImageLazyRow
import com.core.ui.tag.TagContainer
import java.time.LocalDate

@Composable
fun PostScreen(
    postViewModel: PostViewModel = hiltViewModel()
) {
    val postStateHolder = rememberPostScreenState(postViewModel = postViewModel)
    PostScreen(postStateHolder = postStateHolder)
}

@Composable
fun PostScreen(
    postStateHolder: PostScreenStateHolder
) {
    // BottomDrawer 가 열려 있다면 Back 버튼 클릭 시, Drawer hide
    BackHandler(enabled = postStateHolder.isBottomDrawer.value) {
        postStateHolder.bottomDrawerHide()
    }

    postStateHolder.CollectImeVisible()
    postStateHolder.CollectPressFlag()

    HarooBottomDrawer(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding(),
        drawerState = postStateHolder.bottomDrawerState,
        drawerContent = {
            DrawerGalleryContainer(
                images = postStateHolder.images,
                selectedImages = postStateHolder.selectedImages,
                limit = PostViewModel.IMAGE_SELECT_LIMIT,
                space = 2.dp,
                onClose = postStateHolder::bottomDrawerHide,
                onImageSelect = postStateHolder::addSelectedImage
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        HarooTheme.colors.interactiveBackground
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .onInteraction(postStateHolder.interactionSource)
                    .onFocusChanged {
                        // 4. C 클릭 시, D가 열려 있는 경우 close
                        if (it.isFocused)
                            postStateHolder.closeTagTextField()
                    }
            ) {
                BackAndRightButtonHeader(
                    title = "글 작성",
                    onBackPressed = { },
                    onClick = postStateHolder::savePost
                ) {
                    // TODO 새 글 작성 시에는 저장, 기존 글인 경우 수정정
                    Text(text = "저장")
                }
                YearMonthDayText(
                    modifier = Modifier
                        .padding(top = 26.dp, start = 16.dp),
                    date = LocalDate.now()
                )
                AsyncImageLazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(210.dp),
                    images = postStateHolder.selectedImages,
                    space = 4.dp,
                    contentPadding = 12.dp,
                    content = { image ->
                        RemovableImage(
                            image = image,
                            contentScale = ContentScale.FillHeight,
                            onRemove = postStateHolder::removeImage
                        )
                    }
                )
                HarooTextField(
                    modifier = Modifier.padding(
                        horizontal = 12.dp, vertical = 8.dp
                    ),
                    value = postStateHolder.content,
                    onValueChange = postStateHolder::setContent,
                    autoFocus = true,
                    alpha = 0.1f,
                    placeHolder = "내용을 입력해주세요...",
                    singleLine = false,
                    contentPadding = PaddingValues(16.dp)
                )
            }
            TagContainer(
                modifier = Modifier.padding(horizontal = 12.dp),
                showTagTextFieldFlag = postStateHolder.showTagTextFieldFlag,
                tags = postStateHolder.tags,
                onAddTag = postStateHolder::addTag,
                onRemoveTag = postStateHolder::removeTag,
                showTagTextField = postStateHolder::showTagTextField
            )
            BottomAppBar(
                modifier = Modifier
                    .padding(vertical = 12.dp),
                backgroundColor = Color.Transparent
            ) {
                GalleryListContainer(
                    images = postStateHolder.images,
                    space = 8.dp,
                    selectedImages = postStateHolder.selectedImages,
                    limit = PostViewModel.IMAGE_SELECT_LIMIT,
                    onClickAddButton = postStateHolder::showBottomDrawer,
                    onImageSelect = postStateHolder::setSelectedImage
                )
            }
        }
    }
}
//}