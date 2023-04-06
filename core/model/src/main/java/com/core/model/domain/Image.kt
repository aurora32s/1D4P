package com.core.model.domain

import com.core.model.feature.ImageUiModel

data class Image(
    val id: Long?,
    val imageUrl: String
)

fun Image.toImageUiModel() = ImageUiModel(
    id = id,
    imageUrl = imageUrl
)