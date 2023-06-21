package com.example.licenta.ui.manageinfo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.licenta.api.error.ApiException
import com.example.licenta.dto.RestaurantUpdateDTO
import com.example.licenta.services.RestaurantsService
import com.example.licenta.ui.managebookings.RestaurantItemViewModel
import kotlinx.coroutines.launch

class ManageInfoViewModel(context: Context) : ViewModel() {
    private val restaurantsService: RestaurantsService = RestaurantsService(context)

    private val restaurantDetails: MutableLiveData<RestaurantItemViewModel> =
        MutableLiveData(null)

    private val errorLiveData: MutableLiveData<String?> = MutableLiveData(null)

    fun getErrorLiveData() = errorLiveData

    fun getRestaurantDetails() = restaurantDetails

    fun updateRestaurant(restaurantUpdateDTO: RestaurantUpdateDTO) = viewModelScope.launch {
        try {
            val res = RestaurantItemViewModel(
                restaurantsService.updateRestaurant(restaurantUpdateDTO)
            )
            restaurantDetails.postValue(res)
        } catch (e: ApiException) {
            errorLiveData.postValue(e.message)
        }
    }
}

class ManageInfoViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ManageInfoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ManageInfoViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

