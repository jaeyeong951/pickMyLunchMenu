package com.example.pickmylunchmenu.ui.main_two

import androidx.lifecycle.LiveData
import com.example.pickmylunchmenu.base.BaseViewModel
import com.example.pickmylunchmenu.dto.FavoriteDto
import com.example.pickmylunchmenu.repository.RestaurantRepository
import com.example.pickmylunchmenu.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainTwoViewModel @Inject constructor(private val restaurantRepository: RestaurantRepository): BaseViewModel() {
    private val _isGetFavoritesFinished: SingleLiveEvent<Any> = SingleLiveEvent()
    val isGetFavoritesFinished: LiveData<Any> get() = _isGetFavoritesFinished

    var favorites : List<FavoriteDto> = ArrayList()

    fun getFavorites(userId: Long) {
        apiCall(restaurantRepository.getFavorites(userId),
        onSuccess = {
            favorites = it
            _isGetFavoritesFinished.call()
        })
    }
}