package com.example.pickmylunchmenu.ui.history

import androidx.lifecycle.LiveData
import com.example.pickmylunchmenu.base.BaseViewModel
import com.example.pickmylunchmenu.dto.HistoryDto
import com.example.pickmylunchmenu.repository.HistoryRepository
import com.example.pickmylunchmenu.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(private val historyRepository: HistoryRepository): BaseViewModel() {
    private val _isLoadHistoryFinished: SingleLiveEvent<Any> = SingleLiveEvent()
    val isLoadHistoryFinished: LiveData<Any> get() = _isLoadHistoryFinished
    private val _isLoadHistoryFailed: SingleLiveEvent<Any> = SingleLiveEvent()
    val isLoadHistoryFailed: LiveData<Any> get() = _isLoadHistoryFailed
    private val _inDeleteHistoryFinished: SingleLiveEvent<Any> = SingleLiveEvent()
    val inDeleteHistoryFinished: LiveData<Any> get() = _inDeleteHistoryFinished

    var historyList : List<HistoryDto> = ArrayList()

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

    fun deleteHistory(position: Int) {
        apiCall(historyRepository.deleteHistory(historyList[position]),
        onComplete = {
            _inDeleteHistoryFinished.call()
        })
    }
}