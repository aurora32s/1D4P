package com.feature.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.core.domain.post.GetImagesUseCase
import com.core.model.domain.toImageUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
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
}