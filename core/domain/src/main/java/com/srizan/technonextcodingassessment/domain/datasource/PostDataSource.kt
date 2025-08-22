package com.srizan.technonextcodingassessment.domain.datasource

import com.srizan.technonextcodingassessment.model.ApiResult
import com.srizan.technonextcodingassessment.model.Post
import kotlinx.coroutines.flow.Flow

interface PostDataSource {
    suspend fun fetchPosts(): Flow<ApiResult<List<Post>>>
}