package com.core.datasource.di

import com.core.datasource.PostDatasource
import com.core.datasource.local.post.PostLocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DatasourceModule {
    @Binds
    fun bindPostDatasource(
        postLocalDataSourceImpl: PostLocalDataSourceImpl
    ): PostDatasource
}