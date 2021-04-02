package com.example.pickmylunchmenu.repository

import com.example.pickmylunchmenu.dto.*
import io.reactivex.Single
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query

interface RestaurantRepository {
    fun getRestaurantNearByMe(current_x : Double,
                              current_y : Double,
                              boundary : Double) : Single<List<NearByRestaurantItem>>

    fun getRestaurantNearByMe(current_x : Double,
                              current_y : Double,
                              boundary : Double,
                              category: String) : Single<List<NearByRestaurantItem>>

    fun submitOrder(id: Long, menus: List<OrderMenuDto>) : Single<ApiResponse.RESPONSE>

    fun getOrders(id: Long) : Single<List<OrderDto>?>

    fun deleteOrder(id: Long) : Single<ApiResponse.RESPONSE>

    fun putFavorite(
        userId: Long,
        restaurantId: Long
    ) : Single<ApiResponse.RESPONSE>

    fun getFavorites(
        userId: Long
    ) : Single<List<FavoriteDto>>

    fun getFavoritesByRestaurant(
        restaurantId: Long
    ) : Single<List<FavoriteDto>>

    fun deleteFavorite(
        id: Long
    ) : Single<ApiResponse.RESPONSE>
}