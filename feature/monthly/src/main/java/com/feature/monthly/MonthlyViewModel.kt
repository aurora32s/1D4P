package com.feature.monthly

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.domain.post.GetPostByMonthUseCase
import com.core.domain.post.RemovePostUseCase
import com.core.model.feature.PostUiModel
import com.core.model.feature.toPostUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MonthlyViewModel @Inject constructor(
    private val getPostByMonthUseCase: GetPostByMonthUseCase,
    private val removePostUseCase: RemovePostUseCase
) : ViewModel() {

    private val _posts = MutableStateFlow<List<PostUiModel>>(emptyList())
    val posts: StateFlow<List<PostUiModel>> = _posts.asStateFlow()

    fun init(year: Int, month: Int) {
        viewModelScope.launch {
            _posts.value = getPostByMonthUseCase(year, month).map { it.toPostUiModel() }
            println(_posts.value)
        }
    }

    fun removePost(postUiModel: PostUiModel) {
        viewModelScope.launch {
            postUiModel.id?.let { postId ->
                removePostUseCase(postId)
                _posts.value = _posts.value.filterNot { it == postUiModel }
            }
        }
    }
}