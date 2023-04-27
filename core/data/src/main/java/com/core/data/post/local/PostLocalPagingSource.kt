package com.core.data.post.local

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.core.database.dao.PostDao
import com.core.model.data.PostSource
import com.core.model.data.PostSources
import com.core.model.data.toSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.time.YearMonth

/**
 * 특정 연도 / 월에 대한 게시글(post) 정보를 paging 으로 요청
 */
class PostLocalPagingSource(
    private val ioDispatcher: CoroutineDispatcher,
    private val postDao: PostDao,
    private val pageSize: Int = PAGING_SIZE
) : PagingSource<YearMonth, PostSources>() {

    override fun getRefreshKey(state: PagingState<YearMonth, PostSources>): YearMonth? {
        return state.anchorPosition?.let { anchorPosition ->
            val pageIndex = anchorPosition / pageSize
            val index = anchorPosition % pageSize
            val stand = state.pages[pageIndex].data[index].let { YearMonth.of(it.year, it.month) }
            return if (index < pageSize / 2) {
                stand?.minusMonths(pageSize / 2L)
            } else {
                stand
            }
        }
    }

    override suspend fun load(params: LoadParams<YearMonth>): LoadResult<YearMonth, PostSources> =
        withContext(ioDispatcher) {
            val date: YearMonth = params.key ?: YearMonth.now().minusMonths(1)

            try {
                val posts = List(pageSize) { offset ->
                    getPostsByYearAndMonth(date.plusMonths(offset.toLong()))
                }
                LoadResult.Page(
                    prevKey = date.minusMonths(pageSize.toLong()),
                    data = posts,
                    nextKey = date.plusMonths(pageSize.toLong())
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
        const val PAGING_SIZE = 10
    }
}