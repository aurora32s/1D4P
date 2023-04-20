package com.core.domain.post

import com.core.data.post.PostRepository
import com.core.model.domain.Post
import com.core.model.domain.toPost
import javax.inject.Inject

/**
 * 일별 기록 요청 use case
 */
class GetPostByDateUseCase @Inject constructor(
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(year: Int, month: Int, day: Int): Result<Post?> = try {
        val result = postRepository.getPost(year, month, day).getOrThrow()
        Result.success(result?.toPost())
    } catch (exception: Exception) {
        Result.failure(exception)
    }
}