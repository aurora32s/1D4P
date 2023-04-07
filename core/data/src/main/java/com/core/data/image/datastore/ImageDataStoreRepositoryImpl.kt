package com.core.data.image.datastore

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.core.data.image.ImageRepository
import com.core.datastore.ImageDatastore
import com.core.model.data.ImageSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ImageDataStoreRepositoryImpl @Inject constructor(
    private val imageDatastore: ImageDatastore
) : ImageRepository {
    override fun getImages(): Flow<PagingData<ImageSource>> {
        return Pager(
            config = PagingConfig(
                pageSize = ImageDataStorePagingSource.PAGING_SIZE,
                enablePlaceholders = true,
//                maxSize = ImageDataStorePagingSource.PAGING_SIZE * 10
            ),
            pagingSourceFactory = {
                ImageDataStorePagingSource(imageDatastore)
            }
        ).flow
    }
}