package com.core.data.di

import com.core.data.image.ImageRepository
import com.core.data.image.datastore.ImageDataStoreRepositoryImpl
import com.core.data.post.PostRepository
import com.core.data.post.local.PostLocalRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    fun bindImageRepository(
        imageDataStoreRepositoryImpl: ImageDataStoreRepositoryImpl
    ): ImageRepository

    @Binds
    fun bindPostRepository(
        postLocalRepositoryImpl: PostLocalRepositoryImpl
    ): PostRepository
}