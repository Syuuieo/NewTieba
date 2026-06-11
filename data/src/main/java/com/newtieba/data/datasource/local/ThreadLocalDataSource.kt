package com.newtieba.data.datasource.local

import com.newtieba.database.dao.ThreadDao
import com.newtieba.database.entity.ThreadEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 帖子本地数据源
 */
class ThreadLocalDataSource @Inject constructor(
    private val threadDao: ThreadDao
) {
    /**
     * 插入帖子
     */
    suspend fun insert(thread: ThreadEntity) {
        threadDao.insert(thread)
    }

    /**
     * 批量插入帖子
     */
    suspend fun insertAll(threads: List<ThreadEntity>) {
        threadDao.insertAll(threads)
    }

    /**
     * 更新帖子
     */
    suspend fun update(thread: ThreadEntity) {
        threadDao.update(thread)
    }

    /**
     * 删除帖子
     */
    suspend fun delete(thread: ThreadEntity) {
        threadDao.delete(thread)
    }

    /**
     * 根据ID获取帖子
     */
    suspend fun getById(id: Long): ThreadEntity? {
        return threadDao.getById(id)
    }

    /**
     * 根据ID获取帖子（Flow）
     */
    fun getByIdFlow(id: Long): Flow<ThreadEntity?> {
        return threadDao.getByIdFlow(id)
    }

    /**
     * 获取收藏的帖子列表
     */
    fun getCollectedThreads(): Flow<List<ThreadEntity>> {
        return threadDao.getCollectedThreads()
    }

    /**
     * 获取吧帖子列表
     */
    fun getByForumId(forumId: Long): Flow<List<ThreadEntity>> {
        return threadDao.getByForumId(forumId)
    }

    /**
     * 获取用户发布的帖子
     */
    fun getByAuthorId(authorId: Long): Flow<List<ThreadEntity>> {
        return threadDao.getByAuthorId(authorId)
    }

    /**
     * 搜索帖子
     */
    fun search(keyword: String): Flow<List<ThreadEntity>> {
        return threadDao.search(keyword)
    }

    /**
     * 更新收藏状态
     */
    suspend fun updateCollectStatus(id: Long, isCollected: Boolean) {
        threadDao.updateCollectStatus(id, isCollected)
    }

    /**
     * 更新点赞状态
     */
    suspend fun updateLikeStatus(id: Long, isLiked: Boolean, delta: Int) {
        threadDao.updateLikeStatus(id, isLiked, delta)
    }

    /**
     * 更新回复数
     */
    suspend fun updateReplyCount(id: Long, replyCount: Int) {
        threadDao.updateReplyCount(id, replyCount)
    }

    /**
     * 清除过期缓存
     */
    suspend fun clearExpiredCache(expireTime: Long) {
        threadDao.clearExpiredCache(expireTime)
    }
}
