package com.srizan.technonextcodingassessment.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.srizan.technonextcodingassessment.cache.dao.PostDao
import com.srizan.technonextcodingassessment.cache.dao.UserDao
import com.srizan.technonextcodingassessment.cache.entity.PostEntity
import com.srizan.technonextcodingassessment.cache.entity.UserEntity

@Database(
    entities = [PostEntity::class, UserEntity::class], version = 1, autoMigrations = []
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun userDao(): UserDao
}