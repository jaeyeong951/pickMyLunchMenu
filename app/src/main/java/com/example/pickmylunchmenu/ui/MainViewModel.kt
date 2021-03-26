package com.example.pickmylunchmenu.ui

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.pickmylunchmenu.R
import com.example.pickmylunchmenu.base.BaseViewModel
import com.example.pickmylunchmenu.dto.HistoryDto
import com.example.pickmylunchmenu.dto.NearByRestaurantItem
import com.example.pickmylunchmenu.repository.HistoryRepository
import com.example.pickmylunchmenu.repository.RestaurantRepository
import com.example.pickmylunchmenu.util.SingleLiveEvent
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val restaurantRepository: RestaurantRepository,
                                        private val historyRepository: HistoryRepository) : BaseViewModel() {
    private val _isLoadRestaurantFinished: SingleLiveEvent<Any> = SingleLiveEvent()
    val isLoadRestaurantFinished: LiveData<Any> get() = _isLoadRestaurantFinished
    private val _isRandomListFinished: SingleLiveEvent<List<NearByRestaurantItem>> = SingleLiveEvent()
    val isRandomListFinished: LiveData<List<NearByRestaurantItem>> get() = _isRandomListFinished
    private val _isSavedHistoryFinished: SingleLiveEvent<Any> = SingleLiveEvent()
    val isSaveHistoryFinished: LiveData<Any> get() = _isSavedHistoryFinished
    private val _isSavedHistoryFailed: SingleLiveEvent<Any> = SingleLiveEvent()
    val isSavedHistoryFailed: LiveData<Any> get() = _isSavedHistoryFailed
    private val _isMoveCameraCalled: SingleLiveEvent<Pair<Double, Double>> = SingleLiveEvent()
    val isMoveCameraCalled: LiveData<Pair<Double, Double>> get() = _isMoveCameraCalled

    var restaurantList : ArrayList<NearByRestaurantItem> = ArrayList()
    var restaurantListModified : ArrayList<NearByRestaurantItem> = ArrayList()
    val markerList : ArrayList<Marker> = ArrayList()
    var isLoaded = false
    var expanded = false

    var historyList : List<HistoryDto> = ArrayList()

    var totalReview : Int = 0

    var historyDto : HistoryDto? = null

    fun getRestaurantNearByMe(currentX : Double,
                              currentY : Double,
                              boundary : Double) {
        apiCall<ArrayList<NearByRestaurantItem>>(restaurantRepository.getRestaurantNearByMe(currentX, currentY, boundary),
        onSuccess = {
            restaurantList.clear()
            totalReview = 0
            val cont = it
            cont.sortByDescending { nearByRestaurant ->
                nearByRestaurant.rating * (nearByRestaurant.user_ratings_total / 100)
            }
            restaurantListModified = cont
            restaurantList = cont
            for(item in cont) {
                totalReview += item.user_ratings_total
                val marker = Marker()
                marker.apply {
                    position = LatLng(item.lng.toDouble(), item.lat.toDouble())
                    captionText = item.name
                    isIconPerspectiveEnabled = true
                    icon = OverlayImage.fromResource(R.drawable.gastronomy)
                    width = 70
                    height = 70
                }
                markerList.add(marker)
            }
            _isLoadRestaurantFinished.call()
        },
        onError = {

        },
        indicator = true)
    }

    fun pick5RandomRestaurantBasedOnReview(){
        _isRandomListFinished.postValue(restaurantList.filter { it.rating.toFloat() >= 4.0 }.shuffled().subList(0, 3))
    }

    fun callIsFinished() {
        _isLoadRestaurantFinished.call()
    }

    fun insertHistory(historyDto: HistoryDto) {
        apiCall(historyRepository.insertHistory(historyDto),
        onComplete = {
            _isSavedHistoryFinished.call()
        },
        onError = {
            _isSavedHistoryFailed.call()
        })
    }

    fun callCameraUpdate(lat : Double, lng : Double) {
        _isMoveCameraCalled.postValue(Pair(lat, lng))
    }

}