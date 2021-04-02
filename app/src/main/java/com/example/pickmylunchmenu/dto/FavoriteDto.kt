package com.example.pickmylunchmenu.dto

data class FavoriteDto(
    val id: Long = 0,
    val restaurantDto: NearByRestaurantItem
) {
}
