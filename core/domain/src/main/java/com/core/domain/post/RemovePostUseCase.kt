package com.core.domain.post

import com.core.data.post.PostRepository
import javax.inject.Inject

class RemovePostUseCase @Inject constructor(
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(postId: Long) {
        postRepository.removePost(postId)
    }
}