package com.example.licenta.dto

data class RestaurantUpdateDTO(
    val name: String,
    val description: String,
    val address: String,
    val phone: String,
    val totalSeats: Int,
)