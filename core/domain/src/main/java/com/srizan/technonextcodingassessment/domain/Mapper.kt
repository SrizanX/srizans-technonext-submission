package com.srizan.technonextcodingassessment.domain


import com.srizan.technonextcodingassessment.model.ApiResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface Mapper<R, E> {
    fun map(type: R): E

    fun mapApiResponse(source: Flow<ApiResult<R>>): Flow<ApiResult<E>> {
        return source.map {
            when (it) {
                is ApiResult.Success -> ApiResult.Success(map(it.data))
                is ApiResult.Error -> ApiResult.Error(it.errorMessage, it.code)
                is ApiResult.Loading -> ApiResult.Loading
            }
        }
    }
}