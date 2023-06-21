package com.example.licenta.services

import android.content.Context
import android.util.Log
import com.example.licenta.api.ApiModule
import com.example.licenta.api.BaseApiCall
import com.example.licenta.api.error.ApiException
import com.example.licenta.api.error.NetworkException
import com.example.licenta.api.utils.CallResult
import com.example.licenta.models.Booking
import java.time.LocalDate

class BookingService(private val context: Context) : BaseApiCall() {
    companion object {
        private var instance: BookingService? = null

        fun getInstance(context: Context): BookingService {
            if (instance == null) {
                instance = BookingService(context)
            }

            return instance!!
        }
    }

    private val api = ApiModule(context).bookingApi

    suspend fun addBooking(
        restaurantId: String,
        date: LocalDate,
        mealType: Int,
        selectedInterval: Int,
        bookedSeats: Int,
        totalSeats: Int,
        userPhone: String,
    ): Booking {
        when (val response = safeApiCall {
            ApiModule(context).bookingApi.createBooking(
                restaurantId,
                date,
                mealType,
                selectedInterval,
                bookedSeats,
                totalSeats,
                userPhone
            )
        }) {
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