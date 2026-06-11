package com.newtieba.data.datasource.local

import com.newtieba.database.dao.ForumDao
import com.newtieba.database.entity.ForumEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 吧本地数据源
 */
class ForumLocalDataSource @Inject constructor(
    private val forumDao: ForumDao
) {
    /**
     * 插入吧
     */
    suspend fun insert(forum: ForumEntity) {
        forumDao.insert(forum)
    }

    /**
     * 批量插入吧
     */
    suspend fun insertAll(forums: List<ForumEntity>) {
        forumDao.insertAll(forums)
    }

    /**
     * 更新吧
     */
    suspend fun update(forum: ForumEntity) {
        forumDao.update(forum)
    }

    /**
     * 删除吧
     */
    suspend fun delete(forum: ForumEntity) {
        forumDao.delete(forum)
    }

    /**
     * 获取关注的吧列表
     */
    fun getLikedForums(): Flow<List<ForumEntity>> {
        return forumDao.getLikedForums()
    }

    /**
     * 获取置顶的吧列表
     */
    fun getTopForums(): Flow<List<ForumEntity>> {
        return forumDao.getTopForums()
    }

    /**
     * 根据ID获取吧
     */
    suspend fun getById(id: Long): ForumEntity? {
        return forumDao.getById(id)
    }

    /**
     * 根据ID获取吧（Flow）
     */
    fun getByIdFlow(id: Long): Flow<ForumEntity?> {
        return forumDao.getByIdFlow(id)
    }

    /**
     * 根据名称获取吧
     */
    suspend fun getByName(name: String): ForumEntity? {
        return forumDao.getByName(name)
    }

    /**
     * 搜索吧
     */
    fun search(keyword: String): Flow<List<ForumEntity>> {
        return forumDao.search(keyword)
    }

    /**
     * 更新签到状态
     */
    suspend fun updateSignStatus(id: Long, isSigned: Boolean) {
        forumDao.updateSignStatus(id, isSigned)
    }

    /**
     * 更新关注状态
     */
    suspend fun updateLikeStatus(id: Long, isLiked: Boolean) {
        forumDao.updateLikeStatus(id, isLiked)
    }

    /**
     * 更新置顶状态
     */
    suspend fun updateTopStatus(id: Long, isTop: Boolean) {
        forumDao.updateTopStatus(id, isTop)
    }
}
