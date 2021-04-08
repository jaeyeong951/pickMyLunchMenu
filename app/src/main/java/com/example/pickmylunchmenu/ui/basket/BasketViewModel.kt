package com.example.pickmylunchmenu.ui.basket

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.pickmylunchmenu.base.BaseViewModel
import com.example.pickmylunchmenu.dto.OrderDto
import com.example.pickmylunchmenu.repository.RestaurantRepository
import com.example.pickmylunchmenu.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.properties.Delegates

@HiltViewModel
class BasketViewModel @Inject constructor(private val restaurantRepository: RestaurantRepository) : BaseViewModel() {
    private val _isGetOrdersFinished: SingleLiveEvent<Any> = SingleLiveEvent()
    val isGetOrdersFinished: LiveData<Any> get() = _isGetOrdersFinished
    private val _isGetOrdersFailed: SingleLiveEvent<Any> = SingleLiveEvent()
    val isGetOrdersFailed: LiveData<Any> get() = _isGetOrdersFailed

    var orderDtoList = ArrayList<OrderDto>()

    var user_id by Delegates.notNull<Long>()

    fun getOrders(id: Long) {
        apiCall(restaurantRepository.getOrders(id),
        onSuccess = {
            if(it == null) _isGetOrdersFailed.call()
            else {
                user_id = id
                orderDtoList = ArrayList(it)
                _isGetOrdersFinished.call()
            }
        },
        onError = {
            it.message?.let {msg ->
                Log.e("오류 발생", msg)
            }
            _isGetOrdersFailed.call()
        }, indicator = true)
    }

    fun deleteOrder(id: Long) {
        apiCall(restaurantRepository.deleteOrder(id),
            onSuccess = {
                getOrders(user_id)
            },
            onError = {
                it.message?.let {msg ->
                    Log.e("오류 발생", msg)
                }

            }, indicator = true)
    }
}