package com.core.model.data

import com.core.model.database.PostEntity

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

/**
 * 하나의 PostSource page
 */
data class PostSources(
    val year: Int, val month: Int,
    val posts: List<PostSource>
)

fun PostSource.toPostEntity() = PostEntity(
    id = id,
    year = year,
    month = month,
    day = day,
    content = content
)