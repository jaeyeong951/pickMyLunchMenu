package com.example.pickmylunchmenu.dto

import java.time.LocalDate

data class OrderDto(
    val id: Long,
    val orderDateTime: String,
    val orderDetailList: MutableList<OrderDetailDto>,
    var status: String,
    val restaurant: NearByRestaurantItem
) {
}