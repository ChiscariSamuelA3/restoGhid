package com.example.licenta.ui.restaurantdetails

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.licenta.api.error.ApiException
import com.example.licenta.models.Photo
import com.example.licenta.models.Restaurant
import com.example.licenta.services.PhotosService
import com.example.licenta.services.RestaurantsService
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class RestaurantDetailsViewModel(context: Context) : ViewModel() {
    private val restaurantsService: RestaurantsService = RestaurantsService(context)
    private val photosService: PhotosService = PhotosService.getInstance(context)

    private val restaurantDetails: MutableLiveData<RestaurantItemViewModel> =
        MutableLiveData(null)

    private val errorLiveData: MutableLiveData<String?> = MutableLiveData(null)

    fun getErrorLiveData() = errorLiveData

    fun getRestaurantDetails() = restaurantDetails

    fun loadRestaurantDetails(selectedMarkerId: String) = viewModelScope.launch {
        try {
            val res = RestaurantItemViewModel(
                restaurantsService.getRestaurantById(selectedMarkerId)
            )

            val photo = photosService.getPhotosByLocationId(selectedMarkerId)

            res.image = photo.url

            restaurantDetails.postValue(res)
        } catch (e: ApiException) {
            errorLiveData.postValue(e.message)
        }
    }
}

data class RestaurantItemViewModel(val restaurant: Restaurant) {
    val id = restaurant.locationId
    val name = restaurant.name
    val address = restaurant.address
    val latitude = restaurant.latitude.toDouble()
    val longitude = restaurant.longitude.toDouble()
    val subcategoryKey = restaurant.subcategoryKey
    val price = restaurant.price
    val phone = restaurant.phone
    val email = restaurant.email
    val description = restaurant.description
    val cuisine = restaurant.cuisine
    val dietaryRestrictions = restaurant.dietaryRestrictions
    var image = ""
    val rating = restaurant.rating
}

class RestaurantDetailsViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RestaurantDetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RestaurantDetailsViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
