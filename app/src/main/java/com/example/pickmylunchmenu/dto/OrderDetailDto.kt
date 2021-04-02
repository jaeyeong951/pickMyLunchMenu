package com.example.pickmylunchmenu.dto

data class OrderDetailDto(
    val id: Long,
    val menuName: String,
    val price: Int,
    val count: Int,
    val menu_id: Long
) {
}