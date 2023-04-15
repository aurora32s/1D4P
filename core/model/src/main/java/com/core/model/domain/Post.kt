package com.core.model.domain

import com.core.model.data.PostSource
import com.core.model.data.PostSources

/**
 * domain Layer: Post 정보
 */
data class Post(
    val id: Long?, // post id
    val year: Int,
    val month: Int,
    val day: Int,
    val content: String?,
    val images: List<Image>,
    val tags: List<Tag>
)

/**
 * Post 한 page 단위
 */
data class Posts(
    val year: Int, val month: Int,
    val posts: List<Post>
)

fun Post.toSource() = PostSource(
    id = id,
    year = year,
    month = month,
    day = day,
    content = content,
    images = images.map { it.toSource() },
    tags = tags.map { it.toSource() }
)

fun PostSource.toPost() = Post(
    id = id,
    year = year,
    month = month,
    day = day,
    content = content,
    images = images.map { it.toImage() },
    tags = tags.map { it.toTag() }
)

fun PostSources.toPosts() = Posts(
    year = year, month = month,
    posts = posts.map { it.toPost() }
)