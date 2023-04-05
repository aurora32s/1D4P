package com.core.datasource.model

import com.core.database.model.TagEntity

/**
 * datasource tag 정보
 */
data class Tag(
    val id: Int?, // tag id
    val name: String // tag 내용
)

internal fun Tag.toTagEntity(postId: Int) = TagEntity(
    id = id,
    postId = postId,
    name = name
)

internal fun TagEntity.toTag() = Tag(
    id = id,
    name = name
)