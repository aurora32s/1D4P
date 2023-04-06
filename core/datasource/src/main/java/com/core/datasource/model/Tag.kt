package com.core.datasource.model

import com.core.database.model.TagEntity

/**
 * datasource tag 정보
 */
data class Tag(
    val id: Long?, // tag id
    val name: String // tag 내용
)

internal fun Tag.toTagEntity(postId: Long) = TagEntity(
    id = id,
    postId = postId,
    name = name
)

internal fun TagEntity.toTag() = Tag(
    id = id,
    name = name
)