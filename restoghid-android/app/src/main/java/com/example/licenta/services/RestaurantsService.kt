package com.example.licenta.services

import android.content.Context
import android.util.Log
import com.example.licenta.api.ApiModule
import com.example.licenta.api.BaseApiCall
import com.example.licenta.api.error.ApiException
import com.example.licenta.api.error.NetworkException
import com.example.licenta.api.utils.CallResult
import com.example.licenta.dto.RestaurantUpdateDTO
import com.example.licenta.models.Restaurant
import com.example.licenta.storage.LocalStorage
import com.example.licenta.ui.auth.onboarding.skipped

class RestaurantsService(private val context: Context) : BaseApiCall() {
    companion object {
        private var instance: RestaurantsService? = null

        fun getInstance(context: Context): RestaurantsService {
            if (instance == null) {
                instance = RestaurantsService(context)
            }

            return instance!!
        }
    }

    private var api = ApiModule(context).restaurantsApi
    private val localStorage = LocalStorage(context)

    suspend fun getRestaurants(): List<Restaurant> {
        when (val response =
            safeApiCall { api.getRestaurants() }) {
            is CallResult.Success -> {
                if (response.value.data != null) {
                    return response.value.data
                } else {
                    Log.d("HERE", response.toString())
                    throw ApiException("API Error")
                }
            }
            is CallResult.Failure -> {
                if (response.networkError) {
                    throw NetworkException("NETWORK Error")
                } else {
                    throw ApiException("TOKEN_EXPIRED")
                }
            }
        }
    }

    suspend fun getRecommendations(): List<Restaurant> {
        api = ApiModule(context).restaurantsApi

        when (val response = safeApiCall { api.getRecommendations() }) {
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
                    throw ApiException("TOKEN_EXPIRED")
                }
            }
        }
    }

    suspend fun getRestaurantById(selectedMarkerId: String): Restaurant {
        when (val response = safeApiCall { api.getRestaurantById(selectedMarkerId) }) {
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

    suspend fun updateRestaurant(
        restaurant: RestaurantUpdateDTO
    ): Restaurant {
        when (val response =
            safeApiCall { api.updateRestaurant(restaurant) }) {
            is CallResult.Success -> {
                if (response.value.data != null) {
                    return response.value.data
                } else {
                    Log.d("HERE", response.toString())
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

    suspend fun getRestaurantByManager(): Restaurant {
        when (val response = safeApiCall { api.getRestaurantByManager() }) {
            is CallResult.Success -> {
                if (response.value.data != null) {
                    return response.value.data
                } else {
                    Log.d("HERE", response.toString())
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

    var username: String
        get() = localStorage.getUsername()
        set(value) {
            localStorage.setUsername(value)
        }

    fun clearPreferences() {
        localStorage.clear()
    }

    suspend fun deleteBookingById(bookingId: Long): String {
        when (val response = safeApiCall {
            api.deleteBookingById(bookingId)
        }) {
            is CallResult.Success -> {
                if (response.value.data != null) {
                    return "ok"
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