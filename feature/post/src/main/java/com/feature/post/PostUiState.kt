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
    date: LocalDate = LocalDate.now(),
    postViewModel: PostViewModel,
    bottomDrawerState: HarooBottomDrawerState = rememberHarooBottomDrawerState(),
    focusManager: FocusManager = LocalFocusManager.current,
    isImeVisible: Boolean = WindowInsets.isImeVisible,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
): PostScreenStateHolder {
    val images = postViewModel.images.collectAsLazyPagingItems()
    val selectedImages = postViewModel.selectedImages.collectAsState()
    val tags = postViewModel.tags.collectAsState()
    val content = postViewModel.content.collectAsState()
    val pressFlag = interactionSource.collectIsPressedAsState()
    return remember(postViewModel, bottomDrawerState) {
        PostScreenStateHolder(
            date = date,
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
            _showTagTextFieldFlag = mutableStateOf(false)
        )
    }
}

class PostScreenStateHolder(
    val date: LocalDate,
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
    private val _showTagTextFieldFlag: MutableState<Boolean>
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

    fun savePost() {
        postViewModel.savePost()
    }
}