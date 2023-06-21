package com.example.licenta.ui.auth.onboarding

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.licenta.services.UsersService
import java.lang.IllegalArgumentException

class AuthViewModel(context: Context) : ViewModel() {
    private var userService: UsersService = UsersService(context)

    fun clearPreferences() {
        userService.clearPreferences()
    }

    fun checkLoginState() = userService.loggedIn
}

class AuthViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
