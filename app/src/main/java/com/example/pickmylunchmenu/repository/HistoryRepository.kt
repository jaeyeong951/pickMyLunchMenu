package com.example.pickmylunchmenu.repository

import com.example.pickmylunchmenu.dto.HistoryDto
import io.reactivex.Completable
import io.reactivex.Single

interface HistoryRepository {
    fun insertHistory(history : HistoryDto) : Completable
    fun getAllHistory() : Single<List<HistoryDto>>
    fun deleteHistory(history: HistoryDto) : Completable
}