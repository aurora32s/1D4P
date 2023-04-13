package com.core.data.image.datastore

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.core.datastore.ImageDatastore
import com.core.model.data.ImageSource
import com.core.model.data.toSource
import kotlinx.coroutines.coroutineScope

class ImageDataStorePagingSource(
    private val imageDatastore: ImageDatastore
) : PagingSource<Int, ImageSource>() {
    override fun getRefreshKey(state: PagingState<Int, ImageSource>): Int? {
        return state.anchorPosition?.let { position ->
            val page = state.closestPageToPosition(position)
            return page?.prevKey?.plus(1) ?: page?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ImageSource> =
        coroutineScope {
            val offset: Int = params.key ?: INIT_OFFSET

            try {
                val images =
                    imageDatastore.getImages(PAGING_SIZE, offset).map { it.toSource() }

                LoadResult.Page(
                    prevKey = if (offset == INIT_OFFSET) null else offset - PAGING_SIZE,
                    data = images,
                    nextKey = if (images.size < PAGING_SIZE) null else offset + PAGING_SIZE
                )
            } catch (exception: Exception) {
                LoadResult.Error(exception)
            }
        }

    companion object {
        const val PAGING_SIZE = 50
        const val INIT_OFFSET = 0
    }
}