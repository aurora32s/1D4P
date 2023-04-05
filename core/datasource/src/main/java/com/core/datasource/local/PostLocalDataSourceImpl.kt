package com.core.datasource.local

import androidx.paging.PagingData
import com.core.database.dao.PostDao
import com.core.datasource.PostDatasource
import com.core.datasource.model.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostLocalDataSourceImpl @Inject constructor(
    private val postDao: PostDao
) : PostDatasource {
    override suspend fun addPost(post: Post): Int {
        val postId = postDao.insertPost(post.toPostEntity())
        postDao.insertImages(post.images.map { it.toImageEntity(postId) })
        postDao.insertTags(post.tags.map { it.toTagEntity(postId) })
        return postId
    }

    override suspend fun getPost(year: Int, month: Int, day: Int): Post? {
        val post = postDao.selectPostByDate(year, month, day)
        return post?.id?.let {
            val images = postDao.selectImagesByPost(it)
            val tags = postDao.selectTagsByPost(it)
            Post(
                id = it,
                year = post.year,
                month = post.month,
                day = post.day,
                content = post.content,
                images = images.map { image -> image.toImage() },
                tags = tags.map { tag -> tag.toTag() }
            )
        }
    }

    override suspend fun getPosts(year: Int, month: Int): List<Post> {
        val posts = postDao.selectPostByMonth(year, month)
        return posts.mapNotNull { post ->
            post.id?.let {
                val images = postDao.selectImagesByPost(it)
                val tags = postDao.selectTagsByPost(it)
                Post(
                    id = it,
                    year = post.year,
                    month = post.month,
                    day = post.day,
                    content = post.content,
                    images = images.map { image -> image.toImage() },
                    tags = tags.map { tag -> tag.toTag() }
                )
            }
        }
    }

    override fun getPostPaging(year: Int, month: Int): Flow<PagingData<Post>> {
        TODO("Not yet implemented")
    }
}