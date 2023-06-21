package com.example.licenta.services

import android.content.Context
import android.util.Log
import com.example.licenta.R
import com.example.licenta.api.ApiModule
import com.example.licenta.api.BaseApiCall
import com.example.licenta.api.error.ApiException
import com.example.licenta.api.error.NetworkException
import com.example.licenta.api.utils.CallResult
import com.example.licenta.models.User
import com.example.licenta.storage.LocalStorage

class UsersService(private val context: Context) : BaseApiCall() {
    companion object {
        private var instance: UsersService? = null

        fun getInstance(context: Context): UsersService {
            if (instance == null) {
                instance = UsersService(context)
            }

            return instance!!
        }
    }

    private val localStorage = LocalStorage(context)

    val loggedIn: Boolean
        get() = localStorage.getToken().isNotEmpty()

    val isManager: Boolean
        get() = localStorage.getIsManager()

    var username: String
        get() = localStorage.getUsername()
        set(value) {
            localStorage.setUsername(value)
        }

    suspend fun register(username: String, email: String, password: String): String {
        val newUser = User(email = email, username = username, password = password)
        when (val response =
            safeApiCall { ApiModule(context).usersApi.userRegister(newUser) }) {
            is CallResult.Success -> {
                if (response.value.data != null) {
                    return response.value.data.username
                } else {
                    throw ApiException("API error")
                }
            }
            is CallResult.Failure -> {
                if (response.networkError) {
                    throw NetworkException(context.resources.getString(R.string.error_network))
                } else {
                    throw ApiException(response.errorBody.toString())
                }
            }
        }
    }

    suspend fun login(userName: String, password: String): String {
        val currentUser = User(username = userName, password = password)
        when (val response = safeApiCall { ApiModule(context).usersApi.userLogin(currentUser) }) {
            is CallResult.Success -> {
                if (response.value.data != null) {
                    username = userName
                    localStorage.setToken(response.value.data.token)
                    localStorage.setIsManager(response.value.data.isManager)

                    Log.d("_LOGIN", response.value.data.token)
                    Log.d("_LOGIN", username)

                    return username
                } else {
                    throw ApiException("Login error")
                }
            }
            is CallResult.Failure -> {
                if (response.networkError) {
                    throw NetworkException(context.resources.getString(R.string.error_network))
                } else {
                    throw ApiException(response.errorBody.toString())
                }
            }
        }
    }

    suspend fun getUserData(): User {
        when (val response = safeApiCall {
            ApiModule(context).usersApi.getUserData()
        }) {
            is CallResult.Success -> {
                if (response.value.data != null) {
                    Log.d("_LOGIN", username + "saved - api" + response.value.data.username)

                    return response.value.data
                } else {
                    throw ApiException("API Error")
                }
            }
            is CallResult.Failure -> {
                if (response.networkError) {
                    throw NetworkException("NETWORK Error")
                } else {
                    throw ApiException("API Error")
                }
            }
        }
    }

    suspend fun deleteBookingById(bookingId: Long): String {
        when (val response = safeApiCall {
            ApiModule(context).usersApi.deleteBookingById(bookingId)
        }) {
            is CallResult.Success -> {
                if (response.value.data != null) {
                    return "ok"
                } else {
                    throw ApiException("API Error")
                }
            }
            is CallResult.Failure -> {
                if (response.networkError) {

                    throw NetworkException("NETWORK Error")
                } else {
                    throw ApiException("API Error")
                }
            }
        }
    }

    fun clearPreferences() {
        localStorage.clear()
    }
}