package com.example.licenta.services

import android.content.Context
import android.util.Log
import com.example.licenta.api.ApiModule
import com.example.licenta.api.BaseApiCall
import com.example.licenta.api.error.ApiException
import com.example.licenta.api.error.NetworkException
import com.example.licenta.api.utils.CallResult
import com.example.licenta.models.Cuisine
import com.example.licenta.models.Review

class CuisinesService(context: Context) : BaseApiCall() {
    companion object {
        private var instance: CuisinesService? = null

        fun getInstance(context: Context): CuisinesService {
            if (instance == null) {
                instance = CuisinesService(context)
            }

            return instance!!
        }
    }

    private val api = ApiModule(context).cuisinesApi

    suspend fun getCuisines(): List<Cuisine> {
        when (val response = safeApiCall { api.getCuisines() }) {
            is CallResult.Success -> {
                if (response.value.data != null) {
                    Log.d("HERE", response.value.data.toString())
                    return response.value.data
                } else {
                    throw ApiException("API Error")
                }
            }
            is CallResult.Failure -> {
                if (response.networkError) {
                    throw NetworkException("NETWORK Error")
                } else {
                    throw ApiException("API Error")
                }
            }
        }
    }
}