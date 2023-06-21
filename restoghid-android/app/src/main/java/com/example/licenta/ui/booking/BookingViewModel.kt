package com.example.licenta.ui.booking

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.licenta.api.error.ApiException
import com.example.licenta.models.Booking
import com.example.licenta.models.Restaurant
import com.example.licenta.services.BookingService
import com.example.licenta.services.RestaurantsService
import kotlinx.coroutines.launch
import java.time.LocalDate

class BookingViewModel(context: Context) : ViewModel() {
    private val restaurantsService: RestaurantsService = RestaurantsService(context)
    private val bookingService: BookingService = BookingService(context)

    private val bookingDetails: MutableLiveData<BookingItemViewModel> =
        MutableLiveData(null)

    private val restaurantDetails: MutableLiveData<RestaurantItemViewModel> =
        MutableLiveData(null)

    private val errorLiveData: MutableLiveData<String?> = MutableLiveData(null)

    fun getErrorLiveData() = errorLiveData

    fun getRestaurantDetails() = restaurantDetails

    fun getBookingDetails() = bookingDetails

    fun loadRestaurantDetails(selectedMarkerId: String) = viewModelScope.launch {
        try {
            val res = RestaurantItemViewModel(
                restaurantsService.getRestaurantById(selectedMarkerId)
            )

            Log.d("BookingViewModel", res.totalSlots.toString())

            restaurantDetails.postValue(res)
        } catch (e: ApiException) {
            errorLiveData.postValue(e.message)
        }
    }

    fun createBooking(
        restaurantId: String,
        date: LocalDate,
        mealType: Int,
        totalSeats: Int,
        selectedInterval: Int,
        bookedSeats: Int,
        userPhone: String,
    ) = viewModelScope.launch {
        try {
            val res = BookingItemViewModel(
                bookingService.addBooking(
                    restaurantId,
                    date,
                    mealType,
                    selectedInterval,
                    bookedSeats,
                    totalSeats,
                    userPhone
                )
            )

            bookingDetails.postValue(res)
        } catch (e: ApiException) {
            errorLiveData.postValue(e.message)
        }
    }

    fun refreshBookingData() {
        bookingDetails.postValue(null)
    }
}

data class RestaurantItemViewModel(val restaurant: Restaurant) {
    val id = restaurant.locationId
    val name = restaurant.name
    val address = restaurant.address
    val availableSlotsMorning = restaurant.totalSeats
    val availableSlotsAfternoon = restaurant.totalSeats
    val availableSlotsEvening = restaurant.totalSeats
    val totalSlots = restaurant.totalSeats
    val bookings = restaurant.bookings
}

data class BookingItemViewModel(
    val booking: Booking
) {
    val date = booking.localDate
}

class BookingViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BookingViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}