package com.feature.post

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
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
import com.core.ui.manager.SnackbarManager
import com.core.ui.tag.TagContainer
import com.feature.post.ui.Dimens
import java.time.LocalDate

@Composable
fun PostRoute(
    onBackPressed: () -> Unit,
    postViewModel: PostViewModel = hiltViewModel(),
    snackbarManager: SnackbarManager = SnackbarManager
) {
    val postStateHolder = rememberPostScreenState(
        postViewModel = postViewModel,
        onBackPressed = onBackPressed
    )

    LaunchedEffect(key1 = Unit) {
        postViewModel.postUiEvent.collect {
            when (it) {
                is PostUiEvent.Fail.DuplicateTagName -> snackbarManager.showMessage(it.messageId)
            }
        }
    }

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
            modifier = Modifier.fillMaxSize()
        ) {
            PostBody(
                interactionSource = postStateHolder.interactionSource,
                closeTegTextField = postStateHolder::closeTagTextField,
                onBaseClick = postStateHolder::onBaseBtnClick,
                onBackPressed = postStateHolder::onBackPressed,
                postType = postStateHolder.postType,
                isEditMode = postStateHolder.editable,
                date = postStateHolder.date,
                selectedImages = postStateHolder.selectedImages,
                onRemoveSelectedImage = postStateHolder::removeImage,
                content = postStateHolder.content,
                setContent = postStateHolder::setContent
            )
            PostScreenBottomAppBar(
                isEditMode = postStateHolder.editable,
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
    isEditMode: Boolean,
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
            isEditMode = isEditMode,
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
    isEditMode: Boolean,
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
                isEditMode = { isEditMode },
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
        enabled = isEditMode,
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
    isEditMode: Boolean,
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
        isEditMode = isEditMode,
        showTagTextFieldFlag = showTagTextFieldFlag,
        tags = tags,
        onAddTag = onAddTags,
        onRemoveTag = onRemoveTag,
        showTagTextField = showTagTextField
    )

    if (isEditMode.not()) {
        Spacer(modifier = Modifier.height(Dimens.spaceBetweenTagAndGalleryList))
    } else {
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
}