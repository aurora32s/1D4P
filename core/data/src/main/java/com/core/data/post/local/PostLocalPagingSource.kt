package com.core.data.post.local

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.core.database.dao.PostDao
import com.core.model.data.PostSource
import com.core.model.data.PostSources
import com.core.model.data.toSource
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.time.YearMonth

/**
 * 특정 연도 / 월에 대한 게시글(post) 정보를 paging 으로 요청
 */
class PostLocalPagingSource(
    private val postDao: PostDao
) : PagingSource<YearMonth, PostSources>() {

    override fun getRefreshKey(state: PagingState<YearMonth, PostSources>): YearMonth? {
        return state.anchorPosition?.let { page ->
            val closedPage = state.closestPageToPosition(page)
            return closedPage?.let {
                // 최근에 로드한 정보가 현재 정보인 경우
                YearMonth.of(it.data[1].year, it.data[1].month)
            }
        }
    }

    override suspend fun load(params: LoadParams<YearMonth>): LoadResult<YearMonth, PostSources> =
        coroutineScope {
            val date: YearMonth = params.key ?: YearMonth.now()

            try {
                val posts = listOf(
                    getPostsByYearAndMonth(date.minusMonths(1)),
                    getPostsByYearAndMonth(date),
                    getPostsByYearAndMonth(date.plusMonths(1))
                )
                LoadResult.Page(
                    prevKey = date.minusMonths(3),
                    data = posts,
                    nextKey = date.plusMonths(3)
                )
            } catch (exception: Exception) {
                LoadResult.Error(exception)
            }
        }

    private suspend fun getPostsByYearAndMonth(date: YearMonth): PostSources = coroutineScope {
        val posts = postDao.selectPostByMonth(date.year, date.monthValue).map { post ->
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
        PostSources(date.year, date.monthValue, posts)
    }

    companion object {
        const val PAGING_SIZE = 1
    }
}