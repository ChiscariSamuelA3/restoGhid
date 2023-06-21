package com.example.licenta.api.endpoints

import com.example.licenta.api.ApiResponse
import com.example.licenta.models.Cuisine
import retrofit2.http.GET

interface CuisinesApi {
    @GET("cuisines")
    suspend fun getCuisines(): ApiResponse<List<Cuisine>>
}