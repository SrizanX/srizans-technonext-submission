package com.srizan.technonextcodingassessment.domain

import com.srizan.technonextcodingassessment.model.ApiResult
import com.srizan.technonextcodingassessment.model.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    suspend fun fetchPosts(): Flow<ApiResult<List<Post>>>
}