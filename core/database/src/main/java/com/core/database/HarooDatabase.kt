package com.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.core.database.dao.PostDao
import com.core.model.database.ImageEntity
import com.core.model.database.PostEntity

@Database(
    entities = [
        PostEntity::class,
        ImageEntity::class
    ],
    version = HarooDatabase.DATABASE_VERSION
)
abstract class HarooDatabase : RoomDatabase() {

    abstract fun getPostDao(): PostDao

    companion object {
        const val DATABASE_NAME = "Haroo.db"
        const val DATABASE_VERSION = 1
    }
}