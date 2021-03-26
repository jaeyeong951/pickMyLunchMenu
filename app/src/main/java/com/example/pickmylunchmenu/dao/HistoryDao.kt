package com.example.pickmylunchmenu.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.pickmylunchmenu.entity.History
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface HistoryDao {
    @Insert
    fun insert(history : History) : Completable

    @Delete
    fun delete(history: History) : Completable

    @Query("SELECT * FROM history")
    fun getAllHistory() : Single<List<History>>
}