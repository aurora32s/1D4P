package com.core.model.feature

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
