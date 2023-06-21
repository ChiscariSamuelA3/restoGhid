package com.example.licenta.api

import android.util.Log
import com.example.licenta.api.utils.CallResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

abstract class BaseApiCall {
    suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): CallResult<T> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("_HERE", "ok")
                CallResult.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                when (throwable) {
                    is HttpException -> {
                        CallResult.Failure(
                            false,
                            throwable.code(),
                            throwable.response()?.errorBody()
                        )
                    }
                    else -> {
                        Log.d("_HERE", throwable.message.toString())
                        CallResult.Failure(true, null, null)
                    }
                }
            }
        }
    }
}