package com.core.model.data

import com.core.model.database.ImageEntity
import com.core.model.datastore.ImageData

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

fun ImageEntity.toSource() = ImageSource(
    id = id,
    imageUrl = imageUrl
)

fun ImageData.toSource() = ImageSource(
    id = id,
    imageUrl = imageUrl.toString()
)