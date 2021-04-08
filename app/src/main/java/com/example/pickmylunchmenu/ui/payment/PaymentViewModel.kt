package com.example.pickmylunchmenu.ui.payment

import androidx.lifecycle.LiveData
import com.example.pickmylunchmenu.base.BaseViewModel
import com.example.pickmylunchmenu.dto.ApiResponse
import com.example.pickmylunchmenu.dto.PaymentDto
import com.example.pickmylunchmenu.repository.RestaurantRepository
import com.example.pickmylunchmenu.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(private val restaurantRepository: RestaurantRepository) : BaseViewModel() {
    private val _isPaymentFinished: SingleLiveEvent<Any> = SingleLiveEvent()
    val isPaymentFinished: LiveData<Any> get() = _isPaymentFinished
    private val _isPaymentFailed: SingleLiveEvent<Any> = SingleLiveEvent()
    val isPaymentFailed: LiveData<Any> get() = _isPaymentFailed
    private val _isPaymentCancelFinished: SingleLiveEvent<Any> = SingleLiveEvent()
    val isPaymentCancelFinished: LiveData<Any> get() = _isPaymentCancelFinished

    var paymentDTO : PaymentDto? = null

    fun cancelDelayedPayment(paymentID: Long) {
        apiCall(restaurantRepository.cancelDelayedPayment(paymentID),
        onSuccess = {
            _isPaymentCancelFinished.call()
        }, indicator = true)
    }

    fun proceedDelayedPayment(paymentID: Long, method: Int) {
        val methodString = when(method) {
            0 -> "카드결제"
            1 -> "실시간 계좌이체"
            else -> "가상계좌"
        }
        apiCall(restaurantRepository.proceedDelayedPayment(paymentID, methodString),
        onSuccess = {
            if(it.result == true) {
                paymentDTO = it.data as PaymentDto
                _isPaymentFinished.call()
            }
            else {
            _isPaymentFailed.call()
        }
        },
        indicator = true)
    }

    fun payment(orderID: List<Long>, method: Int?) {
        val methodString = when(method) {
            0 -> "카드결제"
            1 -> "실시간 계좌이체"
            2 -> "가상계좌"
            else -> null
        }
        apiCall(restaurantRepository.payment(orderID, methodString),
        onSuccess = {
            if(it.result == true) {
                paymentDTO = it.data as PaymentDto
                _isPaymentFinished.call()
            }
            else {
                _isPaymentFailed.call()
            }
        },
        onError = {
            _isPaymentFailed.call()
        }, indicator = true)
    }
}