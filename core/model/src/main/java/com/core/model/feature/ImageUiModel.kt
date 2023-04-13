package com.core.model.feature

import com.core.model.domain.Image

data class ImageUiModel(
    override val id: Long?,
    val imageUrl: String
): Model(id, CellType.IMAGE)

fun Image.toImageUiModel() = ImageUiModel(
    id = id,
    imageUrl = imageUrl
)