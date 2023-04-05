package com.core.database.di

import android.content.Context
import androidx.room.Room
import com.core.database.HarooDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {
    @Provides
    fun providePostDao(
        harooDatabase: HarooDatabase
    ) = harooDatabase.getPostDao()
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideHarooDatabase(
        @ApplicationContext
        context: Context
    ): HarooDatabase {
        return Room.databaseBuilder(
            context,
            HarooDatabase::class.java,
            HarooDatabase.DATABASE_NAME
        ).build()
    }
}