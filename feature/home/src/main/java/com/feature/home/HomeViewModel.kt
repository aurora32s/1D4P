package com.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.core.domain.post.GetPostPageByMonthUseCase
import com.core.model.feature.toPostUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    getPostPageByMonthUseCase: GetPostPageByMonthUseCase
) : ViewModel() {
    private val initDay = YearMonth.now()
    val posts = getPostPageByMonthUseCase(
        initDay.year, initDay.monthValue
    ).map { it.map { post -> post.toPostUiModel() } }
        .cachedIn(viewModelScope)
}