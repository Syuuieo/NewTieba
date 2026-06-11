package com.newtieba.database.dao

import androidx.room.*
import com.newtieba.database.entity.ThreadEntity
import kotlinx.coroutines.flow.Flow

/**
 * 帖子DAO
 */
@Dao
interface ThreadDao {
    /**
     * 插入帖子
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(thread: ThreadEntity)

    /**
     * 批量插入帖子
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(threads: List<ThreadEntity>)

    /**
     * 更新帖子
     */
    @Update
    suspend fun update(thread: ThreadEntity)

    /**
     * 删除帖子
     */
    @Delete
    suspend fun delete(thread: ThreadEntity)

    /**
     * 根据ID删除帖子
     */
    @Query("DELETE FROM threads WHERE id = :id")
    suspend fun deleteById(id: Long)

    /**
     * 根据ID获取帖子
     */
    @Query("SELECT * FROM threads WHERE id = :id")
    suspend fun getById(id: Long): ThreadEntity?

    /**
     * 根据ID获取帖子（Flow）
     */
    @Query("SELECT * FROM threads WHERE id = :id")
    fun getByIdFlow(id: Long): Flow<ThreadEntity?>

    /**
     * 获取收藏的帖子列表
     */
    @Query("SELECT * FROM threads WHERE isCollected = 1 ORDER BY lastUpdateTime DESC")
    fun getCollectedThreads(): Flow<List<ThreadEntity>>

    /**
     * 获取吧帖子列表
     */
    @Query("SELECT * FROM threads WHERE forumId = :forumId ORDER BY isTop DESC, lastReplyTime DESC")
    fun getByForumId(forumId: Long): Flow<List<ThreadEntity>>

    /**
     * 获取用户发布的帖子
     */
    @Query("SELECT * FROM threads WHERE authorId = :authorId ORDER BY createTime DESC")
    fun getByAuthorId(authorId: Long): Flow<List<ThreadEntity>>

    /**
     * 搜索帖子
     */
    @Query("SELECT * FROM threads WHERE title LIKE '%' || :keyword || '%' OR content LIKE '%' || :keyword || '%' ORDER BY lastReplyTime DESC")
    fun search(keyword: String): Flow<List<ThreadEntity>>

    /**
     * 更新收藏状态
     */
    @Query("UPDATE threads SET isCollected = :isCollected WHERE id = :id")
    suspend fun updateCollectStatus(id: Long, isCollected: Boolean)

    /**
     * 更新点赞状态
     */
    @Query("UPDATE threads SET isLiked = :isLiked, likeCount = likeCount + :delta WHERE id = :id")
    suspend fun updateLikeStatus(id: Long, isLiked: Boolean, delta: Int)

    /**
     * 更新回复数
     */
    @Query("UPDATE threads SET replyCount = :replyCount WHERE id = :id")
    suspend fun updateReplyCount(id: Long, replyCount: Int)

    /**
     * 清除过期缓存
     */
    @Query("DELETE FROM threads WHERE cacheTime < :expireTime")
    suspend fun clearExpiredCache(expireTime: Long)

    /**
     * 获取所有帖子
     */
    @Query("SELECT * FROM threads ORDER BY lastUpdateTime DESC")
    fun getAll(): Flow<List<ThreadEntity>>
}
