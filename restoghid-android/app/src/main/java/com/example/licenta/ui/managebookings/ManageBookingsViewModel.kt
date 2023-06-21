package com.example.licenta.ui.managebookings

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.licenta.api.error.ApiException
import com.example.licenta.models.Restaurant
import com.example.licenta.services.RestaurantsService
import kotlinx.coroutines.launch

class ManageBookingsViewModel(context: Context) : ViewModel() {
    private val restaurantsService: RestaurantsService = RestaurantsService(context)

    private val restaurantDetails: MutableLiveData<RestaurantItemViewModel> =
        MutableLiveData(null)

    private val errorLiveData: MutableLiveData<String?> = MutableLiveData(null)

    fun getErrorLiveData() = errorLiveData

    fun getRestaurantDetails() = restaurantDetails

    fun loadRestaurantDetailsByManager() = viewModelScope.launch {
        try {
            val res = RestaurantItemViewModel(
                restaurantsService.getRestaurantByManager()
            )
            Log.d("_MANAGERIAL", res.bookings.toString())
            restaurantDetails.postValue(res)
        } catch (e: ApiException) {
            errorLiveData.postValue(e.message)
        }
    }

    fun deleteBookingById(id: Long) = viewModelScope.launch {
        try {
            restaurantsService.deleteBookingById(id)
        } catch (e: ApiException) {
            errorLiveData.postValue(e.message)
        }
    }
}

data class RestaurantItemViewModel(val restaurant: Restaurant) {
    val id = restaurant.locationId
    val name = restaurant.name
    val email = restaurant.email
    val bookings = restaurant.bookings
}

class ManageBookingsViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ManageBookingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ManageBookingsViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}