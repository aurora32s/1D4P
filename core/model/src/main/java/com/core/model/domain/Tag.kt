package com.core.model.domain

import com.core.model.data.TagSource

/**
 * domain Layer: Tag 정보
 */
data class Tag(
    val id: Long?,
    val name: String
)

fun Tag.toSource() = TagSource(
    id = id,
    name = name
)