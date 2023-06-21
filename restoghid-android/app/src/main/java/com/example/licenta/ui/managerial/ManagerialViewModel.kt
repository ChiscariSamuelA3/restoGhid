package com.example.licenta.ui.managerial

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.licenta.services.UsersService

class ManagerialViewModel(context: Context) : ViewModel() {
    private val userService: UsersService = UsersService(context)

    fun clearPreferences() {
        userService.clearPreferences()
    }
}

class ManagerialViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ManagerialViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ManagerialViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}