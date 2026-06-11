package com.newtieba.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.newtieba.database.converter.Converters
import com.newtieba.database.dao.*
import com.newtieba.database.entity.*

/**
 * 应用数据库
 */
@Database(
    entities = [
        AccountEntity::class,
        ForumEntity::class,
        ThreadEntity::class,
        UserEntity::class,
        HistoryEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun forumDao(): ForumDao
    abstract fun threadDao(): ThreadDao
    abstract fun userDao(): UserDao
    abstract fun historyDao(): HistoryDao
}
