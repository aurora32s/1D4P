package com.core.model.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

/**
 * Image Table
 */
@Entity(
    tableName = "image",
    foreignKeys = [
        ForeignKey(
            entity = PostEntity::class,
            parentColumns = ["id"],
            childColumns = ["post_id"],
            onDelete = CASCADE
        )
    ]
)
data class ImageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: String,
    @ColumnInfo(name = "post_id")
    val postId: String,
    @ColumnInfo(name = "image_url")
    val imageUrl: String
)
