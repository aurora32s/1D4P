package com.core.domain.post

import androidx.paging.PagingData
import androidx.paging.map
import com.core.data.post.PostRepository
import com.core.model.domain.Posts
import com.core.model.domain.toPosts
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * 월별 기록 paging 요청 use case
 */
class GetPostPageByMonthUseCase @Inject constructor(
    private val postRepository: PostRepository
) {
    operator fun invoke(): Flow<PagingData<Posts>> {
        return postRepository.getPostPaging().map { it.map { post -> post.toPosts() } }
    }
}