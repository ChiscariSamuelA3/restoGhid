package com.example.licenta.ui.managereviews

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.licenta.api.error.ApiException
import com.example.licenta.models.Review
import com.example.licenta.services.ReviewsService
import kotlinx.coroutines.launch

class ManageReviewsViewModel(context: Context) : ViewModel() {
    private val reviewsService: ReviewsService = ReviewsService(context)

    private val reviews: MutableLiveData<List<Review>> = MutableLiveData(null)
    private val errorLiveData: MutableLiveData<String?> = MutableLiveData(null)

    fun getErrorLiveData() = errorLiveData
    fun getReviews() = reviews

    fun loadRestaurantReviews() = viewModelScope.launch {
        try {
            val res = reviewsService.getRestaurantReviewsByManager()
            reviews.postValue(res)
        } catch (e: ApiException) {
            errorLiveData.postValue(e.message)
        }
    }

    fun updateReview(id: Long, reply: String) = viewModelScope.launch {
        try {
            val res = reviewsService.updateReview(id, reply)
            val list = reviews.value?.toMutableList()
            val index = list?.indexOfFirst { it.id == id }
            if (index != null) {
                list[index] = res
                reviews.postValue(list.toList())
            }
        } catch (e: ApiException) {
            errorLiveData.postValue(e.message)
        }
    }
}

class ManageReviewsViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ManageReviewsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ManageReviewsViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
