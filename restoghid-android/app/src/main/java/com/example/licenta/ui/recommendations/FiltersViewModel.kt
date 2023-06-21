package com.example.licenta.ui.recommendations

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class FiltersViewModel(context: Context) : ViewModel()  {
}

class FiltersViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FiltersViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FiltersViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}