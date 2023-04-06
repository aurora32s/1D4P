package com.core.datasource.local

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.core.database.dao.PostDao
import com.core.datasource.PostDatasource
import com.core.datasource.model.*
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PostLocalDataSourceImpl @Inject constructor(
    private val postDao: PostDao
) : PostDatasource {
    override suspend fun addPost(post: Post): Long = coroutineScope {
        val postId = postDao.insertPost(post.toPostEntity())
        launch { postDao.insertImages(post.images.map { it.toImageEntity(postId) }) }
        launch { postDao.insertTags(post.tags.map { it.toTagEntity(postId) }) }
        postId
    }

    override suspend fun getPost(year: Int, month: Int, day: Int): Post? = coroutineScope {
        val post = postDao.selectPostByDate(year, month, day)
        post?.id?.let {
            val images = async { postDao.selectImagesByPost(it) }
            val tags = async { postDao.selectTagsByPost(it) }
            Post(
                id = it,
                year = post.year,
                month = post.month,
                day = post.day,
                content = post.content,
                images = images.await().map { image -> image.toImage() },
                tags = tags.await().map { tag -> tag.toTag() }
            )
        }
    }

    override suspend fun getPosts(year: Int, month: Int): List<Post> = coroutineScope {
        val posts = postDao.selectPostByMonth(year, month)
        posts.map { post ->
            async {
                post.id?.let {
                    val images = async { postDao.selectImagesByPost(it) }
                    val tags = async { postDao.selectTagsByPost(it) }
                    Post(
                        id = it,
                        year = post.year,
                        month = post.month,
                        day = post.day,
                        content = post.content,
                        images = images.await().map { image -> image.toImage() },
                        tags = tags.await().map { tag -> tag.toTag() }
                    )
                }
            }
        }.awaitAll().filterNotNull()
    }

    override fun getPostPaging(year: Int, month: Int): Flow<PagingData<Post>> {
        return Pager(
            config = PagingConfig(
                pageSize = PostPagingSource.PAGING_SIZE,
                enablePlaceholders = true,
                maxSize = PostPagingSource.PAGING_SIZE * 5
            ),
            pagingSourceFactory = {
                PostPagingSource(postDao)
            }
        ).flow
    }
}