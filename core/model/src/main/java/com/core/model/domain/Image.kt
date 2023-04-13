package com.core.model.domain

import com.core.model.data.ImageSource

data class Image(
    val id: Long?,
    val imageUrl: String
)

fun Image.toSource() = ImageSource(
    id = id,
    imageUrl = imageUrl
)

fun ImageSource.toImage() = Image(
    id = id,
    imageUrl = imageUrl
)