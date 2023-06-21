package com.example.licenta.models

data class Filter(
    var name: String,
    var isChecked: Boolean = false,
    var isCuisine: Boolean = true
)