package com.core.domain.model

import com.core.data.model.ImageSource

data class Image(
    val id: Long?,
    val imageUrl: String
)

internal fun ImageSource.toImage() = Image(
    id = id,
    imageUrl = imageUrl
)