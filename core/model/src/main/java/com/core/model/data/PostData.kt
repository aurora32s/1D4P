package com.core.model.data

/**
 * Post 정보
 */
data class PostData(
    val id: String,
    val year: Int,
    val month: Int,
    val day: Int,
    val content: String,
    val images: List<ImageData>
)
