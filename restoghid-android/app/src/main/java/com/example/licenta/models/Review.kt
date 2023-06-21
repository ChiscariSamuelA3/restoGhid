package com.example.licenta.models

import com.google.gson.annotations.SerializedName
import java.time.LocalDate
import java.time.ZonedDateTime

data class Review(
    @SerializedName("id")
    val id: Long? = null,

    @SerializedName("idUser")
    val userId: Long? = null,

    @SerializedName("idRestaurant")
    val restaurantId: String? = null,

    @SerializedName("content")
    val content: String,

    @SerializedName("score")
    val score: Double,

    @SerializedName("username")
    val username: String?= null,

    @SerializedName("reply")
    val reply: String? = null,

    @SerializedName("date")
    val date: String? = null
)
