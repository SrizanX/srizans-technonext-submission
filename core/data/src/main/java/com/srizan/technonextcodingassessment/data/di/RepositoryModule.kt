package com.srizan.technonextcodingassessment.data.di

import com.srizan.technonextcodingassessment.data.repository.PostRepositoryImpl
import com.srizan.technonextcodingassessment.domain.PostRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    fun bindAnalyticsService(repo: PostRepositoryImpl): PostRepository
}