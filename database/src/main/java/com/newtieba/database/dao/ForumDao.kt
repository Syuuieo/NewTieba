package com.newtieba.database.dao

import androidx.room.*
import com.newtieba.database.entity.ForumEntity
import kotlinx.coroutines.flow.Flow

/**
 * 吧DAO
 */
@Dao
interface ForumDao {
    /**
     * 插入吧
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(forum: ForumEntity)

    /**
     * 批量插入吧
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(forums: List<ForumEntity>)

    /**
     * 更新吧
     */
    @Update
    suspend fun update(forum: ForumEntity)

    /**
     * 删除吧
     */
    @Delete
    suspend fun delete(forum: ForumEntity)

    /**
     * 根据ID删除吧
     */
    @Query("DELETE FROM forums WHERE id = :id")
    suspend fun deleteById(id: Long)

    /**
     * 获取关注的吧列表
     */
    @Query("SELECT * FROM forums WHERE isLiked = 1 ORDER BY isTop DESC, lastUpdateTime DESC")
    fun getLikedForums(): Flow<List<ForumEntity>>

    /**
     * 获取置顶的吧列表
     */
    @Query("SELECT * FROM forums WHERE isTop = 1 ORDER BY lastUpdateTime DESC")
    fun getTopForums(): Flow<List<ForumEntity>>

    /**
     * 根据ID获取吧
     */
    @Query("SELECT * FROM forums WHERE id = :id")
    suspend fun getById(id: Long): ForumEntity?

    /**
     * 根据ID获取吧（Flow）
     */
    @Query("SELECT * FROM forums WHERE id = :id")
    fun getByIdFlow(id: Long): Flow<ForumEntity?>

    /**
     * 根据名称获取吧
     */
    @Query("SELECT * FROM forums WHERE name = :name")
    suspend fun getByName(name: String): ForumEntity?

    /**
     * 搜索吧
     */
    @Query("SELECT * FROM forums WHERE name LIKE '%' || :keyword || '%' ORDER BY memberNum DESC")
    fun search(keyword: String): Flow<List<ForumEntity>>

    /**
     * 更新签到状态
     */
    @Query("UPDATE forums SET isSigned = :isSigned WHERE id = :id")
    suspend fun updateSignStatus(id: Long, isSigned: Boolean)

    /**
     * 更新关注状态
     */
    @Query("UPDATE forums SET isLiked = :isLiked WHERE id = :id")
    suspend fun updateLikeStatus(id: Long, isLiked: Boolean)

    /**
     * 更新置顶状态
     */
    @Query("UPDATE forums SET isTop = :isTop WHERE id = :id")
    suspend fun updateTopStatus(id: Long, isTop: Boolean)

    /**
     * 获取所有吧
     */
    @Query("SELECT * FROM forums ORDER BY lastUpdateTime DESC")
    fun getAll(): Flow<List<ForumEntity>>
}
