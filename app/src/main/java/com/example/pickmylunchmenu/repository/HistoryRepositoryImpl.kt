package com.example.pickmylunchmenu.repository

import com.example.pickmylunchmenu.dao.HistoryDao
import com.example.pickmylunchmenu.dto.HistoryDto
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(private val historyDao: HistoryDao) : HistoryRepository {
    override fun insertHistory(history: HistoryDto) : Completable {
       return historyDao.insert(history.toEntity())
    }

    override fun getAllHistory(): Single<List<HistoryDto>> =
        historyDao.getAllHistory().map {
            it.map { history ->
                HistoryDto(history.id, history.name, history.rating, history.myRating, history.date, history.comment)
            }
        }

    override fun deleteHistory(history: HistoryDto): Completable {
        return historyDao.delete(history.toEntity())
    }
}