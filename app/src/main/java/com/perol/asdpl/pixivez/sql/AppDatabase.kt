package com.perol.asdpl.pixivez.sql

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.perol.asdpl.pixivez.sql.dao.IllustDao
import com.perol.asdpl.pixivez.sql.entity.Illust

@Database(entities = arrayOf(SearchHistoryEntity::class, IllustBeanEntity::class,UserEntity::class,Illust::class), version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun searchhistoryDao(): SearchHistoryDao
    abstract fun illusthistoryDao(): IllustHistoryDao
    abstract fun userDao(): UserDao
    abstract fun IllustDao():IllustDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
                }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                        AppDatabase::class.java, "app.db").fallbackToDestructiveMigration()
                        .build()
    }
}