package com.feature.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.core.domain.post.GetImagesUseCase
import com.core.model.domain.toImageUiModel
import com.core.model.feature.ImageUiModel
import com.core.model.feature.TagUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    getImagesUseCase: GetImagesUseCase
) : ViewModel() {

    val images = getImagesUseCase()
        .map { images ->
            images.map { it.toImageUiModel() }
        }.cachedIn(viewModelScope)

    private val _selectedImages = MutableStateFlow<List<ImageUiModel>>(emptyList())
    val selectedImages: StateFlow<List<ImageUiModel>> = _selectedImages.asStateFlow()

    private val _tags = MutableStateFlow<List<TagUiModel>>(emptyList())
    val tags: StateFlow<List<TagUiModel>> = _tags.asStateFlow()

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
    }

    fun addTag(name: String) {
        // 빈칸을 입력한 경우
        if (name.isBlank()) return
        // 동일한 이름의 tag 가 이미 존재 하는 경우
        if (_tags.value.any { it.name == name }) return
        _tags.value += TagUiModel(name = name)
    }

    fun removeTag(tagUiModel: TagUiModel) {
        _tags.value = _tags.value.filterNot { it == tagUiModel }
    }

    companion object {
        const val IMAGE_SELECT_LIMIT = 4
    }
}