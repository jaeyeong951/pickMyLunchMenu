package com.example.pickmylunchmenu.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PaymentDto(
    val id: Long = 0L,
    val date: String,
    var method: String? = null,
    var isPurchased: Boolean = false,
    var userID: Long,
    var orderList: List<OrderDto> = ArrayList()
) : Parcelable {
}
