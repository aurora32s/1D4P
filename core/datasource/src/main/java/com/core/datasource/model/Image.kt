package com.core.datasource.model

import com.core.database.model.ImageEntity

/**
 * datasource Image 정보
 */
data class Image(
    val id: Int?, // 이미지 id
    val imageUrl: String // 이지미 url
)

internal fun Image.toImageEntity(postId: Int) = ImageEntity(
    id = id,
    postId = postId,
    imageUrl = imageUrl
)

internal fun ImageEntity.toImage() = Image(
    id = id,
    imageUrl = imageUrl
)