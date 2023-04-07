package com.feature.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.core.domain.post.GetImagesUseCase
import com.core.model.domain.toImageUiModel
import com.core.model.feature.ImageUiModel
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

    fun selectImage(imageUiModel: ImageUiModel) {
        if (imageUiModel in _selectedImages.value) {
            _selectedImages.value = _selectedImages.value.filterNot { it == imageUiModel }
        } else if (_selectedImages.value.size < IMAGE_SELECT_LIMIT) {
            _selectedImages.value = _selectedImages.value + imageUiModel
        }
    }

    companion object {
        const val IMAGE_SELECT_LIMIT = 4
    }
}