package com.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Post Table
 */
@Entity(tableName = "post")
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long?,
    val year: Int,
    val month: Int,
    val day: Int,
    val content: String?
)
