package com.srizan.technonextcodingassessment.network.model

data class ApiError(
    val message: String,
    val errorCode: String? = null,
)