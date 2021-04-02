package com.example.pickmylunchmenu.dto

data class ApiResponse(
    val result : RESPONSE
) {
    enum class RESPONSE {
        SUCCESS, FAIL
    }
}
