package com.example.pickmylunchmenu.service

import com.example.pickmylunchmenu.dto.*
import io.reactivex.Single
import retrofit2.http.*

interface RestaurantService {
    @GET("/get_restaurant")
    fun getRestaurant(
        @Query("current_x") currentX : Double,
        @Query("current_y") currentY : Double,
        @Query("boundary") boundary : Double
    ) : Single<List<NearByRestaurantItem>>

    @GET("/get_restaurant")
    fun getRestaurant(
        @Query("current_x") currentX : Double,
        @Query("current_y") currentY : Double,
        @Query("boundary") boundary : Double,
        @Query("category") category: String
    ) : Single<List<NearByRestaurantItem>>

    @POST("/submit_order")
    fun submitOrder(
        @Query("id") id : Long,
        @Body menus: List<OrderMenuDto>
    ) : Single<ApiResponse.RESPONSE>

    @GET("/get_orders")
    fun getOrders(
        @Query("id") id : Long
    ) : Single<List<OrderDto>?>

    @DELETE("/delete_order")
    fun deleteOrder(
        @Query("id") id : Long
    ) : Single<ApiResponse.RESPONSE>

    @PUT("/put_favorite")
    fun putFavorite(
        @Query("user_id") userId: Long,
        @Query("restaurant_id") restaurantId: Long
    ) : Single<ApiResponse.RESPONSE>

    @GET("/get_favorites")
    fun getFavorites(
        @Query("user_id") userId: Long
    ) : Single<List<FavoriteDto>>

    @GET("/get_favorites_by_restaurant")
    fun getFavoritesByRestaurant(
        @Query("restaurant_id") restaurantId: Long
    ) : Single<List<FavoriteDto>>

    @DELETE("/delete_favorites")
    fun deleteFavorite(
        @Query("id") id: Long
    ) : Single<ApiResponse.RESPONSE>

    @POST("/update_menus")
    fun updateOrders(
        @Body menus: List<OrderDetailDto>,
        @Query("order_id") order_id: Long
    ) : Single<ApiResponse.RESPONSE>

    @PUT("/payment")
    fun payment(
        @Query("order_id") orderID: List<Long>,
        @Query("method") method: String?
    ) : Single<ResultDTO<PaymentDto>>

    @GET("/get_delayed_payments")
    fun getDelayedPayments(
        @Query("user_id") userID: Long
    ) : Single<List<PaymentDto>>

    @POST("/proceed-delayed-payment")
    fun proceedDelayedPayment(
        @Query("payment_id") paymentID: Long,
        @Query("method") method: String
    ) : Single<ResultDTO<PaymentDto>>

    @POST("/cancel-delayed_payment")
    fun cancelDelayedPayment(
        @Query("payment_id") paymentID: Long
    ) : Single<ResultDTO<PaymentDto>>
}