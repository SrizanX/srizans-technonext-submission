package com.srizan.technonextcodingassessment.network

import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.srizan.technonextcodingassessment.model.ApiResult
import com.srizan.technonextcodingassessment.network.model.ApiError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withTimeout
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Centralized network resource downloader that provides consistent error handling
 * and loading states for all API calls.
 * 
 * Features:
 * - Automatic loading state emission
 * - Comprehensive error handling with user-friendly messages
 * - Structured logging for debugging
 * - Timeout handling
 * - Consistent ApiResult wrapping
 */
@Singleton
class NetworkResourceDownLoader @Inject constructor(
    private val gson: Gson
) {


    /**
     * Safely executes an API call with comprehensive error handling and loading states.
     * 
     * @param timeoutMillis Optional timeout for the API call (default: 30 seconds)
     * @param apiCall The suspend function that makes the actual API call
     * @return Flow<ApiResult<T>> that emits Loading, then Success or Error
     */
    fun <T> safeApiCall(
        timeoutMillis: Long = 30_000L,
        apiCall: suspend () -> Response<T>
    ) = flow<ApiResult<T>> {
        try {
            val response = withTimeout(timeoutMillis) {
                apiCall.invoke()
            }
            
            if (response.isSuccessful) {
                handleSuccessResponse(response)?.let { result ->
                    emit(result)
                }
            } else {
                val errorResult = handleErrorResponse(response)
                emit(errorResult)
            }
        } catch (e: Exception) {
            val errorResult = handleException<T>(e)
            emit(errorResult)
            
            // Log error for debugging (but don't expose sensitive info to users)
            Timber.e(e, "Network request failed: ${e.message}")
        }
    }.flowOn(Dispatchers.IO).onStart {
        emit(ApiResult.Loading)
    }

    /**
     * Handles successful HTTP responses
     */
    private fun <T> handleSuccessResponse(response: Response<T>): ApiResult<T>? {
        return response.body()?.let { body ->
            ApiResult.Success(data = body)
        } ?: run {
            Timber.w("Response was successful but body is null. Code: ${response.code()}")
            ApiResult.Error("Response body is null", response.code())
        }
    }

    /**
     * Handles HTTP error responses with detailed error parsing
     */
    private fun <T> handleErrorResponse(response: Response<T>): ApiResult<T> {
        val errorBody = response.errorBody()?.string()
        val apiErrorMessage = parseApiError(errorBody)?.message
        
        val message = when (response.code()) {
            400 -> apiErrorMessage ?: "Bad request. Please check your input."
            401 -> apiErrorMessage ?: "Authentication failed. Please sign in again."
            403 -> apiErrorMessage ?: "Access denied. You don't have permission for this action."
            404 -> apiErrorMessage ?: "The requested resource was not found."
            408 -> apiErrorMessage ?: "Request timeout. Please try again."
            429 -> apiErrorMessage ?: "Too many requests. Please wait a moment before trying again."
            in 500..599 -> "Server error. Please try again later."
            else -> apiErrorMessage ?: errorBody ?: "Unknown error occurred"
        }
        
        Timber.w("HTTP Error ${response.code()}: $message")
        return ApiResult.Error(message, response.code())
    }


    /**
     * Safely parses API error response body
     */
    private fun parseApiError(errorBody: String?): ApiError? {
        return try {
            errorBody?.let { gson.fromJson(it, ApiError::class.java) }
        } catch (e: JsonSyntaxException) {
            Timber.w(e, "Failed to parse error response: $errorBody")
            null
        }
    }

    /**
     * Handles various types of exceptions with user-friendly error messages
     */
    private fun <T> handleException(exception: Exception): ApiResult<T> {
        return when (exception) {
            is TimeoutCancellationException -> {
                ApiResult.Error("Request timed out. Please check your connection and try again.", 408)
            }
            
            is HttpException -> {
                val code = exception.code()
                val errorBody = exception.response()?.errorBody()?.string()
                val apiError = parseApiError(errorBody)
                
                val message = when (code) {
                    400 -> apiError?.message ?: "Bad request. Please check your input."
                    401 -> "Authentication failed. Please sign in again."
                    403 -> "Access denied. You don't have permission for this action."
                    404 -> "The requested resource was not found."
                    408 -> "Request timeout. Please try again."
                    429 -> "Too many requests. Please wait a moment before trying again."
                    in 500..599 -> "Server error. Please try again later."
                    else -> apiError?.message ?: errorBody ?: "HTTP error occurred"
                }
                
                ApiResult.Error(message, code)
            }

            is UnknownHostException -> {
                ApiResult.Error("No internet connection. Please check your network settings.", 0)
            }
            
            is ConnectException -> {
                ApiResult.Error("Connection failed. Please check your internet connection.", 0)
            }
            
            is SocketTimeoutException -> {
                ApiResult.Error("Connection timed out. Please try again.", 0)
            }
            
            is IOException -> {
                ApiResult.Error("Network error. Please check your connection and try again.", 0)
            }

            is JsonSyntaxException -> {
                ApiResult.Error("Invalid data format received. Please try again.", 0)
            }
            
            is JsonParseException -> {
                ApiResult.Error("Data parsing error. Please try again.", 0)
            }

            else -> {
                ApiResult.Error("An unexpected error occurred. Please try again.", 0)
            }
        }
    }
}