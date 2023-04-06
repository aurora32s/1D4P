package com.core.model.data

import com.core.model.database.ImageEntity
import com.core.model.domain.Image

/**
 * datasource Image 정보
 */
data class ImageSource(
    val id: Long?, // 이미지 id
    val imageUrl: String // 이지미 url
)

fun ImageSource.toImageEntity(postId: Long) = ImageEntity(
    id = id,
    postId = postId,
    imageUrl = imageUrl
)

fun ImageSource.toImage() = Image(
    id = id,
    imageUrl = imageUrl
)