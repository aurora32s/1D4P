package com.feature.home

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.core.domain.post.GetPostPageByMonthUseCase
import com.core.domain.post.RemovePostUseCase
import com.core.model.feature.PostUiModel
import com.core.model.feature.toPostsUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    getPostPageByMonthUseCase: GetPostPageByMonthUseCase,
    private val removePostUseCase: RemovePostUseCase
) : ViewModel() {
    private val _homeUiEvent = MutableSharedFlow<HomeUiEvent>()
    val homeUiEvent: SharedFlow<HomeUiEvent> = _homeUiEvent.asSharedFlow()

    val posts = getPostPageByMonthUseCase(PAGE_SIZE)
        .map { it.map { post -> post.toPostsUiModel() } }
        .cachedIn(viewModelScope)

    fun removePost(postUiModel: PostUiModel) {
        viewModelScope.launch {
            postUiModel.id?.let {
                removePostUseCase(it)
                    .onSuccess { _homeUiEvent.emit(HomeUiEvent.Success.RemovePost) }
                    .onFailure { _homeUiEvent.emit(HomeUiEvent.Fail.RemovePost) }
            }
        }
    }

    var recentVisibleItem = YearMonth.now()
    var recentVisibleItemOffset: Int? = null

    companion object {
        const val PAGE_SIZE = 10
    }
}

sealed interface HomeUiEvent {
    sealed class Fail(
        @StringRes val messageId: Int
    ) : HomeUiEvent {
        object RemovePost : Fail(R.string.fail_to_remove_post)
    }

    sealed class Success(
        @StringRes val messageId: Int
    ) : HomeUiEvent {
        object RemovePost : Success(R.string.success_to_remove_post)
    }
}