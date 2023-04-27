package com.core.data.post.local

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.core.common.util.PostIdIsNegativeException
import com.core.data.di.IODispatcher
import com.core.data.post.PostRepository
import com.core.database.dao.PostDao
import com.core.model.data.ImageSource
import com.core.model.data.PostSource
import com.core.model.data.PostSources
import com.core.model.data.TagSource
import com.core.model.data.toImageEntity
import com.core.model.data.toPostEntity
import com.core.model.data.toSource
import com.core.model.data.toTagEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PostLocalRepositoryImpl @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val postDao: PostDao
) : PostRepository {

    private var pagingInvalidate: (() -> Unit)? = null

    override suspend fun addPost(
        post: PostSource,
        removeImages: List<ImageSource>,
        removeTags: List<TagSource>
    ): Result<Long> = withContext(ioDispatcher) {
        try {
            val postId = postDao.insertPost(post.toPostEntity())
            if (postId >= 0L) { // post 저장에 성공한 경우
                launch { postDao.insertImages(post.images.map { it.toImageEntity(postId) }) }
                launch { postDao.insertTags(post.tags.map { it.toTagEntity(postId) }) }

                // 기존 이미지 제거
                if (removeImages.isNotEmpty())
                    launch {
                        postDao.deleteImages(removeImages.map { image ->
                            image.toImageEntity(postId)
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
                pagingInvalidate?.invoke()
                Result.success(postId)
            } else {
                Result.failure(PostIdIsNegativeException())
            }
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    override suspend fun getPost(
        year: Int,
        month: Int,
        day: Int
    ): Result<PostSource?> = withContext(ioDispatcher) {
        try {
            val post = postDao.selectPostByDate(year, month, day)
            Result.success(post?.id?.let {
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
            })
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    override suspend fun getPosts(year: Int, month: Int): Result<List<PostSource>> = withContext(ioDispatcher) {
        try {
            val posts = postDao.selectPostByMonth(year, month)
            Result.success(posts.map { post ->
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
            }.awaitAll().filterNotNull())
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    override fun getPostPaging(): Flow<PagingData<PostSources>> {
        return Pager(
            config = PagingConfig(
                pageSize = PostLocalPagingSource.PAGING_SIZE
            ),
            pagingSourceFactory = {
                val source = PostLocalPagingSource(ioDispatcher, postDao)
                pagingInvalidate = { source.invalidate() }
                source
            }
        ).flow
    }

    override suspend fun removePost(postId: Long): Result<Unit> =
        withContext(ioDispatcher) {
            try {
                postDao.deletePost(postId)
                pagingInvalidate?.invoke()
                Result.success(Unit)
            } catch (exception: Exception) {
                Result.failure(exception)
            }
        }
}