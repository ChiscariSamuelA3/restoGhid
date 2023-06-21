package com.example.licenta.models

import java.time.LocalDate

data class Booking(
    val id: Long,
    val restaurant: Restaurant? = null,
    val user: User? = null,
    val date: String,
    val mealType: Int,
    val selectedInterval: Int,
    val bookedSeats: Int,
    val totalSeats: Int,
    val restaurantName: String,
    val latitude: String,
    val longitude: String,
    val phoneNumber: String,
    val userPhone: String,
    val username: String
) {
    val localDate: LocalDate = LocalDate.parse(date)
}