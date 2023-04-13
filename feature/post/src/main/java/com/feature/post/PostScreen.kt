package com.feature.post

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import com.core.designsystem.components.HarooBottomDrawer
import com.core.designsystem.components.HarooButton
import com.core.designsystem.components.HarooTextField
import com.core.designsystem.components.RemovableImage
import com.core.designsystem.modifiers.onInteraction
import com.core.designsystem.theme.HarooTheme
import com.core.designsystem.util.getString
import com.core.model.feature.ImageUiModel
import com.core.model.feature.TagUiModel
import com.core.ui.date.YearMonthDayText
import com.core.ui.gallery.DrawerGalleryContainer
import com.core.ui.gallery.GalleryListContainer
import com.core.ui.image.AsyncImageLazyRow
import com.core.ui.tag.TagContainer
import com.feature.post.ui.Dimens
import java.time.LocalDate

@Composable
fun PostScreen(
    year: Int, month: Int, day: Int,
    postViewModel: PostViewModel = hiltViewModel()
) {
    val postStateHolder =
        rememberPostScreenState(year, month, day, postViewModel = postViewModel, onBackPressed = {})
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
            PostBody(
                interactionSource = postStateHolder.interactionSource,
                closeTegTextField = postStateHolder::closeTagTextField,
                onBaseClick = postStateHolder::onBaseBtnClick,
                onBackPressed = postStateHolder::onBackPressed,
                postType = postStateHolder.postType,
                date = postStateHolder.date,
                selectedImages = postStateHolder.selectedImages,
                onRemoveSelectedImage = postStateHolder::removeImage,
                content = postStateHolder.content,
                setContent = postStateHolder::setContent
            )
            PostScreenBottomAppBar(
                showTagTextFieldFlag = postStateHolder.showTagTextFieldFlag,
                tags = postStateHolder.tags,
                images = postStateHolder.images,
                selectedImages = postStateHolder.selectedImages,
                onAddTags = postStateHolder::addTag,
                onRemoveTag = postStateHolder::removeTag,
                showTagTextField = postStateHolder::showTagTextField,
                showBottomDrawer = postStateHolder::showBottomDrawer,
                onImageSelect = postStateHolder::setSelectedImage
            )
        }
    }
}

/**
 * PostScreen 의 header / body / bottom 중 header 와 body
 */
@Composable
fun ColumnScope.PostBody(
    interactionSource: MutableInteractionSource,
    closeTegTextField: () -> Unit,
    onBaseClick: () -> Unit,
    onBackPressed: () -> Unit,
    postType: PostType,
    date: LocalDate,
    selectedImages: List<ImageUiModel>,
    onRemoveSelectedImage: (ImageUiModel) -> Unit,
    content: String,
    setContent: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .onInteraction(interactionSource)
            .onFocusChanged {
                // 4. C 클릭 시, D가 열려 있는 경우 close
                if (it.isFocused) closeTegTextField()
            }
    ) {
        PostScreenHeader(
            postType = postType,
            onBackPressed = onBackPressed,
            onBaseClick = onBaseClick,
        )
        PostContent(
            date = date,
            selectedImages = selectedImages,
            onRemoveSelectedImage = onRemoveSelectedImage,
            content = content,
            setContent = setContent
        )
    }
}

/**
 * PostScreen 의 header / body / bottom 중 header
 */
@Composable
fun PostScreenHeader(
    modifier: Modifier = Modifier,
    postType: PostType,
    onBaseClick: () -> Unit,
    onBackPressed: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(Dimens.headerPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CompositionLocalProvider(
            LocalContentColor provides HarooTheme.colors.text
        ) {
            IconButton(onClick = onBackPressed) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "back"
                )
            }
            Text(
                modifier = Modifier.weight(1f),
                text = when (postType) {
                    PostType.NEW -> getString(id = R.string.title_new)
                    PostType.SHOW -> getString(id = R.string.title_show)
                    PostType.EDIT -> getString(id = R.string.title_edit)
                },
                style = MaterialTheme.typography.subtitle1
            )
            // 기본 button
            HarooButton(
                onClick = onBaseClick,
            ) {
                Text(
                    text = when (postType) {
                        PostType.SHOW -> getString(id = R.string.edit_btn)
                        else -> getString(id = R.string.save_btn)
                    }
                )
            }
            if (postType == PostType.SHOW) {
                // 기본 button
                HarooButton(
                    modifier = Modifier.padding(Dimens.headerExtraBtnPadding),
                    onClick = onBaseClick,
                ) {
                    Text(text = getString(id = R.string.del_btn))
                }
            }
        }
    }
}

/**
 * PostScreen 의 header / body / bottom 중 body
 */
@Composable
fun PostContent(
    date: LocalDate,
    selectedImages: List<ImageUiModel>,
    onRemoveSelectedImage: (ImageUiModel) -> Unit,
    content: String,
    setContent: (String) -> Unit
) {
    // 날짜
    YearMonthDayText(
        modifier = Modifier.padding(Dimens.datePadding),
        date = date
    )
    // 사용자 가 선택한 이미지 리스트
    AsyncImageLazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(Dimens.selectedImageListHeight),
        images = selectedImages,
        space = Dimens.selectedImageListSpace,
        contentPadding = Dimens.selectedImageListPadding,
        content = { image ->
            RemovableImage(
                image = image,
                contentScale = ContentScale.FillHeight,
                onRemove = onRemoveSelectedImage
            )
        }
    )
    // 게시글 의 내용을 입력 하는 TextField
    HarooTextField(
        modifier = Modifier.padding(Dimens.contentPadding),
        value = content,
        onValueChange = setContent,
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
    showTagTextFieldFlag: Boolean,
    tags: List<TagUiModel>,
    images: LazyPagingItems<ImageUiModel>,
    selectedImages: List<ImageUiModel>,
    onAddTags: (String) -> Unit,
    onRemoveTag: (TagUiModel) -> Unit,
    showTagTextField: () -> Unit,
    showBottomDrawer: () -> Unit,
    onImageSelect: (ImageUiModel) -> Unit
) {
    TagContainer(
        modifier = Modifier.padding(Dimens.tagPadding),
        showTagTextFieldFlag = showTagTextFieldFlag,
        tags = tags,
        onAddTag = onAddTags,
        onRemoveTag = onRemoveTag,
        showTagTextField = showTagTextField
    )
    BottomAppBar(
        modifier = Modifier
            .padding(Dimens.bottomAppBarPadding),
        backgroundColor = Color.Transparent
    ) {
        GalleryListContainer(
            images = images,
            space = Dimens.galleryListContainerSpace,
            selectedImages = selectedImages,
            limit = PostViewModel.IMAGE_SELECT_LIMIT,
            onClickAddButton = showBottomDrawer,
            onImageSelect = onImageSelect
        )
    }
}