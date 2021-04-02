package com.example.pickmylunchmenu.dto

import android.os.Parcelable
import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.toy.plzPickMyLunchMenu.domain.dto.Review
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Keep
@Parcelize
//@JsonClass(generateAdapter = true)
data class NearByRestaurantItem(
    val doro: String?,
    val id: Long,
    val jibun: String,
    val lat: String,
    val lng: String,
    val loc: @RawValue Any,
    val name: String,
    val tel: String?,
    val reviews: @RawValue List<Review>,
    val rating: Double,
    val user_ratings_total: Int,
    val category: String,
    val distance: Double = 0.0,
    val menus: @RawValue List<MenuDto>
) : Parcelable