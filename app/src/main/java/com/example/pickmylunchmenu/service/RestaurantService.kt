package com.example.pickmylunchmenu.service

import com.example.pickmylunchmenu.dto.NearByRestaurantItem
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface RestaurantService {
    @GET("/get_restaurant")
    fun getRestaurant(
        @Query("current_x") currentX : Double,
        @Query("current_y") currentY : Double,
        @Query("boundary") boundary : Double
    ) : Single<List<NearByRestaurantItem>>
}