package com.core.datasource.local.image

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.core.datasource.ImageDatasource
import com.core.datasource.model.Image
import com.core.datastore.ImageDatastore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ImageLocalDataSourceImpl @Inject constructor(
    private val imageDatastore: ImageDatastore
) : ImageDatasource {
    override fun getImages(): Flow<PagingData<Image>> {
        return Pager(
            config = PagingConfig(
                pageSize = ImagePagingSource.PAGING_SIZE,
                enablePlaceholders = true,
                maxSize = ImagePagingSource.PAGING_SIZE * 5
            ),
            pagingSourceFactory = {
                ImagePagingSource(imageDatastore)
            }
        ).flow
    }
}