package com.example.pickmylunchmenu.repository

import com.example.pickmylunchmenu.dto.*
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

    override fun getRestaurantNearByMe(
        current_x: Double,
        current_y: Double,
        boundary: Double,
        category: String
    ): Single<List<NearByRestaurantItem>> {
        return restaurantService.getRestaurant(current_x, current_y, boundary, category)
    }

    override fun submitOrder(id: Long, menus: List<OrderMenuDto>): Single<ApiResponse.RESPONSE> {
        return restaurantService.submitOrder(id, menus)
    }

    override fun getOrders(id: Long): Single<List<OrderDto>?> {
        return restaurantService.getOrders(id)
    }

    override fun deleteOrder(id: Long): Single<ApiResponse.RESPONSE> {
        return restaurantService.deleteOrder(id)
    }

    override fun putFavorite(userId: Long, restaurantId: Long): Single<ApiResponse.RESPONSE> {
        return restaurantService.putFavorite(userId, restaurantId)
    }

    override fun getFavorites(userId: Long): Single<List<FavoriteDto>> {
        return restaurantService.getFavorites(userId)
    }

    override fun getFavoritesByRestaurant(restaurantId: Long): Single<List<FavoriteDto>> {
        return restaurantService.getFavoritesByRestaurant(restaurantId)
    }

    override fun deleteFavorite(id: Long): Single<ApiResponse.RESPONSE> {
        return restaurantService.deleteFavorite(id)
    }

    override fun updateOrders(
        orderDetails: List<OrderDetailDto>,
        order_id: Long
    ): Single<ApiResponse.RESPONSE> {
        return restaurantService.updateOrders(orderDetails, order_id)
    }
}