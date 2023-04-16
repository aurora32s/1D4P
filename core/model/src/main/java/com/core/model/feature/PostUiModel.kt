package com.core.model.feature

import com.core.model.domain.Post
import com.core.model.domain.Posts
import java.time.LocalDate
import java.time.YearMonth

/**
 * Post Ui 정보
 */
data class PostUiModel(
    override val id: Long?,
    val date: LocalDate,
    val content: String,
    val images: List<ImageUiModel>,
    val tags: List<TagUiModel>
) : Model(id, CellType.POST)

data class PostsUiModel(
    val date: YearMonth,
    val posts: List<PostUiModel>
) : Model(null, CellType.POSTS)

fun Post.toPostUiModel() = PostUiModel(
    id = id,
    date = LocalDate.of(year, month, day),
    content = content ?: "",
    images = images.map { image -> image.toImageUiModel() },
    tags = tags.map { tag -> tag.toTagUiModel() }
)

fun Posts.toPostsUiModel() = PostsUiModel(
    date = YearMonth.of(year, month),
    posts = posts.map { it.toPostUiModel() }
)