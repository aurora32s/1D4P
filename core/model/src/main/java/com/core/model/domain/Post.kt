package com.core.model.domain

/**
 * Post 정보
 */
data class Post(
    val id: String,
    val year: Int,
    val month: Int,
    val day: Int,
    val content: String,
    val images: List<Image>
)
