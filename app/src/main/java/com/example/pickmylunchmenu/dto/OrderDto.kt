package com.example.pickmylunchmenu.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class OrderDto(
    val id: Long,
    val orderDateTime: String,
    val orderDetailList: @RawValue MutableList<OrderDetailDto>,
    var status: String,
    val restaurant: NearByRestaurantItem
) : Parcelable {
}