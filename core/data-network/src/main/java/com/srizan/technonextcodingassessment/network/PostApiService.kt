package com.srizan.technonextcodingassessment.network

import com.srizan.technonextcodingassessment.network.model.Post
import retrofit2.Response
import retrofit2.http.GET

interface PostApiService {

    @GET("/posts")
    suspend fun getPosts(): Response<List<Post>>
}