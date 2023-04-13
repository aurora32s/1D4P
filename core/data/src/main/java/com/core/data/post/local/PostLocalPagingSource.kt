package com.core.data.post.local

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.core.database.dao.PostDao
import com.core.model.data.PostSource
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
) : PagingSource<YearMonth, PostSource>() {
    override fun getRefreshKey(state: PagingState<YearMonth, PostSource>): YearMonth? {
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.prevKey?.plusMonths(1)
        }
    }

    override suspend fun load(params: LoadParams<YearMonth>): LoadResult<YearMonth, PostSource> =
        coroutineScope {
            val date: YearMonth = params.key ?: YearMonth.now()

            try {
                val posts =
                    postDao.selectPostByMonth(date.year, date.monthValue).map { post ->
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
                    }.awaitAll()

                LoadResult.Page(
                    prevKey = date.minusMonths(1),
                    data = posts.filterNotNull(),
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