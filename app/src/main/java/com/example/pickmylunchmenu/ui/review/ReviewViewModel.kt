package com.example.pickmylunchmenu.ui.review

import androidx.lifecycle.LiveData
import com.example.pickmylunchmenu.base.BaseViewModel
import com.example.pickmylunchmenu.dto.HistoryDto
import com.example.pickmylunchmenu.repository.HistoryRepository
import com.example.pickmylunchmenu.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(private val historyRepository: HistoryRepository): BaseViewModel() {
    private val _isLoadHistoryFinished: SingleLiveEvent<Any> = SingleLiveEvent()
    val isLoadHistoryFinished: LiveData<Any> get() = _isLoadHistoryFinished
    private val _isLoadHistoryFailed: SingleLiveEvent<Any> = SingleLiveEvent()
    val isLoadHistoryFailed: LiveData<Any> get() = _isLoadHistoryFailed
    private val _isSavedHistoryFinished: SingleLiveEvent<Any> = SingleLiveEvent()
    val isSaveHistoryFinished: LiveData<Any> get() = _isSavedHistoryFinished
    private val _isSavedHistoryFailed: SingleLiveEvent<Any> = SingleLiveEvent()
    val isSavedHistoryFailed: LiveData<Any> get() = _isSavedHistoryFailed

    var historyList : List<HistoryDto> = ArrayList()

    fun insertHistory(historyDto: HistoryDto) {
        apiCall(historyRepository.insertHistory(historyDto),
            onComplete = {
                _isSavedHistoryFinished.call()
            },
            onError = {
                _isSavedHistoryFailed.call()
            })
    }

    fun getAllHistory() {
        apiCall(historyRepository.getAllHistory(),
            onSuccess = {
                historyList = it
                _isLoadHistoryFinished.call()
            },
            onError = {
                _isLoadHistoryFailed.call()
            })
    }
}