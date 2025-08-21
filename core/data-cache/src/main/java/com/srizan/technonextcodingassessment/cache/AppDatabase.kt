package com.srizan.technonextcodingassessment.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.srizan.technonextcodingassessment.cache.dao.PostDao
import com.srizan.technonextcodingassessment.cache.entity.PostEntity

@Database(
    entities = [
        PostEntity::class,

    ],
    version = 1,
    autoMigrations = [
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
}