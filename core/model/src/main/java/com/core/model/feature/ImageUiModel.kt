package com.core.model.feature

data class ImageUiModel(
    override val id: String,
    val imageUrl: String
): Model(id, CellType.IMAGE)
