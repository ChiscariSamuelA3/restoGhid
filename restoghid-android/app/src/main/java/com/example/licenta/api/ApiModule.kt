package com.example.licenta.api

import android.content.Context
import android.util.Log
import com.example.licenta.BuildConfig
import com.example.licenta.api.endpoints.*
import com.example.licenta.storage.LocalStorage
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiModule(context: Context) {

    private val localStorage = LocalStorage(context)

    private val client: OkHttpClient
        get() = OkHttpClient.Builder().also { client ->
            val token = localStorage.getToken()

            if (token.isNotEmpty()) {
                // print token in logcat
                Log.d("_LOGIN", "$token   CLIENT")

                client.addInterceptor(Interceptor { chain ->
                    val request =
                        chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer $token")
                            .build()
                    chain.proceed(request)
                })
            }

            if (BuildConfig.DEBUG) {
                val logging = HttpLoggingInterceptor()
                logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                client.addInterceptor(logging)
            }
        }.build()

    private val retrofit: Retrofit
        get() = Retrofit.Builder()
            .baseUrl("http://192.168.1.137:5000/api/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val restaurantsApi: RestaurantsApi
        get() = retrofit.create(RestaurantsApi::class.java)

    val photosApi: PhotosApi
        get() = retrofit.create(PhotosApi::class.java)

    val usersApi: UsersApi
        get() = retrofit.create(UsersApi::class.java)

    val reviewsApi: ReviewsApi
        get() = retrofit.create(ReviewsApi::class.java)

    val bookingApi: BookingApi
        get() = retrofit.create(BookingApi::class.java)

    val cuisinesApi: CuisinesApi
        get() = retrofit.create(CuisinesApi::class.java)
}