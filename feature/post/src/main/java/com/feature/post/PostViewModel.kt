package com.feature.post

import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.core.domain.post.AddPostUseCase
import com.core.domain.post.GetImagesUseCase
import com.core.domain.post.GetPostByDateUseCase
import com.core.domain.post.RemovePostUseCase
import com.core.model.domain.Post
import com.core.model.feature.ImageUiModel
import com.core.model.feature.TagUiModel
import com.core.model.feature.toImage
import com.core.model.feature.toImageUiModel
import com.core.model.feature.toTag
import com.core.model.feature.toTagUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getImagesUseCase: GetImagesUseCase,
    private val addPostUseCase: AddPostUseCase,
    private val getPostByDateUseCase: GetPostByDateUseCase,
    private val removePostUseCase: RemovePostUseCase
) : ViewModel() {
    internal val date = DateArg(savedStateHandle)

    private val _postUiEvent = MutableSharedFlow<PostUiEvent>()
    val postUiEvent: SharedFlow<PostUiEvent> = _postUiEvent.asSharedFlow()

    private val _postId = MutableStateFlow<Long?>(null)
    val postId: StateFlow<Long?> = _postId.asStateFlow()
    private val _isEditMode = MutableStateFlow(false)
    val isEditMode: StateFlow<Boolean> = _isEditMode.asStateFlow()

    val images = getImagesUseCase()
        .map { images ->
            images.map { it.toImageUiModel() }
        }.cachedIn(viewModelScope)

    private val _selectedImages = MutableStateFlow<List<ImageUiModel>>(emptyList())
    val selectedImages: StateFlow<List<ImageUiModel>> = _selectedImages.asStateFlow()
    private val removeImages = mutableListOf<ImageUiModel>()

    private val _tags = MutableStateFlow<List<TagUiModel>>(emptyList())
    val tags: StateFlow<List<TagUiModel>> = _tags.asStateFlow()
    private val removeTags = mutableListOf<TagUiModel>()

    private val _content = MutableStateFlow("")
    val content: StateFlow<String> = _content.asStateFlow()

    /**
     * 기존 Post 요청
     */
    fun getPost() {
        viewModelScope.launch {
            getPostByDateUseCase(
                date.year, date.month, date.day
            ).onSuccess {
                it?.let { post ->
                    _content.value = post.content ?: ""
                    _selectedImages.value = post.images.map { it.toImageUiModel() }
                    _tags.value = post.tags.map { it.toTagUiModel() }

                    _postId.value = post.id ?: -1
                }
            }.onFailure {
                _postUiEvent.emit(PostUiEvent.Fail.GetPost)
            }
        }
    }

    /**
     * 신규 post 저장
     */
    fun savePost() {
        viewModelScope.launch {
            // 유효성 검사 1. 이미지 1개 이상
            if (_selectedImages.value.isEmpty()) {
                _postUiEvent.emit(PostUiEvent.Fail.NeedImageMoreOne)
                return@launch
            }
            // 유효성 검사 2. 내용
            if (_content.value.isBlank()) {
                _postUiEvent.emit(PostUiEvent.Fail.NeedContent)
                return@launch
            }

            addPostUseCase(
                Post(
                    id = _postId.value,
                    year = date.year, month = date.month, day = date.day,
                    content = _content.value,
                    images = _selectedImages.value.map { it.toImage() },
                    tags = _tags.value.map { it.toTag() }
                ),
                removeImages = removeImages.map { it.toImage() },
                removeTags = removeTags.map { it.toTag() }
            ).onSuccess {
                _postId.value = it
                _isEditMode.value = false
                _postUiEvent.emit(PostUiEvent.Success.SavePost)
            }.onFailure {
                _postUiEvent.emit(PostUiEvent.Fail.SavePost)
            }
        }
    }

    fun selectImage(imageUiModel: ImageUiModel) {
        if (imageUiModel in _selectedImages.value) {
            _selectedImages.value = _selectedImages.value.filterNot { it == imageUiModel }
        } else if (_selectedImages.value.size < IMAGE_SELECT_LIMIT) {
            _selectedImages.value = _selectedImages.value + imageUiModel
        }
    }

    fun setImages(selectedImages: List<ImageUiModel>) {
        _selectedImages.value = selectedImages
    }

    fun removeImage(imageUiModel: ImageUiModel) {
        _selectedImages.value = _selectedImages.value.filterNot { it == imageUiModel }
        if (imageUiModel.id != null) removeImages.add(imageUiModel)
    }

    fun addTag(name: String) {
        viewModelScope.launch {
            // 빈칸을 입력한 경우
            if (name.isBlank()) return@launch
            // 동일한 이름의 tag 가 이미 존재 하는 경우
            if (_tags.value.any { it.name == name }) {
                _postUiEvent.emit(PostUiEvent.Fail.DuplicateTagName)
                return@launch
            }
            _tags.value += TagUiModel(name = name)
        }
    }

    fun removeTag(tagUiModel: TagUiModel) {
        _tags.value = _tags.value.filterNot { it == tagUiModel }
        if (tagUiModel.id != null) removeTags.add(tagUiModel)
    }

    fun setContent(content: String) {
        _content.value = content
    }

    fun toggleEditMode() {
        _isEditMode.value = _isEditMode.value.not()
    }

    fun removePost() {
        viewModelScope.launch {
            _postId.value?.let {
                removePostUseCase(it)
            }
        }
    }

    companion object {
        const val IMAGE_SELECT_LIMIT = 4
    }
}

sealed interface PostUiEvent {
    sealed class Fail(
        @StringRes val messageId: Int
    ) : PostUiEvent {
        object DuplicateTagName : Fail(R.string.duplicate_tag_name)
        object NeedImageMoreOne : Fail(R.string.need_image_one_more)
        object NeedContent : Fail(R.string.need_content)
        object SavePost : Fail(R.string.fail_to_save_post)
        object GetPost : Fail(R.string.fail_to_get_post)
    }

    sealed class Success(
        @StringRes val messageId: Int
    ) : PostUiEvent {
        object SavePost : Success(R.string.success_to_save_post)
    }
}