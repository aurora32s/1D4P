package com.core.model.data

import com.core.model.database.TagEntity

/**
 * datasource tag 정보
 */
data class TagSource(
    val id: Long?, // tag id
    val name: String // tag 내용
)

fun TagSource.toTagEntity(postId: Long) = TagEntity(
    id = id,
    postId = postId,
    name = name
)
