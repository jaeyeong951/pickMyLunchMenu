package com.example.pickmylunchmenu.ui.welcome

import androidx.lifecycle.LiveData
import com.example.pickmylunchmenu.base.BaseViewModel
import com.example.pickmylunchmenu.dto.NearByRestaurantItem
import com.example.pickmylunchmenu.repository.RestaurantRepository
import com.example.pickmylunchmenu.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(private val restaurantRepository: RestaurantRepository) : BaseViewModel() {
    private val _isLoadRestaurantFinished: SingleLiveEvent<Any> = SingleLiveEvent()
    val isLoadRestaurantFinished: LiveData<Any> get() = _isLoadRestaurantFinished

    var restaurantList: List<NearByRestaurantItem> = ArrayList()

    fun getRestaurantByCategory(category: String) {
        apiCall(restaurantRepository.getRestaurantNearByMe(35.154324, 129.057481, 0.25, category),
        onSuccess = {
            restaurantList = it
            _isLoadRestaurantFinished.call()
        },
        onError = {

        },
        indicator = true)
    }
}