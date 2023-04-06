package com.core.model.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.core.model.data.TagSource

@Entity(
    tableName = "tag",
    foreignKeys = [
        ForeignKey(
            entity = PostEntity::class,
            parentColumns = ["id"],
            childColumns = ["post_id"],
            onDelete = CASCADE
        )
    ]
)
data class TagEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long?,
    @ColumnInfo(name = "post_id", index = true)
    val postId: Long?,
    val name: String
)

fun TagEntity.toTag() = TagSource(
    id = id,
    name = name
)