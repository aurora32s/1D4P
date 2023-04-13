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
import androidx.hilt.navigation.compose.hiltViewModel
import com.core.designsystem.components.BackAndRightButtonHeader
import com.core.designsystem.components.HarooBottomDrawer
import com.core.designsystem.components.HarooTextField
import com.core.designsystem.components.RemovableImage
import com.core.designsystem.modifiers.onInteraction
import com.core.designsystem.theme.HarooTheme
import com.core.designsystem.util.getString
import com.core.ui.date.YearMonthDayText
import com.core.ui.gallery.DrawerGalleryContainer
import com.core.ui.gallery.GalleryListContainer
import com.core.ui.image.AsyncImageLazyRow
import com.core.ui.tag.TagContainer
import com.feature.post.ui.Dimens

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
                space = Dimens.galleryContainerSpace,
                onClose = postStateHolder::bottomDrawerHide,
                onImageSelect = postStateHolder::addSelectedImage
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.linearGradient(HarooTheme.colors.interactiveBackground))
        ) {
            PostBody(postStateHolder = postStateHolder)
            PostScreenBottomAppBar(postStateHolder = postStateHolder)
        }
    }
}

/**
 * PostScreen 의 header / body / bottom 중 header 와 body
 */
@Composable
fun ColumnScope.PostBody(
    postStateHolder: PostScreenStateHolder
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
        PostScreenHeader(postStateHolder = postStateHolder)
        PostContent(postStateHolder = postStateHolder)
    }
}

/**
 * PostScreen 의 header / body / bottom 중 header
 */
@Composable
fun PostScreenHeader(
    postStateHolder: PostScreenStateHolder
) {
    BackAndRightButtonHeader(
        title = "글 작성",
        onBackPressed = { },
        onClick = postStateHolder::savePost
    ) {
        // TODO 새 글 작성 시에는 저장, 기존 글인 경우 수정정
        Text(text = getString(id = R.string.save_btn))
    }
}

/**
 * PostScreen 의 header / body / bottom 중 body
 */
@Composable
fun PostContent(
    postStateHolder: PostScreenStateHolder
) {
    // 날짜
    YearMonthDayText(
        modifier = Modifier.padding(Dimens.datePadding),
        date = postStateHolder.date
    )
    // 사용자 가 선택한 이미지 리스트
    AsyncImageLazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(Dimens.selectedImageListHeight),
        images = postStateHolder.selectedImages,
        space = Dimens.selectedImageListSpace,
        contentPadding = Dimens.selectedImageListPadding,
        content = { image ->
            RemovableImage(
                image = image,
                contentScale = ContentScale.FillHeight,
                onRemove = postStateHolder::removeImage
            )
        }
    )
    // 게시글 의 내용을 입력 하는 TextField
    HarooTextField(
        modifier = Modifier.padding(Dimens.contentPadding),
        value = postStateHolder.content,
        onValueChange = postStateHolder::setContent,
        autoFocus = true,
        alpha = Dimens.contentAlpha,
        placeHolder = getString(id = R.string.content_hint),
        singleLine = false,
        contentPadding = Dimens.contentInnerPadding
    )
}

/**
 * PostScreen 의 header / body / bottom 중 bottom
 */
@Composable
fun PostScreenBottomAppBar(
    postStateHolder: PostScreenStateHolder
) {
    TagContainer(
        modifier = Modifier.padding(Dimens.tagPadding),
        showTagTextFieldFlag = postStateHolder.showTagTextFieldFlag,
        tags = postStateHolder.tags,
        onAddTag = postStateHolder::addTag,
        onRemoveTag = postStateHolder::removeTag,
        showTagTextField = postStateHolder::showTagTextField
    )
    BottomAppBar(
        modifier = Modifier
            .padding(Dimens.bottomAppBarPadding),
        backgroundColor = Color.Transparent
    ) {
        GalleryListContainer(
            images = postStateHolder.images,
            space = Dimens.galleryListContainerSpace,
            selectedImages = postStateHolder.selectedImages,
            limit = PostViewModel.IMAGE_SELECT_LIMIT,
            onClickAddButton = postStateHolder::showBottomDrawer,
            onImageSelect = postStateHolder::setSelectedImage
        )
    }
}