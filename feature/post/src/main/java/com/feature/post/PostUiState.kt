package com.feature.post

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.runtime.*
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.core.designsystem.components.HarooBottomDrawerState
import com.core.designsystem.components.rememberHarooBottomDrawerState
import com.core.model.feature.ImageUiModel
import com.core.model.feature.TagUiModel
import java.time.LocalDate

@Composable
fun rememberPostScreenState(
    postViewModel: PostViewModel,
    bottomDrawerState: HarooBottomDrawerState = rememberHarooBottomDrawerState(),
    focusManager: FocusManager = LocalFocusManager.current,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onBackPressed: () -> Unit
): PostScreenStateHolder {
    val images = postViewModel.images.collectAsLazyPagingItems()
    val selectedImages = postViewModel.selectedImages.collectAsState()
    val tags = postViewModel.tags.collectAsState()
    val content = postViewModel.content.collectAsState()
    val pressFlag = interactionSource.collectIsPressedAsState()

    val postId = postViewModel.postId.collectAsState()
    val isEditMode = postViewModel.isEditMode.collectAsState()

    return remember(postViewModel, bottomDrawerState) {
        PostScreenStateHolder(
            date = postViewModel.date.currentDate,
            postViewModel = postViewModel,
            images = images,
            _selectedImages = selectedImages,
            _tags = tags,
            _content = content,
            bottomDrawerState = bottomDrawerState,
            focusManager = focusManager,
            interactionSource = interactionSource,
            pressFlag = pressFlag,
            postId = postId,
            isEditMode = isEditMode,
            _showTagTextFieldFlag = mutableStateOf(false),
            _onBackPressed = onBackPressed
        )
    }
}

class PostScreenStateHolder(
    val date: LocalDate,
    val postViewModel: PostViewModel,
    val images: LazyPagingItems<ImageUiModel>,
    private val _selectedImages: State<List<ImageUiModel>>,
    private val _tags: State<List<TagUiModel>>,
    private val _content: State<String>,
    val bottomDrawerState: HarooBottomDrawerState,
    val focusManager: FocusManager,
    val interactionSource: MutableInteractionSource,
    val pressFlag: State<Boolean>,
    val postId: State<Long?>,
    val isEditMode: State<Boolean>,
    private val _showTagTextFieldFlag: MutableState<Boolean>,
    private val _onBackPressed: () -> Unit
) {
    val selectedImages: List<ImageUiModel>
        get() = _selectedImages.value
    val tags: List<TagUiModel>
        get() = _tags.value
    val content: String
        get() = _content.value
    val showTagTextFieldFlag: Boolean
        get() = _showTagTextFieldFlag.value

    val isBottomDrawer: State<Boolean>
        get() = bottomDrawerState.isShow
    val postType: PostType
        get() = when {
            postId.value != null && isEditMode.value -> PostType.EDIT
            postId.value != null -> PostType.SHOW
            else -> PostType.NEW
        }
    val editable: Boolean
        get() = postType != PostType.SHOW

    @Composable
    @OptIn(ExperimentalLayoutApi::class)
    fun CollectImeVisible(
        isImeVisible: Boolean = WindowInsets.isImeVisible
    ) {
        LaunchedEffect(key1 = isImeVisible) {
            if (isImeVisible.not() && _showTagTextFieldFlag.value) {
                _showTagTextFieldFlag.value = false
            }
        }
    }

    @Composable
    fun CollectPressFlag() {
        LaunchedEffect(key1 = pressFlag.value) {
            // 1. A, B 클릭 시
            if (pressFlag.value) {
                // 2. C, D focus lost
                clearFocus()
            }
        }
    }

    fun showBottomDrawer() {
        clearFocus()
        bottomDrawerState.show()
    }

    fun bottomDrawerHide() {
        bottomDrawerState.hide()
    }

    private fun clearFocus() {
        focusManager.clearFocus()
        closeTagTextField()
    }

    fun closeTagTextField() {
        if (_showTagTextFieldFlag.value) {
            _showTagTextFieldFlag.value = false
        }
    }

    fun addSelectedImage(images: List<ImageUiModel>) {
        postViewModel.setImages(images)
        bottomDrawerHide()
    }

    fun setSelectedImage(imageUiModel: ImageUiModel) {
        clearFocus()
        postViewModel.selectImage(imageUiModel)
    }

    fun showTagTextField() {
        _showTagTextFieldFlag.value = true
    }

    /**
     * header 의 기본 버튼 클릭
     */
    fun onBaseBtnClick() {
        when (postType) {
            PostType.SHOW -> postViewModel.toggleEditMode()
            PostType.NEW,
            PostType.EDIT -> postViewModel.savePost()
        }
    }

    /**
     * back button 클릭
     */
    fun onBackPressed() {
        when (postType) {
            PostType.EDIT -> {
                postViewModel.getPost()
                postViewModel.toggleEditMode()
            }

            PostType.NEW,
            PostType.SHOW -> _onBackPressed()
        }
    }
}

enum class PostType {
    NEW, SHOW, EDIT
}