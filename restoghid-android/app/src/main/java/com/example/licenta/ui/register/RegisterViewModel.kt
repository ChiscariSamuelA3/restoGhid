package com.example.licenta.ui.register

import android.content.Context
import androidx.lifecycle.*
import com.example.licenta.services.UsersService
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class RegisterViewModel(context: Context) : ViewModel() {
    private var usersService: UsersService = UsersService(context)

    private val registerResponse: MutableLiveData<String> = MutableLiveData(null)
    private val errorLiveData: MutableLiveData<String?> = MutableLiveData(null)

    fun getRegisterResponse(): LiveData<String> = registerResponse
    fun getErrorLiveData(): LiveData<String?> = errorLiveData

    fun register(username: String, email: String, password: String) = viewModelScope.launch {
        try {
            val registeredUsername = usersService.register(username, email, password)
            registerResponse.postValue(registeredUsername)
        } catch (e: Exception) {
            errorLiveData.postValue(e.message)
        }
    }
}

class RegisterViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegisterViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}