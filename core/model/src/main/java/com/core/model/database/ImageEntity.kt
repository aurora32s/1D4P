package com.core.model.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.core.model.data.ImageSource

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
    val id: Long?,
    @ColumnInfo(name = "post_id", index = true)
    val postId: Long,
    @ColumnInfo(name = "image_url")
    val imageUrl: String
)

fun ImageEntity.toImageSource() = ImageSource(
    id = id,
    imageUrl = imageUrl
)