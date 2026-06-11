package com.newtieba.data.datasource.local

import com.newtieba.database.dao.HistoryDao
import com.newtieba.database.entity.HistoryEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 历史记录本地数据源
 */
class HistoryLocalDataSource @Inject constructor(
    private val historyDao: HistoryDao
) {
    /**
     * 插入历史记录
     */
    suspend fun insert(history: HistoryEntity) {
        historyDao.insert(history)
    }

    /**
     * 批量插入历史记录
     */
    suspend fun insertAll(histories: List<HistoryEntity>) {
        historyDao.insertAll(histories)
    }

    /**
     * 更新历史记录
     */
    suspend fun update(history: HistoryEntity) {
        historyDao.update(history)
    }

    /**
     * 删除历史记录
     */
    suspend fun delete(history: HistoryEntity) {
        historyDao.delete(history)
    }

    /**
     * 根据帖子ID删除历史记录
     */
    suspend fun deleteByThreadId(threadId: Long) {
        historyDao.deleteByThreadId(threadId)
    }

    /**
     * 获取历史记录列表
     */
    fun getAll(): Flow<List<HistoryEntity>> {
        return historyDao.getAll()
    }

    /**
     * 获取最近的历史记录
     */
    fun getRecent(limit: Int): Flow<List<HistoryEntity>> {
        return historyDao.getRecent(limit)
    }

    /**
     * 根据帖子ID获取历史记录
     */
    suspend fun getByThreadId(threadId: Long): HistoryEntity? {
        return historyDao.getByThreadId(threadId)
    }

    /**
     * 搜索历史记录
     */
    fun search(keyword: String): Flow<List<HistoryEntity>> {
        return historyDao.search(keyword)
    }

    /**
     * 清除所有历史记录
     */
    suspend fun clearAll() {
        historyDao.clearAll()
    }

    /**
     * 清除指定时间之前的历史记录
     */
    suspend fun clearBefore(time: Long) {
        historyDao.clearBefore(time)
    }

    /**
     * 获取历史记录数量
     */
    suspend fun getCount(): Int {
        return historyDao.getCount()
    }

    /**
     * 获取历史记录数量（Flow）
     */
    fun getCountFlow(): Flow<Int> {
        return historyDao.getCountFlow()
    }
}
