package com.example.licenta.ui.reviews

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.licenta.api.error.ApiException
import com.example.licenta.models.Review
import com.example.licenta.services.ReviewsService
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class ReviewsViewModel(context: Context) : ViewModel() {
    private val reviewsService: ReviewsService = ReviewsService(context)

    private val reviews: MutableLiveData<List<Review>> = MutableLiveData(null)
    private val newReview: MutableLiveData<Review> = MutableLiveData(null)
    private val errorLiveData: MutableLiveData<String?> = MutableLiveData(null)

    fun getErrorLiveData() = errorLiveData
    fun getReviews() = reviews
    fun getNewReview() = newReview

    fun loadRestaurantReviews(restaurantId: String) = viewModelScope.launch {
        try {
            val res = reviewsService.getRestaurantReviews(restaurantId)
            reviews.postValue(res)
        } catch (e: ApiException) {
            errorLiveData.postValue(e.message)
        }
    }

    fun addReview(restaurantId: String, review: Review) = viewModelScope.launch {
        try {
            val res = reviewsService.addReview(restaurantId, review)
            newReview.postValue(res)
        } catch (e: ApiException) {
            errorLiveData.postValue(e.message)
        }
    }
}

class ReviewsViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReviewsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReviewsViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}