package com.example.pickmylunchmenu.dto

import com.toy.plzPickMyLunchMenu.domain.dto.Review

data class UserDto(
    val uid: Long = 0,
    val userId: String,
    var passwrod: String,
    var token: String?,
    var login: Boolean = false,
    var reviews: MutableList<Review> = ArrayList()
) {
}
