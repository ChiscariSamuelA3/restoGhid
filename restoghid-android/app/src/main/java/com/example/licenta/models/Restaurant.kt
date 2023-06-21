package com.example.licenta.models

import com.google.gson.annotations.SerializedName

data class Restaurant(
    @SerializedName("categoryKey")
    val categoryKey: String,
    @SerializedName("subcategoryKey")
    val subcategoryKey: String,
    @SerializedName("location_id")
    val locationId: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("latitude")
    val latitude: String,
    @SerializedName("longitude")
    val longitude: String,
    @SerializedName("manager_username")
    val managerUsername: String,
    @SerializedName("num_reviews")
    val numReviews: String,
    @SerializedName("ranking_geo")
    val rankingGeo: String,
    @SerializedName("rating")
    val rating: String,
    @SerializedName("is_closed")
    val isClosed: Boolean,
    @SerializedName("price")
    val price: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("website")
    val website: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("cluster")
    val cluster: Int,
    @SerializedName("total_seats")
    val totalSeats: Int,
    @SerializedName("cuisine")
    val cuisine: MutableList<Cuisine>,
    @SerializedName("dietary_restrictions")
    val dietaryRestrictions: MutableList<DietaryRestrictions>,
//    @SerializedName("reviews")
//    val reviews: MutableList<Review>,
    @SerializedName("bookings")
    val bookings: MutableList<Booking>
)

data class Cuisine(
    @SerializedName("key")
    val key: String,
    @SerializedName("name")
    val name: String
)

data class DietaryRestrictions(
    @SerializedName("key")
    val key: String,

    @SerializedName("name")
    val name: String
)