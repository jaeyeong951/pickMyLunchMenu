package com.example.pickmylunchmenu.repository

import com.example.pickmylunchmenu.dto.UserDto
import com.example.pickmylunchmenu.service.UserService
import io.reactivex.Single
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val userService: UserService) : UserRepository {
    companion object{
        var user: UserDto? = null
    }

    override fun login(userId: String, password: String): Single<UserDto> {
        val loginResult = userService.login(userId, password)
        return loginResult
    }
}