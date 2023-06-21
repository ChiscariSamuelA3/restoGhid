package com.example.licenta.api.endpoints

import com.example.licenta.api.ApiResponse
import com.example.licenta.models.Booking
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.time.LocalDate

interface BookingApi {
    @POST("booking/create")
    suspend fun createBooking(
        @Query("restaurantId") restaurantId: String,
        @Query("date") date: LocalDate,
        @Query("period") mealType: Int,
        @Query("selectedInterval") selectedInterval: Int,
        @Query("numOfSeats") bookedSeats: Int,
        @Query("totalSeats") totalSeats: Int,
        @Query("userPhone") userPhone: String,
    ): ApiResponse<Booking>
}