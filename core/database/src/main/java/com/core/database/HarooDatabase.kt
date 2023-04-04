package com.core.database

import com.core.database.dao.PostDao

abstract class HarooDatabase {

    abstract fun getPostDao(): PostDao

    companion object {
        const val DATABASE_NAME = "Haroo.db"
        const val DATABASE_VERSION = 1
    }
}