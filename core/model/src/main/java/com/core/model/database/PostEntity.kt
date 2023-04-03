package com.core.model.database

/**
 * Post Table
 */
data class PostEntity(
    val id: String,
    val year: Int,
    val month: Int,
    val day: Int,
    val content: String?
)
