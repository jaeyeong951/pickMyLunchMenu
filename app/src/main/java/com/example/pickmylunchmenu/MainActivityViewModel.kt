package com.example.pickmylunchmenu

import androidx.lifecycle.LiveData
import com.example.pickmylunchmenu.base.BaseViewModel
import com.example.pickmylunchmenu.dto.*
import com.example.pickmylunchmenu.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(): BaseViewModel() {
    private val _testValue: SingleLiveEvent<Int> = SingleLiveEvent()
    val testValue: LiveData<Int> get() = _testValue

    private val _totalPrice: SingleLiveEvent<Int> = SingleLiveEvent()
    val totalPrice: LiveData<Int> get() = _totalPrice

    var cachedUser: UserDto? = null

    var cachedRestaurantList: List<NearByRestaurantItem> = ArrayList()

    var orderMenuList = ArrayList<OrderMenuDto>()

    var orderDtoList = ArrayList<OrderDto>()

    var delayedPayments: List<PaymentDto> = ArrayList()
    var remainingDays = 0

    var priceForPurchase = 0

    fun setTestValue(v: Int) {
        _testValue.postValue(v)
    }

    fun setTotalPrice(v: Int) {
        _totalPrice.postValue(v)
    }

    fun clearOrder() {
        orderDtoList.clear()
        orderMenuList.clear()
    }
}