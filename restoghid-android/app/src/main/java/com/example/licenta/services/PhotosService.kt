package com.example.licenta.services

import android.content.Context
import android.util.Log
import com.example.licenta.api.ApiModule
import com.example.licenta.api.BaseApiCall
import com.example.licenta.api.error.ApiException
import com.example.licenta.api.error.NetworkException
import com.example.licenta.api.utils.CallResult
import com.example.licenta.models.Photo

class PhotosService(context: Context) : BaseApiCall() {
    companion object {
        private var instance: PhotosService? = null

        fun getInstance(context: Context): PhotosService {
            if (instance == null) {
                instance = PhotosService(context)
            }

            return instance!!
        }
    }

    private val api = ApiModule(context).photosApi

    suspend fun getPhotosByLocationId(locationId: String): Photo {
        when (val response = safeApiCall { api.getPhotoByLocationId(locationId) }) {
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