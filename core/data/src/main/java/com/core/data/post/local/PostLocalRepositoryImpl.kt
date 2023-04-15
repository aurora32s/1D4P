package com.core.data.post.local

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.core.data.post.PostRepository
import com.core.database.dao.PostDao
import com.core.model.data.*
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PostLocalRepositoryImpl @Inject constructor(
    private val postDao: PostDao
) : PostRepository {
    override suspend fun addPost(
        post: PostSource,
        removeImages: List<ImageSource>,
        removeTags: List<TagSource>
    ): Long = coroutineScope {
        val postId = postDao.insertPost(post.toPostEntity())
        if (postId >= 0L) {
            launch { postDao.insertImages(post.images.map { it.toImageEntity(postId) }) }
            launch { postDao.insertTags(post.tags.map { it.toTagEntity(postId) }) }

            // 기존 이미지 제거
            if (removeImages.isNotEmpty())
                launch {
                    postDao.deleteImages(removeImages.mapNotNull { image ->
                        image.id?.let {
                            image.toImageEntity(
                                it
                            )
                        }
                    })
                }
            // 기존 태그 제거
            if (removeTags.isNotEmpty())
                launch {
                    postDao.deleteTags(removeTags.mapNotNull { tag ->
                        tag.id?.let {
                            tag.toTagEntity(
                                it
                            )
                        }
                    })
                }
        }
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
                images = images.await().map { image -> image.toSource() },
                tags = tags.await().map { tag -> tag.toSource() }
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
                            images = images.await().map { image -> image.toSource() },
                            tags = tags.await().map { tag -> tag.toSource() }
                        )
                    }
                }
            }.awaitAll().filterNotNull()
        }

    override fun getPostPaging(
        year: Int,
        month: Int
    ): Flow<PagingData<PostSources>> {
        return Pager(
            config = PagingConfig(
                pageSize = PostLocalPagingSource.PAGING_SIZE,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                PostLocalPagingSource(postDao)
            }
        ).flow
    }
}