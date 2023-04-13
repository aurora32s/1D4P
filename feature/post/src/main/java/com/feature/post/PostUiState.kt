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
@OptIn(ExperimentalLayoutApi::class)
fun rememberPostScreenState(
    year: Int, month: Int, day: Int,
    postViewModel: PostViewModel,
    bottomDrawerState: HarooBottomDrawerState = rememberHarooBottomDrawerState(),
    focusManager: FocusManager = LocalFocusManager.current,
    isImeVisible: Boolean = WindowInsets.isImeVisible,
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

    return remember(year, month, day, postViewModel, bottomDrawerState) {
        PostScreenStateHolder(
            year, month, day,
            postViewModel = postViewModel,
            images = images,
            _selectedImages = selectedImages,
            _tags = tags,
            _content = content,
            bottomDrawerState = bottomDrawerState,
            focusManager = focusManager,
            isImeVisible = isImeVisible,
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
    private val year: Int,
    private val month: Int,
    private val day: Int,
    private val postViewModel: PostViewModel,
    val images: LazyPagingItems<ImageUiModel>,
    private val _selectedImages: State<List<ImageUiModel>>,
    private val _tags: State<List<TagUiModel>>,
    private val _content: State<String>,
    val bottomDrawerState: HarooBottomDrawerState,
    val focusManager: FocusManager,
    val isImeVisible: Boolean,
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
    val date: LocalDate
        get() = LocalDate.of(year, month, day)

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
    fun CollectImeVisible() {
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

    fun removeImage(imageUiModel: ImageUiModel) {
        postViewModel.removeImage(imageUiModel)
    }

    fun setContent(content: String) {
        postViewModel.setContent(content)
    }

    fun addTag(tag: String) {
        postViewModel.addTag(tag)
    }

    fun removeTag(tagUiModel: TagUiModel) {
        postViewModel.removeTag(tagUiModel)
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
            PostType.EDIT -> postViewModel.savePost(year, month, day)
        }
    }

    /**
     * back button 클릭
     */
    fun onBackPressed() {
        when (postType) {
            PostType.EDIT -> {
                postViewModel.getPost(year, month, day)
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