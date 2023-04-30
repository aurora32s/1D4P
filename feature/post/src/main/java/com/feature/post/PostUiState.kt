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
import com.core.ui.manager.SnackbarManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun rememberPostScreenState(
    postViewModel: PostViewModel,
    bottomDrawerState: HarooBottomDrawerState = rememberHarooBottomDrawerState(),
    focusManager: FocusManager = LocalFocusManager.current,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onBackPressed: () -> Unit,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    snackbarManager: SnackbarManager = SnackbarManager
): PostScreenStateHolder {
    val images = postViewModel.images.collectAsLazyPagingItems()
    val selectedImages = postViewModel.selectedImages.collectAsState()
    val tags = postViewModel.tags.collectAsState()
    val content = postViewModel.content.collectAsState()
    val pressFlag = interactionSource.collectIsPressedAsState()

    val postId = postViewModel.postId.collectAsState()
    val isEditMode = postViewModel.isEditMode.collectAsState()

    return remember(postViewModel, bottomDrawerState, coroutineScope, snackbarManager) {
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
            _onBackPressed = onBackPressed,
            coroutineScope = coroutineScope,
            snackbarManager = snackbarManager
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
    private val _onBackPressed: () -> Unit,
    coroutineScope: CoroutineScope,
    snackbarManager: SnackbarManager
) {
    init {
        coroutineScope.launch {
            postViewModel.getPost()
            postViewModel.postUiEvent.collect {
                when (it) {
                    is PostUiEvent.Fail.DuplicateTagName -> snackbarManager.showMessage(it.messageId)
                    is PostUiEvent.Fail.NeedImageMoreOne -> snackbarManager.showMessage(it.messageId)
                    is PostUiEvent.Fail.NeedContent -> snackbarManager.showMessage(it.messageId)
                    is PostUiEvent.Fail.SavePost -> snackbarManager.showMessage(it.messageId)
                    is PostUiEvent.Success.SavePost -> snackbarManager.showMessage(it.messageId)
                    is PostUiEvent.Fail.GetPost -> {
                        snackbarManager.showMessage(it.messageId)
                        onBackPressed()
                    }

                    is PostUiEvent.Fail.RemovePost -> snackbarManager.showMessage(it.messageId)
                    is PostUiEvent.Success.RemovePost -> snackbarManager.showMessage(it.messageId)
                }
            }
        }
    }

    val selectedImages: List<ImageUiModel>
        get() = _selectedImages.value
    val tags: List<TagUiModel>
        get() = _tags.value
    val content: String
        get() = _content.value
    val showTagTextFieldFlag: Boolean
        get() = _showTagTextFieldFlag.value && isEditMode.value

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