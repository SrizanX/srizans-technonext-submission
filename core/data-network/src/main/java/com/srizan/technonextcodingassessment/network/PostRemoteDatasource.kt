package com.srizan.technonextcodingassessment.network

import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.srizan.technonextcodingassessment.domain.datasource.PostDataSource
import com.srizan.technonextcodingassessment.model.ApiResult
import com.srizan.technonextcodingassessment.model.Post
import com.srizan.technonextcodingassessment.network.model.PostApiMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
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


data class ApiError(
    val message: String,
    val errorCode: String? = null,
)


class NetworkResourceDownLoader @Inject constructor(private val gson: Gson) {


    fun <T> safeApiCall(apiCall: suspend () -> Response<T>) = flow<ApiResult<T>> {
        try {
            val response = apiCall.invoke()
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(ApiResult.Success(data = it))
                } ?: emit(ApiResult.Error("Response body is null", response.code()))
            } else {
                val errorBody = response.errorBody()?.string()
                val apiError = try {
                    errorBody?.let { gson.fromJson(it, ApiError::class.java) }
                } catch (e: JsonSyntaxException) {
                    null // Failed to parse error body
                }
                val message = apiError?.message ?: errorBody ?: "Unknown error"
                emit(ApiResult.Error(message, response.code()))
            }
        } catch (e: Exception) {
            emit(handleException(e))
            e.printStackTrace()
        }
    }.flowOn(Dispatchers.IO).onStart {
        emit(ApiResult.Loading)
    }


    private fun <T> handleException(exception: Exception): ApiResult<T> {
        return when (exception) {
            is HttpException -> {
                val code = exception.code()
                val errorBody = exception.response()?.errorBody()?.string()
                val apiError = try {
                    errorBody?.let { gson.fromJson(it, ApiError::class.java) }
                } catch (e: JsonSyntaxException) {
                    null
                }
                val message = apiError?.message ?: errorBody ?: exception.message()
                ApiResult.Error(message, code)
            }

            is IOException -> {
                ApiResult.Error("Network error: ${exception.message}", 0)
            }

            is JsonParseException -> {
                // Attempt to extract field name from exception message
                val message = "Parsing error: ${exception.message}"
                ApiResult.Error(message, code = 0)
            }

            else -> {
                ApiResult.Error("Unexpected error: ${exception.message}", 0)
            }
        }
    }


}