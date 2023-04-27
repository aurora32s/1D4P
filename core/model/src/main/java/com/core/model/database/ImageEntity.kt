package com.core.model.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE

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
    ],
    primaryKeys = ["image_id","post_id"]
)
data class ImageEntity(
    @ColumnInfo(name = "image_id", index = true)
    val id: Long,
    @ColumnInfo(name = "post_id", index = true)
    val postId: Long,
    @ColumnInfo(name = "image_url")
    val imageUrl: String
)