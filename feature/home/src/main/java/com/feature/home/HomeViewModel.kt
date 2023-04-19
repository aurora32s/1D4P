package com.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.core.domain.post.GetPostPageByMonthUseCase
import com.core.domain.post.RemovePostUseCase
import com.core.model.feature.PostUiModel
import com.core.model.feature.toPostsUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    getPostPageByMonthUseCase: GetPostPageByMonthUseCase,
    private val removePostUseCase: RemovePostUseCase
) : ViewModel() {

    private val _homeUiEvent = MutableStateFlow<HomeUiEvent>(HomeUiEvent.Initialized)
    val homeUiEvent: StateFlow<HomeUiEvent> = _homeUiEvent.asStateFlow()

    private val initDay = YearMonth.now()
    val posts = getPostPageByMonthUseCase(
        initDay.year, initDay.monthValue
    ).map { it.map { post -> post.toPostsUiModel() } }
        .cachedIn(viewModelScope)

    fun removePost(postUiModel: PostUiModel) {
        viewModelScope.launch {
            _homeUiEvent.value = HomeUiEvent.Loading
            postUiModel.id?.let {
                removePostUseCase(it)
                _homeUiEvent.value = HomeUiEvent.Success.RemovePost
            }
        }
    }
}

sealed interface HomeUiEvent {
    object Initialized : HomeUiEvent
    object Loading : HomeUiEvent
    sealed interface Success : HomeUiEvent {
        // Post 제거 성공 event
        object RemovePost : Success
    }
}