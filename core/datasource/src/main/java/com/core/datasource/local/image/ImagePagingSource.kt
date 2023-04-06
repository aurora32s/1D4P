package com.core.datasource.local.image

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.core.datasource.model.Image
import com.core.datasource.model.toImage
import com.core.datastore.ImageDatastore
import kotlinx.coroutines.coroutineScope

class ImagePagingSource(
    private val imageDatastore: ImageDatastore
) : PagingSource<Int, Image>() {
    override fun getRefreshKey(state: PagingState<Int, Image>): Int? {
        return state.anchorPosition?.let { position ->
            val page = state.closestPageToPosition(position)
            return page?.prevKey?.plus(1) ?: page?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Image> = coroutineScope {
        val offset: Int = params.key ?: INIT_OFFSET

        try {
            val images = imageDatastore.getImages(PAGING_SIZE, offset).map { it.toImage() }

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