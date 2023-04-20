package com.core.domain.post

import com.core.data.post.PostRepository
import com.core.model.domain.Post
import com.core.model.domain.toPost
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 월별 기록 요청 use case
 */
class GetPostByMonthUseCase @Inject constructor(
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(year: Int, month: Int): Result<List<Post>> = try {
        val result = postRepository.getPosts(year, month).getOrThrow()
        Result.success(result.map { it.toPost() })
    } catch (exception: Exception) {
        Result.failure(exception)
    }
}