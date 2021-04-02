package com.example.pickmylunchmenu.repository

import com.example.pickmylunchmenu.dto.UserDto
import io.reactivex.Single

interface UserRepository {
    fun login(userId: String, password: String) : Single<UserDto>
}