package com.example.licenta.api

data class ApiResponse<T>(
    val data: T?
)

data class ErrorResponse(
    val error: String,
    val statusCode: Int
)