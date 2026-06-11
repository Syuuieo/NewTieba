package com.newtieba.database.dao

import androidx.room.*
import com.newtieba.database.entity.HistoryEntity
import kotlinx.coroutines.flow.Flow

/**
 * 历史记录DAO
 */
@Dao
interface HistoryDao {
    /**
     * 插入历史记录
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(history: HistoryEntity)

    /**
     * 批量插入历史记录
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(histories: List<HistoryEntity>)

    /**
     * 更新历史记录
     */
    @Update
    suspend fun update(history: HistoryEntity)

    /**
     * 删除历史记录
     */
    @Delete
    suspend fun delete(history: HistoryEntity)

    /**
     * 根据ID删除历史记录
     */
    @Query("DELETE FROM history WHERE id = :id")
    suspend fun deleteById(id: Long)

    /**
     * 根据帖子ID删除历史记录
     */
    @Query("DELETE FROM history WHERE threadId = :threadId")
    suspend fun deleteByThreadId(threadId: Long)

    /**
     * 获取历史记录列表
     */
    @Query("SELECT * FROM history ORDER BY lastViewTime DESC")
    fun getAll(): Flow<List<HistoryEntity>>

    /**
     * 获取最近的历史记录
     */
    @Query("SELECT * FROM history ORDER BY lastViewTime DESC LIMIT :limit")
    fun getRecent(limit: Int): Flow<List<HistoryEntity>>

    /**
     * 根据帖子ID获取历史记录
     */
    @Query("SELECT * FROM history WHERE threadId = :threadId")
    suspend fun getByThreadId(threadId: Long): HistoryEntity?

    /**
     * 搜索历史记录
     */
    @Query("SELECT * FROM history WHERE threadTitle LIKE '%' || :keyword || '%' ORDER BY lastViewTime DESC")
    fun search(keyword: String): Flow<List<HistoryEntity>>

    /**
     * 清除所有历史记录
     */
    @Query("DELETE FROM history")
    suspend fun clearAll()

    /**
     * 清除指定时间之前的历史记录
     */
    @Query("DELETE FROM history WHERE lastViewTime < :time")
    suspend fun clearBefore(time: Long)

    /**
     * 获取历史记录数量
     */
    @Query("SELECT COUNT(*) FROM history")
    suspend fun getCount(): Int

    /**
     * 获取历史记录数量（Flow）
     */
    @Query("SELECT COUNT(*) FROM history")
    fun getCountFlow(): Flow<Int>
}
