package com.example.pickmylunchmenu.dto

import android.os.Parcelable
import androidx.annotation.Keep
import com.toy.plzPickMyLunchMenu.domain.dto.Review
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Keep
@Parcelize
data class NearByRestaurantItem(
    val doro: String?,
    val id: Int,
    val jibun: String,
    val lat: String,
    val lng: String,
    val loc: @RawValue Any,
    val name: String,
    val tel: String?,
    val reviews: @RawValue List<Review>,
    val rating: Double,
    val user_ratings_total: Int
) : Parcelable