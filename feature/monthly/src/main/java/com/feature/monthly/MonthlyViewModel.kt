package com.feature.monthly

import androidx.lifecycle.SavedStateHandle
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
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class MonthlyViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getPostByMonthUseCase: GetPostByMonthUseCase,
    private val removePostUseCase: RemovePostUseCase
) : ViewModel() {

    internal val date = YearMonthArg(savedStateHandle)

    init {
        viewModelScope.launch {
            _posts.value = getPostByMonthUseCase(
                date.currentYearMonth.year,
                date.currentYearMonth.monthValue
            ).map { it.toPostUiModel() }
        }
    }

    private val _posts = MutableStateFlow<List<PostUiModel>>(emptyList())
    val posts: StateFlow<List<PostUiModel>> = _posts.asStateFlow()

    fun removePost(postUiModel: PostUiModel) {
        viewModelScope.launch {
            postUiModel.id?.let { postId ->
                removePostUseCase(postId)
                _posts.value = _posts.value.filterNot { it == postUiModel }
            }
        }
    }
}