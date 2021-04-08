package com.example.pickmylunchmenu.ui.welcome

import androidx.lifecycle.LiveData
import com.example.pickmylunchmenu.base.BaseViewModel
import com.example.pickmylunchmenu.dto.NearByRestaurantItem
import com.example.pickmylunchmenu.dto.PaymentDto
import com.example.pickmylunchmenu.repository.RestaurantRepository
import com.example.pickmylunchmenu.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(private val restaurantRepository: RestaurantRepository) : BaseViewModel() {
    private val _isLoadRestaurantFinished: SingleLiveEvent<Any> = SingleLiveEvent()
    val isLoadRestaurantFinished: LiveData<Any> get() = _isLoadRestaurantFinished
    private val _isGetDelayedPaymentsFinished: SingleLiveEvent<Any> = SingleLiveEvent()
    val isGetDelayedPaymentsFinished: LiveData<Any> get() = _isGetDelayedPaymentsFinished

    var restaurantList: List<NearByRestaurantItem> = ArrayList()

    var delayedPayments: List<PaymentDto> = ArrayList()

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

    fun getDelayedPayments(userID: Long) {
        apiCall(restaurantRepository.getDelayedPayments(userID),
        onSuccess = {
            delayedPayments = it
            _isGetDelayedPaymentsFinished.call()
        }, indicator = false)
    }
}