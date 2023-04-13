package com.core.model.feature

import com.core.model.domain.Tag

/**
 * tag 정보를 위한 ui model
 */
data class TagUiModel(
    override val id: Long? = null,
    val name: String
) : Model(id, CellType.TAG)

fun TagUiModel.toTag() = Tag(id = id, name = name)