package com.core.model.feature

import com.core.model.domain.Post

/**
 * Post Ui 정보
 */
data class PostUiModel(
    override val id: Long?,
    val year: Int,
    val month: Int,
    val day: Int,
    val content: String,
    val images: List<ImageUiModel>
) : Model(id, CellType.POST)

fun Post.toPostUiModel() = PostUiModel(
    id = id,
    year = year,
    month = month,
    day = day,
    content = content ?: "",
    images = images.map { image -> image.toImageUiModel() }
)