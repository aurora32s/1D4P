package com.core.model.feature

data class ImageUiModel(
    override val id: Long?,
    val imageUrl: String
): Model(id, CellType.IMAGE)