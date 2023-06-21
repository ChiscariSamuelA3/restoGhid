package com.example.licenta.models

data class AuthResponse(
    val token: String,
    val isManager: Boolean
)

data class User(
    val id: Long? = null,
    val email: String? = null,
    val phoneNumber: String? = null,
    val username: String,
    val password: String,
    val city: String? = null,
    val county: String? = null,
    val address: String? = null,
    val bookings: List<Booking>? = null
)