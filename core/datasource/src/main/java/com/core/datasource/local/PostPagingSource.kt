package com.core.datasource.local

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.core.database.dao.PostDao
import com.core.datasource.model.Post
import com.core.datasource.model.toImage
import com.core.datasource.model.toTag
import java.time.YearMonth

/**
 * 특정 연도 / 월에 대한 게시글(post) 정보를 paging 으로 요청
 */
class PostPagingSource(
    private val postDao: PostDao
) : PagingSource<YearMonth, Post>() {
    override fun getRefreshKey(state: PagingState<YearMonth, Post>): YearMonth? {
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.prevKey?.plusMonths(1)
        }
    }

    override suspend fun load(params: LoadParams<YearMonth>): LoadResult<YearMonth, Post> {
        val date: YearMonth = params.key ?: YearMonth.now()

        return try {
            val posts = postDao.selectPostByMonth(date.year, date.monthValue).mapNotNull { post ->
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

            LoadResult.Page(
                prevKey = date.minusMonths(1),
                data = posts,
                nextKey = date.plusMonths(1)
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    companion object {
        const val PAGING_SIZE = 31
    }
}