package com.core.data.model

import com.core.database.model.ImageEntity
import com.core.datastore.model.ImageData

/**
 * datasource Image 정보
 */
data class ImageSource(
    val id: Long?, // 이미지 id
    val imageUrl: String // 이지미 url
)

internal fun ImageSource.toImageEntity(postId: Long) = ImageEntity(
    id = id,
    postId = postId,
    imageUrl = imageUrl
)

internal fun ImageEntity.toImageSource() = ImageSource(
    id = id,
    imageUrl = imageUrl
)

internal fun ImageData.toImageSource() = ImageSource(
    id = id,
    imageUrl = imageUrl.toString()
)