package com.example.licenta.storage

import android.content.Context
import android.content.Context.MODE_PRIVATE

const val KEY_APP_PREFERENCES = "app.preferences"
const val KEY_FIRST_TIME_LOCATION_PERMISSIONS = "app.permissions.location"

const val KEY_USERNAME = "user.username"
const val KEY_TOKEN = "token"
const val KEY_IS_MANAGER = "isManager"

class LocalStorage(context: Context) {

    companion object {
        private var instance: LocalStorage? = null

        fun getInstance(context: Context): LocalStorage {
            if (instance == null) {
                instance = LocalStorage(context)
            }
            return instance!!
        }
    }

    private val sharedPreferences = context.getSharedPreferences(KEY_APP_PREFERENCES, MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    fun clear() {
        editor.clear().apply()
    }

    fun setUsername(name: String) {
        editor.putString(KEY_USERNAME, name).apply()
    }

    fun getUsername() = sharedPreferences.getString(KEY_USERNAME, "")!!

    fun setIsManager(isManager: Boolean) {
        editor.putBoolean(KEY_IS_MANAGER, isManager).apply()
    }

    fun getIsManager() = sharedPreferences.getBoolean(KEY_IS_MANAGER, false)

    fun setToken(token: String) {
        editor.putString(KEY_TOKEN, token).apply()
    }

    fun getToken() = sharedPreferences.getString(KEY_TOKEN, "")!!


    fun setFirstTimeLocationPermissions(visible: Boolean) {
        editor.putBoolean(KEY_FIRST_TIME_LOCATION_PERMISSIONS, visible).apply()
    }

    fun getFirstTimeLocationPermissions() =
        sharedPreferences.getBoolean(KEY_FIRST_TIME_LOCATION_PERMISSIONS, false)
}