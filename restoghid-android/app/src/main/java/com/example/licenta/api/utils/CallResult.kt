package com.example.licenta.api.utils

import okhttp3.ResponseBody

sealed class CallResult<out T> {
    data class Success<out T>(val value: T) : CallResult<T>()

    data class Failure(
        val networkError: Boolean,
        val errorCode: Int?,
        val errorBody: ResponseBody?
    ) : CallResult<Nothing>()
}