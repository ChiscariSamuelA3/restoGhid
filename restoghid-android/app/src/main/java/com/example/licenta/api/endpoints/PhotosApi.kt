package com.example.licenta.api.endpoints

import com.example.licenta.api.ApiResponse
import com.example.licenta.models.Photo
import retrofit2.http.GET
import retrofit2.http.Path

interface PhotosApi {
    @GET("photos/{locationId}")
    suspend fun getPhotoByLocationId(@Path("locationId") locationId: String): ApiResponse<Photo>
}