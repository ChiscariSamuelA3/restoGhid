package com.example.licenta.models

import com.google.gson.annotations.SerializedName

data class Photo(
    @SerializedName("id")
    val id: Long,

    @SerializedName("restaurant_id")
    val restaurant_id: String,

    @SerializedName("url")
    val url: String
)
