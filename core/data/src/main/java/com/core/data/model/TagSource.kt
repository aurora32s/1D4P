package com.core.data.model

import com.core.database.model.TagEntity

/**
 * datasource tag 정보
 */
data class TagSource(
    val id: Long?, // tag id
    val name: String // tag 내용
)

internal fun TagSource.toTagEntity(postId: Long) = TagEntity(
    id = id,
    postId = postId,
    name = name
)

internal fun TagEntity.toTag() = TagSource(
    id = id,
    name = name
)