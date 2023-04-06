package com.core.data.model

import com.core.database.model.PostEntity

/**
 * data source Post 정보
 */
data class PostSource(
    val id: Long?, // post id
    val year: Int, // 연도
    val month: Int, // 월
    val day: Int, // 일
    val content: String?, // 내용
    val images: List<ImageSource>, // 이미지
    val tags: List<TagSource> // 태그
)

internal fun PostSource.toPostEntity() = PostEntity(
    id = id,
    year = year,
    month = month,
    day = day,
    content = content
)