package com.example.licenta.api.endpoints

import com.example.licenta.api.ApiResponse
import com.example.licenta.models.Review
import retrofit2.http.*

interface ReviewsApi {
    @GET("reviews/restaurant/{restaurantId}")
    suspend fun getReviewsByRestaurantId(@Path("restaurantId") restaurantId: String): ApiResponse<List<Review>>

    @POST("reviews/add/{restaurantId}")
    suspend fun addReview(
        @Path("restaurantId") restaurantId: String,
        @Body review: Review
    ): ApiResponse<Review>

    @GET("reviews/restaurant/manager")
    suspend fun getRestaurantReviewsByManager(): ApiResponse<List<Review>>

    @PATCH("reviews/reply/{reviewId}")
    suspend fun updateReview(
        @Path("reviewId") reviewId: Long,
        @Body reply: String
    ): ApiResponse<Review>
}