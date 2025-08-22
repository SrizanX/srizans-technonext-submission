package com.srizan.technonextcodingassessment.network

import com.srizan.technonextcodingassessment.network.model.PostNetworkModel
import retrofit2.Response
import retrofit2.http.GET

interface PostApiService {

    @GET("/posts")
    suspend fun getPosts(): Response<List<PostNetworkModel>>
}