package com.core.domain.post

import com.core.data.post.PostRepository
import com.core.model.domain.Image
import com.core.model.domain.Post
import com.core.model.domain.Tag
import com.core.model.domain.toSource
import javax.inject.Inject

/**
 * 게시글 추가 use case
 */
class AddPostUseCase @Inject constructor(
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(
        post: Post,
        removeImages: List<Image>,
        removeTags: List<Tag>
    ): Long {
        return postRepository.addPost(
            post.toSource(),
            removeImages.map { it.toSource() },
            removeTags = removeTags.map { it.toSource() })
    }
}