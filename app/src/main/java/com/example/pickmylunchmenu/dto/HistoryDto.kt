package com.example.pickmylunchmenu.dto

import android.os.Parcelable
import com.example.pickmylunchmenu.entity.History
import kotlinx.parcelize.Parcelize

@Parcelize
data class HistoryDto(
    val id: Int = 0,
    val name: String,
    val rating: Float,
    var myRating: Float = 0F,
    var date : String = "",
    var comment: String = "",
    var lat: String = "",
    var lng: String = ""
) : Parcelable {
    fun toEntity() : History = History(
        id = this.id,
        name = this.name,
        rating = this.rating,
        myRating = this.myRating,
        date = this.date,
        comment = this.comment)
}
