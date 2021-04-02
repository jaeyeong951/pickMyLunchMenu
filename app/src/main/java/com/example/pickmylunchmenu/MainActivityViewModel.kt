package com.example.pickmylunchmenu

import androidx.lifecycle.LiveData
import com.example.pickmylunchmenu.base.BaseViewModel
import com.example.pickmylunchmenu.dto.NearByRestaurantItem
import com.example.pickmylunchmenu.dto.OrderMenuDto
import com.example.pickmylunchmenu.dto.UserDto
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

    var orderMenuList: MutableList<OrderMenuDto> = ArrayList()

    fun setTestValue(v: Int) {
        _testValue.postValue(v)
    }

    fun setTotalPrice(v: Int) {
        _totalPrice.postValue(v)
    }
}