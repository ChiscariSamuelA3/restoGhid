package com.example.licenta.ui.restaurants

import android.content.Context
import android.location.Location
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.licenta.api.error.ApiException
import com.example.licenta.models.Filter
import com.example.licenta.models.Restaurant
import com.example.licenta.services.*
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class RestaurantsViewModel(context: Context) :
    ViewModel() {
    private val restaurantsService: RestaurantsService = RestaurantsService(context)
    private val locationService: LocationService = LocationService.getInstance(context)
    private val cuisinesService: CuisinesService = CuisinesService.getInstance(context)

    val filters = MutableLiveData<List<Filter>>()

    fun setFilters(newFilters: List<Filter>) {
        filters.value = newFilters
    }

    private val restaurants: MutableLiveData<List<RestaurantItemViewModel>?> =
        MutableLiveData(null)

    private val cuisines: MutableLiveData<List<String>?> =
        MutableLiveData(null)

    private val filteredRestaurants: MutableLiveData<List<RestaurantItemViewModel>?> =
        MutableLiveData(null)

    private val restaurantDetails: MutableLiveData<RestaurantItemViewModel> =
        MutableLiveData(null)

    private val errorLiveData: MutableLiveData<String?> = MutableLiveData(null)

    fun getErrorLiveData() = errorLiveData

    fun getRestaurants() = restaurants

    fun getCuisines() = cuisines

    fun getFilteredRestaurants() = filteredRestaurants

    fun getRestaurantDetails() = restaurantDetails

    fun loadCuisines() = viewModelScope.launch {
        try {
            cuisines.postValue(null)
            val res = cuisinesService.getCuisines()
                .map { cuisine ->
                    cuisine.name
                }

            cuisines.postValue(res)
        } catch (e: ApiException) {
            errorLiveData.postValue(e.message)
        }
    }

    fun loadRestaurantsByRecommendations() = viewModelScope.launch {
        try {
            restaurants.postValue(null)
            val res = restaurantsService.getRecommendations()
                .map { restaurant ->
                    RestaurantItemViewModel(restaurant)
                }

            restaurants.postValue(res)
        } catch (e: ApiException) {
            errorLiveData.postValue(e.message)
        }
    }

    fun loadRestaurantsFiltered(
        filters: List<Filter>,
        restaurants: List<RestaurantItemViewModel>
    ) = viewModelScope.launch {
        try {
            filteredRestaurants.postValue(null)
            //loadRestaurants(fullServiceSelected, fastFoodSelected, pubSelected)

            if (filters.isEmpty()) {
                filteredRestaurants.postValue(restaurants)
                return@launch
            }

            val res = restaurants.filter { restaurant ->
                filters.filter { filter ->
                    filter.isChecked
                }.all { filter ->
                    if (filter.isCuisine) {
                        restaurant.cuisines.any { cuisine ->
                            cuisine.name.contains(filter.name, ignoreCase = true)
                        }
                    } else {
                        restaurant.dietaryRestrictions.any { dietaryRestriction ->
                            dietaryRestriction.name.contains(filter.name, ignoreCase = true)
                        }
                    }
                }
            }

            filteredRestaurants.postValue(res)
        } catch (e: ApiException) {
            errorLiveData.postValue(e.message)
        }
    }

    fun loadRestaurants(
        fullServiceSelected: Boolean,
        fastFoodSelected: Boolean,
        pubSelected: Boolean
    ) = viewModelScope.launch {
        try {
            restaurants.postValue(null)
            val res = restaurantsService.getRestaurants()
                .filter { restaurant ->
                    !(restaurant.subcategoryKey == "fast_food" && !fastFoodSelected)
                }.filter { restaurant ->
                    !(restaurant.subcategoryKey == "sit_down" && !fullServiceSelected)
                }.filter { restaurant ->
                    !(restaurant.subcategoryKey == "cafe" && !pubSelected)
                }
                .map { restaurant ->
                    RestaurantItemViewModel(restaurant)
                }

            restaurants.postValue(res)
        } catch (e: ApiException) {
            errorLiveData.postValue(e.message)
        }
    }

    fun loadRestaurantDetails(selectedMarkerId: String) = viewModelScope.launch {
        try {
            val res =
                RestaurantItemViewModel(restaurantsService.getRestaurantById(selectedMarkerId))

            restaurantDetails.postValue(res)
        } catch (e: ApiException) {
            errorLiveData.postValue(e.message)
        }
    }

    val username: String
        get() = restaurantsService.username

    fun clearPreferences() {
        restaurantsService.clearPreferences()
    }

    var firstTimeLocationPermissions: Boolean
        get() = locationService.firstTimeLocationPermissions
        set(value) {
            locationService.firstTimeLocationPermissions = value
        }

    fun startDeviceLocationUpdates() {
        locationService.setLocationAccuracy(LocationAccuracy.HIGH, 30_000)
        locationService.enableLocationUpdates()
    }

    fun stopDeviceLocationUpdates() {
        locationService.disableLocationUpdates()
    }

    suspend fun awaitDeviceLocation(): Location =
        locationService.getLocation().filter { location -> location != UnknownLocation }.first()

    fun isLocationEnabled() = locationService.isLocationEnabled()
}

data class RestaurantItemViewModel(val restaurant: Restaurant) {
    val id = restaurant.locationId
    val name = restaurant.name
    val address = restaurant.address
    val latitude = restaurant.latitude.toDouble()
    val longitude = restaurant.longitude.toDouble()
    val subcategoryKey = restaurant.subcategoryKey
    val price = restaurant.price
    val rating = restaurant.rating
    val cuisines = restaurant.cuisine
    val dietaryRestrictions = restaurant.dietaryRestrictions
}

class RestaurantsViewModelFactory(
    private val context: Context,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RestaurantsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RestaurantsViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
