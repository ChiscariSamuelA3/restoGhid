package com.example.licenta.api.endpoints

import com.example.licenta.api.ApiResponse
import com.example.licenta.dto.RestaurantUpdateDTO
import com.example.licenta.models.Restaurant
import com.example.licenta.models.Review
import retrofit2.http.*

interface RestaurantsApi {
    @GET("restaurants")
    suspend fun getRestaurants(): ApiResponse<List<Restaurant>>

    @GET("restaurants/favorites")
    suspend fun getRecommendations(): ApiResponse<List<Restaurant>>

    @GET("restaurants/{id}")
    suspend fun getRestaurantById(@Path("id") id: String): ApiResponse<Restaurant>

    @GET("restaurants/manager")
    suspend fun getRestaurantByManager(): ApiResponse<Restaurant>

    @DELETE("restaurants/bookings/{id}")
    suspend fun deleteBookingById(@Path("id") id: Long): ApiResponse<String>

    @PATCH("restaurants/update")
    suspend fun updateRestaurant(@Body restaurant: RestaurantUpdateDTO): ApiResponse<Restaurant>
}