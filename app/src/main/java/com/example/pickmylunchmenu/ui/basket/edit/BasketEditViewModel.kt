package com.example.pickmylunchmenu.ui.basket.edit

import androidx.lifecycle.LiveData
import com.example.pickmylunchmenu.base.BaseViewModel
import com.example.pickmylunchmenu.dto.ApiResponse
import com.example.pickmylunchmenu.dto.OrderDto
import com.example.pickmylunchmenu.repository.RestaurantRepository
import com.example.pickmylunchmenu.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BasketEditViewModel @Inject constructor(private val restaurantRepository: RestaurantRepository): BaseViewModel() {
    private val _isUpdateFinished: SingleLiveEvent<Any> = SingleLiveEvent()
    val isUpdateFinished: LiveData<Any> get() = _isUpdateFinished
    private val _isUpdateFailed: SingleLiveEvent<Any> = SingleLiveEvent()
    val isUpdateFailed: LiveData<Any> get() = _isUpdateFailed
    private val _totalPrice: SingleLiveEvent<Int> = SingleLiveEvent()
    val totalPrice: LiveData<Int> get() = _totalPrice

    var selectedBasket : OrderDto? = null

    fun setTotalPrice(price : Int) {
        _totalPrice.postValue(price)
    }

    fun submitUpdate() {
        apiCall(restaurantRepository.updateOrders(selectedBasket!!.orderDetailList, selectedBasket!!.id),
        onSuccess = {
            if(it == ApiResponse.RESPONSE.SUCCESS) {
                _isUpdateFinished.call()
            }
            else {
                _isUpdateFinished.call()
            }
        })
    }
}