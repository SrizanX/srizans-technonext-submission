package com.srizan.technonextcodingassessment.domain.repository

import com.srizan.technonextcodingassessment.model.ApiResult
import com.srizan.technonextcodingassessment.model.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun getPosts(): Flow<List<Post>>
    fun getFavouritePosts(): Flow<List<Post>>
    suspend fun refreshPosts(): Flow<ApiResult<List<Post>>>
    suspend fun cachePosts(data: List<Post>)

    suspend fun markPostAsFavourite(postId: Int)
    suspend fun unmarkPostAsFavourite(postId: Int)
    suspend fun deletePost(postId: Int)
    suspend fun deleteAllPosts()
}