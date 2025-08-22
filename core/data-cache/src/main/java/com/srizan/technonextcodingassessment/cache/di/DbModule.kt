package com.srizan.technonextcodingassessment.cache.di

import android.content.Context
import androidx.room.Room
import com.srizan.technonextcodingassessment.cache.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object DbModule {

    @Provides
    fun provideDb(
        @ApplicationContext applicationContext: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            applicationContext, AppDatabase::class.java, "post_db"
        ).build()
    }

    @Provides
    fun providePostDao(db: AppDatabase) = db.postDao()

    @Provides
    fun provideUserDao(db: AppDatabase) = db.userDao()

}