package com.core.data.post.local

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.core.data.post.PostRepository
import com.core.database.dao.PostDao
import com.core.model.data.PostSource
import com.core.model.data.toImageEntity
import com.core.model.data.toPostEntity
import com.core.model.data.toTagEntity
import com.core.model.database.toImageSource
import com.core.model.database.toTag
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PostLocalRepositoryImpl @Inject constructor(
    private val postDao: PostDao
) : PostRepository {
    override suspend fun addPost(post: PostSource): Long = coroutineScope {
        val postId = postDao.insertPost(post.toPostEntity())
        launch { postDao.insertImages(post.images.map { it.toImageEntity(postId) }) }
        launch { postDao.insertTags(post.tags.map { it.toTagEntity(postId) }) }
        postId
    }

    override suspend fun getPost(
        year: Int,
        month: Int,
        day: Int
    ): PostSource? = coroutineScope {
        val post = postDao.selectPostByDate(year, month, day)
        post?.id?.let {
            val images = async { postDao.selectImagesByPost(it) }
            val tags = async { postDao.selectTagsByPost(it) }
            PostSource(
                id = it,
                year = post.year,
                month = post.month,
                day = post.day,
                content = post.content,
                images = images.await().map { image -> image.toImageSource() },
                tags = tags.await().map { tag -> tag.toTag() }
            )
        }
    }

    override suspend fun getPosts(year: Int, month: Int): List<PostSource> =
        coroutineScope {
            val posts = postDao.selectPostByMonth(year, month)
            posts.map { post ->
                async {
                    post.id?.let {
                        val images = async { postDao.selectImagesByPost(it) }
                        val tags = async { postDao.selectTagsByPost(it) }
                        PostSource(
                            id = it,
                            year = post.year,
                            month = post.month,
                            day = post.day,
                            content = post.content,
                            images = images.await().map { image -> image.toImageSource() },
                            tags = tags.await().map { tag -> tag.toTag() }
                        )
                    }
                }
            }.awaitAll().filterNotNull()
        }

    override fun getPostPaging(
        year: Int,
        month: Int
    ): Flow<PagingData<PostSource>> {
        return Pager(
            config = PagingConfig(
                pageSize = PostLocalPagingSource.PAGING_SIZE,
                enablePlaceholders = true,
                maxSize = PostLocalPagingSource.PAGING_SIZE * 5
            ),
            pagingSourceFactory = {
                PostLocalPagingSource(postDao)
            }
        ).flow
    }
}