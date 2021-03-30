package com.example.pickmylunchmenu.repository

import com.example.pickmylunchmenu.dto.NearByRestaurantItem
import com.example.pickmylunchmenu.service.RestaurantService
import io.reactivex.Single
import javax.inject.Inject

class RestaurantRepositoryImpl @Inject constructor(private val restaurantService: RestaurantService): RestaurantRepository {
    override fun getRestaurantNearByMe(
        current_x: Double,
        current_y: Double,
        boundary: Double
    ): Single<List<NearByRestaurantItem>> {
       return restaurantService.getRestaurant(current_x, current_y, boundary)
    }
}