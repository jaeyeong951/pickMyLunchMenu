package com.example.pickmylunchmenu.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class History(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "rating") val rating: Float,
    @ColumnInfo(name = "my_rating") val myRating: Float,
    @ColumnInfo(name = "date") val date : String,
    @ColumnInfo(name = "comment") val comment: String
)