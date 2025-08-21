package com.srizan.technonextcodingassessment.model


sealed class ApiResult<out R> {
    data class Success<out T>(val data: T) : ApiResult<T>()
    data class Loading<out T>(val isLoading: Boolean) : ApiResult<T>()
    data class Error<out T>(val errorMessage: String, val code: Int) : ApiResult<T>()
}