package com.srizan.technonextcodingassessment.data.repository

import com.srizan.technonextcodingassessment.cache.dao.PostDao
import com.srizan.technonextcodingassessment.domain.PostRepository
import com.srizan.technonextcodingassessment.model.ApiResult
import com.srizan.technonextcodingassessment.model.Post
import com.srizan.technonextcodingassessment.network.PostApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val postApiService: PostApiService, private val postDao: PostDao
) : PostRepository {
    override suspend fun fetchPosts(): Flow<ApiResult<List<Post>>> {
        return flow {
            emit(ApiResult.Loading(true))
            emit(
                ApiResult.Success(
                    listOf<Post>(
                        Post(
                            id = 1,
                            title = "Sample Post",
                            body = "This is a sample post for demonstration purposes.",
                            userId = 1
                        ), Post(
                            id = 2,
                            title = "Another Post",
                            body = "This is another sample post for demonstration purposes.",
                            userId = 1
                        ), Post(
                            id = 3,
                            title = "Third Post",
                            body = "This is the third sample post for demonstration purposes.",
                            userId = 1
                        ), Post(
                            id = 4,
                            title = "Fourth Post",
                            body = "This is the fourth sample post for demonstration purposes.",
                            userId = 1
                        )
                    )
                )
            )
            emit(ApiResult.Loading(false))
        }
    }
}