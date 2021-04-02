package com.example.pickmylunchmenu.ui.restaurant

import androidx.lifecycle.LiveData
import com.example.pickmylunchmenu.base.BaseViewModel
import com.example.pickmylunchmenu.dto.ApiResponse
import com.example.pickmylunchmenu.dto.FavoriteDto
import com.example.pickmylunchmenu.dto.OrderMenuDto
import com.example.pickmylunchmenu.repository.RestaurantRepository
import com.example.pickmylunchmenu.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RestaurantViewModel @Inject constructor(private val restaurantRepository: RestaurantRepository): BaseViewModel() {
    private val _isSubmitOrderFinished: SingleLiveEvent<Any> = SingleLiveEvent()
    val isSubmitOrderFinished: LiveData<Any> get() = _isSubmitOrderFinished
    private val _isSubmitOrderFailed: SingleLiveEvent<Any> = SingleLiveEvent()
    val isSubmitOrderFailed: LiveData<Any> get() = _isSubmitOrderFailed
    private val _isPutFavoriteFinished: SingleLiveEvent<Any> = SingleLiveEvent()
    val isPutFavoriteFinished: LiveData<Any> get() = _isPutFavoriteFinished
    private val _isFavoriteExist: SingleLiveEvent<Any> = SingleLiveEvent()
    val isFavoriteExist: LiveData<Any> get() = _isFavoriteExist
    private val _isFavoriteNotExist: SingleLiveEvent<Any> = SingleLiveEvent()
    val isFavoriteNotExist: LiveData<Any> get() = _isFavoriteNotExist
    private val _isDeleteFavoriteFinished: SingleLiveEvent<Any> = SingleLiveEvent()
    val isDeleteFavoriteFinished: LiveData<Any> get() = _isDeleteFavoriteFinished

    var favoriteList : List<FavoriteDto> = ArrayList()

    fun submitOrder(user_id: Long, menus: List<OrderMenuDto>) {
        apiCall(restaurantRepository.submitOrder(user_id, menus),
        onSuccess = {
            if(it == ApiResponse.RESPONSE.SUCCESS) {
                _isSubmitOrderFinished.call()
            }
            else {
                _isSubmitOrderFailed.call()
            }
        },
        onError = {
            _isSubmitOrderFailed.call()
        })
    }

    fun setFavorite(user_id: Long, restaurant_id: Long) {
        apiCall(restaurantRepository.putFavorite(user_id, restaurant_id),
        onSuccess = {
            _isPutFavoriteFinished.call()
        }, indicator = true)
    }

    fun getFavorites(restaurant_id: Long) {
        apiCall(restaurantRepository.getFavoritesByRestaurant(restaurant_id),
        onSuccess = {
            if(it.isEmpty()) {
                _isFavoriteNotExist.call()
            }
            else {
                _isFavoriteExist.call()
                favoriteList = it
            }
        })
    }

    fun deleteFavorite(id: Long) {
        apiCall(restaurantRepository.deleteFavorite(id),
        onSuccess = {
            _isDeleteFavoriteFinished.call()
        })
    }
}