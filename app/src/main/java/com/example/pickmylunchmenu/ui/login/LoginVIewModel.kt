package com.example.pickmylunchmenu.ui.login

import androidx.lifecycle.LiveData
import com.example.pickmylunchmenu.base.BaseViewModel
import com.example.pickmylunchmenu.dto.UserDto
import com.example.pickmylunchmenu.repository.UserRepository
import com.example.pickmylunchmenu.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginVIewModel @Inject constructor(private val userRepository: UserRepository): BaseViewModel() {
    private val _isLoginFinished: SingleLiveEvent<Any> = SingleLiveEvent()
    val isLoginFinished: LiveData<Any> get() = _isLoginFinished
    private val _isLoginFailed: SingleLiveEvent<Any> = SingleLiveEvent()
    val isLoginFailed: LiveData<Any> get() = _isLoginFailed

    var user: UserDto? = null

    fun login(userId: String, password: String) {
        apiCall(userRepository.login(userId, password),
        onSuccess = {
            if(it.login) {
                user = it
                _isLoginFinished.call()
            }
            else {
                _isLoginFailed.call()
            }
        },
        indicator = true)
    }
}