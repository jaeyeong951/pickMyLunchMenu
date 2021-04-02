package com.example.pickmylunchmenu.service

import com.example.pickmylunchmenu.dto.UserDto
import io.reactivex.Single
import retrofit2.http.POST
import retrofit2.http.Query

interface UserService {
    @POST("/login")
    fun login(
        @Query("userId") userId: String,
        @Query("password") password: String
    ) : Single<UserDto>
}