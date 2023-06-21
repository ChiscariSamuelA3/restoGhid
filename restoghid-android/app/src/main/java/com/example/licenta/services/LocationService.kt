package com.example.licenta.services

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import androidx.core.location.LocationManagerCompat
import com.example.licenta.storage.LocalStorage
import com.google.android.gms.location.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

fun locationFor(lat: Double, lng: Double) = Location("app").apply {
    latitude = lat
    longitude = lng
}

object UnknownLocation : Location(locationFor(0.0, 0.0))

enum class LocationAccuracy {
    HIGH, BALANCED, LOW, PASSIVE
}

class LocationService(private val context: Context) {

    companion object {
        private var instance: LocationService? = null

        fun getInstance(context: Context): LocationService {
            if (instance == null) {
                instance = LocationService(context)
            }

            return instance!!
        }
    }

    private val localStorage: LocalStorage = LocalStorage(context)

    private val locationFlow = MutableSharedFlow<Location>(replay = 1)
    private val locationClient: FusedLocationProviderClient

    private var enabled = false
    private var requestAccuracy = LocationAccuracy.PASSIVE
    private var requestInterval = -1L

    private val passiveLocationUpdateCallback = LocationListener()
    private val activeLocationUpdateCallback = LocationListener()

    init {
        locationFlow.tryEmit(UnknownLocation)
        locationClient = LocationServices.getFusedLocationProviderClient(context)
    }

    var firstTimeLocationPermissions: Boolean
        get() = localStorage.getFirstTimeLocationPermissions()
        set(value) {
            localStorage.setFirstTimeLocationPermissions(value)
        }


    fun isLocationEnabled(): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return LocationManagerCompat.isLocationEnabled(locationManager)
    }

    fun getLocation(): Flow<Location> = locationFlow.asSharedFlow()

    @SuppressLint("MissingPermission")
    fun enableLocationUpdates() {
        if (enabled) return
        enabled = true

        locationClient.lastLocation.addOnSuccessListener {
            if (it != null) {
                locationFlow.tryEmit(it)
            }
        }

        val request = LocationRequest.create().setPriority(Priority.PRIORITY_HIGH_ACCURACY)

        locationClient.requestLocationUpdates(
            request,
            passiveLocationUpdateCallback,
            Looper.getMainLooper()
        )

        setLocationAccuracyInternal(requestAccuracy, requestInterval)
    }

    fun disableLocationUpdates() {
        if (!enabled) return
        enabled = false

        locationClient.removeLocationUpdates(passiveLocationUpdateCallback)
        if (requestAccuracy != LocationAccuracy.PASSIVE) {
            locationClient.removeLocationUpdates(activeLocationUpdateCallback)
        }
    }

    fun setLocationAccuracy(accuracy: LocationAccuracy, intervalMillis: Long = -1L) {
        if (accuracy == requestAccuracy && intervalMillis == requestInterval) return
        setLocationAccuracyInternal(accuracy, intervalMillis)
    }

    @SuppressLint("MissingPermission")
    private fun setLocationAccuracyInternal(accuracy: LocationAccuracy, interval: Long = -1L) {
        requestAccuracy = accuracy
        requestInterval = interval

        if (!enabled) return

        locationClient.removeLocationUpdates(activeLocationUpdateCallback)

        if (accuracy == LocationAccuracy.PASSIVE) return

        val request = when (accuracy) {
            LocationAccuracy.HIGH -> LocationRequest.create()
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            LocationAccuracy.BALANCED -> LocationRequest.create()
                .setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
            LocationAccuracy.LOW -> LocationRequest.create()
                .setPriority(Priority.PRIORITY_LOW_POWER)
            LocationAccuracy.PASSIVE -> throw AssertionError()
        }

        if (interval >= 0) {
            request.interval = interval
        }

        locationClient.requestLocationUpdates(
            request,
            activeLocationUpdateCallback,
            Looper.getMainLooper()
        )
    }

    private inner class LocationListener : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            if (result.lastLocation != UnknownLocation && result.lastLocation != null) {
                locationFlow.tryEmit(result.lastLocation!!)
            }
        }
    }
}