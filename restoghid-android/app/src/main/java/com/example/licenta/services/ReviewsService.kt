package com.example.licenta.services

import android.content.Context
import android.util.Log
import com.example.licenta.api.ApiModule
import com.example.licenta.api.BaseApiCall
import com.example.licenta.api.error.ApiException
import com.example.licenta.api.error.NetworkException
import com.example.licenta.api.utils.CallResult
import com.example.licenta.models.Review

class ReviewsService(private val context: Context) : BaseApiCall() {
    companion object {
        private var instance: ReviewsService? = null

        fun getInstance(context: Context): ReviewsService {
            if (instance == null) {
                instance = ReviewsService(context)
            }

            return instance!!
        }
    }

    private val api = ApiModule(context).reviewsApi

    suspend fun getRestaurantReviews(restaurantId: String): List<Review> {
        when (val response = safeApiCall { ApiModule(context).reviewsApi.getReviewsByRestaurantId(restaurantId) }) {
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

    suspend fun getRestaurantReviewsByManager(): List<Review> {
        when (val response = safeApiCall { ApiModule(context).reviewsApi.getRestaurantReviewsByManager() }) {
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

    suspend fun updateReview(id: Long, reply: String): Review {
        when (val response = safeApiCall { ApiModule(context).reviewsApi.updateReview(id, reply) }) {
            is CallResult.Success -> {
                if (response.value.data != null) {
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

    suspend fun addReview(restaurantId: String, review: Review): Review {
        when (val response = safeApiCall { ApiModule(context).reviewsApi.addReview(restaurantId, review) }) {
            is CallResult.Success -> {
                if (response.value.data != null) {
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