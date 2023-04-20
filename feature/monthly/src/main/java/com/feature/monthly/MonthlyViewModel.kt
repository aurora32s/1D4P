package com.feature.monthly

import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.domain.post.GetPostByMonthUseCase
import com.core.domain.post.RemovePostUseCase
import com.core.model.feature.PostUiModel
import com.core.model.feature.toPostUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
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

    private val _monthlyUiEvent = MutableSharedFlow<MonthlyUiEvent>()
    val monthlyUiEvent: SharedFlow<MonthlyUiEvent> = _monthlyUiEvent.asSharedFlow()

    internal val date = YearMonthArg(savedStateHandle)

    private val _posts = MutableStateFlow<List<PostUiModel>>(emptyList())
    val posts: StateFlow<List<PostUiModel>> = _posts.asStateFlow()

    fun getPost() {
        viewModelScope.launch {
            getPostByMonthUseCase(
                date.currentYearMonth.year,
                date.currentYearMonth.monthValue
            ).onSuccess {
                val result = it.map { post -> post.toPostUiModel() }
                _posts.value = result
            }.onFailure {
                _monthlyUiEvent.emit(MonthlyUiEvent.Fail.GetPost)
            }
        }
    }

    fun removePost(postUiModel: PostUiModel) {
        viewModelScope.launch {
            postUiModel.id?.let { postId ->
                removePostUseCase(postId)
                    .onSuccess {
                        _monthlyUiEvent.emit(MonthlyUiEvent.Success.RemovePost)
                        _posts.value = _posts.value.filterNot { it == postUiModel }
                    }
                    .onFailure { _monthlyUiEvent.emit(MonthlyUiEvent.Fail.RemovePost) }
            }
        }
    }
}

sealed interface MonthlyUiEvent {
    sealed class Fail(
        @StringRes val messageId: Int
    ) : MonthlyUiEvent {
        object GetPost : Fail(R.string.fail_to_get_post)
        object RemovePost : Fail(R.string.fail_to_remove_post)
    }

    sealed class Success(
        @StringRes val messageId: Int
    ) : MonthlyUiEvent {
        object RemovePost : Success(R.string.success_to_remove_post)
    }
}