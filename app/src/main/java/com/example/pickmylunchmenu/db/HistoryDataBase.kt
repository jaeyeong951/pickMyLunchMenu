package com.example.pickmylunchmenu.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pickmylunchmenu.dao.HistoryDao
import com.example.pickmylunchmenu.entity.History

@Database(entities = [History::class], version = 1)
abstract class HistoryDataBase : RoomDatabase() {
    abstract fun historyDao() : HistoryDao
}