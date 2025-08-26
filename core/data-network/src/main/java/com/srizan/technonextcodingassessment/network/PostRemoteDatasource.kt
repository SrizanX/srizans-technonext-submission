package com.srizan.technonextcodingassessment.network

import com.srizan.technonextcodingassessment.domain.datasource.PostDataSource
import com.srizan.technonextcodingassessment.model.ApiResult
import com.srizan.technonextcodingassessment.model.Post
import com.srizan.technonextcodingassessment.network.model.PostApiMapper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostRemoteDatasource @Inject constructor(
    private val networkResourceDownLoader: NetworkResourceDownLoader,
    private val postApiService: PostApiService,
    private val postApiMapper: PostApiMapper
) : PostDataSource {
    override suspend fun fetchPosts(): Flow<ApiResult<List<Post>>> {
        return networkResourceDownLoader.safeApiCall { postApiService.getPosts() }
            .run { postApiMapper.mapApiResponse(this) }
    }
}