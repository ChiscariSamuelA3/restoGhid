package com.example.licenta.ui.profile

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.licenta.api.error.ApiException
import com.example.licenta.models.Booking
import com.example.licenta.models.User
import com.example.licenta.services.UsersService
import kotlinx.coroutines.launch

class ProfileViewModel(context: Context) : ViewModel() {
    private val userService: UsersService = UsersService(context)

    private val errorLiveData: MutableLiveData<String?> = MutableLiveData(null)
    private val userData: MutableLiveData<UserItemViewModel> =
        MutableLiveData(null)

    fun getErrorLiveData() = errorLiveData
    fun getUserData() = userData

    fun loadUserData() = viewModelScope.launch {
        try {
            val res =
                userService.getUserData()
            userData.postValue(UserItemViewModel(res))
        } catch (e: ApiException) {
            errorLiveData.postValue(e.message)
        }
    }

    fun deleteBookingById(id: Long) = viewModelScope.launch {
        try {
            userService.deleteBookingById(id)
        } catch (e: ApiException) {
            errorLiveData.postValue(e.message)
        }
    }

    fun clearPreferences() {
        userService.clearPreferences()
    }
}

data class UserItemViewModel(
    val userData: User
) {
    val username: String
        get() = userData.username

    val email: String
        get() = userData.email ?: ""

    val address: String
        get() = userData.address ?: ""

    val bookings: List<Booking>
        get() = userData.bookings ?: listOf()
}

class ProfileViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}