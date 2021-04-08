package com.example.pickmylunchmenu.dto

data class ResultDTO<T>(
    var result: Boolean? = null,
    var data: T? = null
) {
}
