package com.example.pickmylunchmenu.repository

import com.example.pickmylunchmenu.dto.NearByRestaurantItem
import io.reactivex.Single

interface RestaurantRepository {
    fun getRestaurantNearByMe(current_x : Double,
                              current_y : Double,
                              boundary : Double) : Single<List<NearByRestaurantItem>>
}