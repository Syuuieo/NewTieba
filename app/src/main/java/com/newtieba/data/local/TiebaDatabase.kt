package com.newtieba.data.local

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Database
import androidx.room.RoomDatabase

// ── Entity ────────────────────────────────────────────────

@Entity(tableName = "threads")
data class ThreadEntity(
    @PrimaryKey val tid: Long,
    val forumName: String,
    val title: String,
    val authorName: String,
    val authorUid: Long,
    val replyNum: Int,
    val agreeNum: Int,
    val createTime: Long,
    val lastTime: Long,
    val abstract: String,
    val isGood: Boolean,
    val isTop: Boolean,
    val cachedAt: Long = System.currentTimeMillis(),
)

@Entity(tableName = "forums")
data class ForumEntity(
    @PrimaryKey val fid: Long,
    val name: String,
    val avatar: String,
    val memberNum: Int,
    val threadNum: Int,
    val isFollowed: Boolean,
    val cachedAt: Long = System.currentTimeMillis(),
)

// ── DAO ────────────────────────────────────────────────────

@Dao
interface ThreadDao {
    @Query("SELECT * FROM threads WHERE forumName = :forumName ORDER BY lastTime DESC LIMIT :limit")
    suspend fun getThreadsByForum(forumName: String, limit: Int = 30): List<ThreadEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(threads: List<ThreadEntity>)

    @Query("DELETE FROM threads WHERE forumName = :forumName")
    suspend fun deleteByForum(forumName: String)

    @Query("SELECT * FROM threads WHERE tid = :tid")
    suspend fun getById(tid: Long): ThreadEntity?
}

@Dao
interface ForumDao {
    @Query("SELECT * FROM forums ORDER BY name ASC")
    suspend fun getAll(): List<ForumEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(forums: List<ForumEntity>)

    @Query("UPDATE forums SET isFollowed = :isFollowed WHERE fid = :fid")
    suspend fun updateFollowStatus(fid: Long, isFollowed: Boolean)

    @Query("DELETE FROM forums")
    suspend fun deleteAll()
}

// ── Database ──────────────────────────────────────────────

@Database(
    entities = [ThreadEntity::class, ForumEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class TiebaDatabase : RoomDatabase() {
    abstract fun threadDao(): ThreadDao
    abstract fun forumDao(): ForumDao

    companion object {
        const val NAME = "newtieba.db"
    }
}
