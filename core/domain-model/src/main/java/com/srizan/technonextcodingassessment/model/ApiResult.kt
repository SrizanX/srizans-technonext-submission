package com.srizan.technonextcodingassessment.model


sealed class ApiResult<out R> {
    data class Success<out T>(val data: T) : ApiResult<T>()
    data object Loading : ApiResult<Nothing>()
    data class Error(val errorMessage: String, val code: Int?) : ApiResult<Nothing>()
}