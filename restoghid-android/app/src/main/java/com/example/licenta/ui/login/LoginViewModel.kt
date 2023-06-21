package com.example.licenta.ui.login

import android.content.Context
import androidx.lifecycle.*
import com.example.licenta.services.UsersService
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class LoginViewModel(context: Context) : ViewModel() {
    private var usersService: UsersService = UsersService(context)

    private val loginResponse: MutableLiveData<String> = MutableLiveData(null)
    private val errorLiveData: MutableLiveData<String?> = MutableLiveData(null)

    fun getLoginResponse(): LiveData<String> = loginResponse
    fun getErrorLiveData(): LiveData<String?> = errorLiveData

    fun checkLoginState() = usersService.loggedIn
    fun checkIsManager() = usersService.isManager

    fun login(username: String, password: String) = viewModelScope.launch {
        try {
            val loginUsername = usersService.login(username, password)
            loginResponse.postValue(loginUsername)
        } catch (e: Exception) {
            errorLiveData.postValue(e.message)
        }
    }
}

class LoginViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}