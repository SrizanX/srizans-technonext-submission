package com.srizan.technonextcodingassessment.domain


import com.srizan.technonextcodingassessment.model.ApiResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface Mapper<R, E> {
     fun map(type: R): E

    fun mapApiResponse(apiResult: Flow<ApiResult<R>>): Flow<ApiResult<E>> {
        return apiResult.map {
            when (it) {
                is ApiResult.Success -> ApiResult.Success(map(it.data))
                is ApiResult.Error -> ApiResult.Error(it.errorMessage, it.code)
                is ApiResult.Loading -> ApiResult.Loading
            }
        }
    }
}


fun <R, E> mapApiResponse(
    apiResult: Flow<ApiResult<R>>, mapper: Mapper<R, E>
): Flow<ApiResult<E>> {
    return apiResult.map {
        when (it) {
            is ApiResult.Success -> ApiResult.Success(mapper.map(it.data))
            is ApiResult.Error -> ApiResult.Error(it.errorMessage, it.code)
            is ApiResult.Loading -> ApiResult.Loading
        }
    }
}