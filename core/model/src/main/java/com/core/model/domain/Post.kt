package com.core.model.domain

import com.core.model.data.PostSource

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