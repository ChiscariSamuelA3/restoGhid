package com.example.licenta.api.endpoints

import com.example.licenta.api.ApiResponse
import com.example.licenta.models.AuthResponse
import com.example.licenta.models.User
import retrofit2.http.*

interface UsersApi {
    @POST("users/register")
    suspend fun userRegister(
        @Body user: User
    ): ApiResponse<User>

    @POST("users/login")
    suspend fun userLogin(
        @Body user: User
    ): ApiResponse<AuthResponse>

    @GET("users/data")
    suspend fun getUserData(): ApiResponse<User>

    @DELETE("users/bookings/{id}")
    suspend fun deleteBookingById(@Path("id") id: Long): ApiResponse<String>
}