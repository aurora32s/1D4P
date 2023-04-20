package com.core.data.image.datastore

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.core.data.di.IODispatcher
import com.core.data.image.ImageRepository
import com.core.datastore.ImageDatastore
import com.core.model.data.ImageSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ImageDataStoreRepositoryImpl @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val imageDatastore: ImageDatastore
) : ImageRepository {
    override fun getImages(): Flow<PagingData<ImageSource>> {
        return Pager(
            config = PagingConfig(
                pageSize = ImageDataStorePagingSource.PAGING_SIZE,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                ImageDataStorePagingSource(ioDispatcher, imageDatastore)
            }
        ).flow
    }
}