package com.srizan.technonextcodingassessment.network.di

import com.srizan.technonextcodingassessment.network.PostApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@InstallIn(SingletonComponent::class)
@Module
object PostApiServiceModule {

    @Provides
    fun provideRoutesApiService(
        retrofit: Retrofit.Builder
    ): PostApiService {
        return retrofit.baseUrl("https://jsonplaceholder.typicode.com").build()
            .create(PostApiService::class.java)
    }

}