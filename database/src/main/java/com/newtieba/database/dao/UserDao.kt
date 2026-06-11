package com.newtieba.database.dao

import androidx.room.*
import com.newtieba.database.entity.UserEntity
import kotlinx.coroutines.flow.Flow

/**
 * 用户DAO
 */
@Dao
interface UserDao {
    /**
     * 插入用户
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity)

    /**
     * 批量插入用户
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<UserEntity>)

    /**
     * 更新用户
     */
    @Update
    suspend fun update(user: UserEntity)

    /**
     * 删除用户
     */
    @Delete
    suspend fun delete(user: UserEntity)

    /**
     * 根据ID删除用户
     */
    @Query("DELETE FROM users WHERE id = :id")
    suspend fun deleteById(id: Long)

    /**
     * 根据ID获取用户
     */
    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getById(id: Long): UserEntity?

    /**
     * 根据ID获取用户（Flow）
     */
    @Query("SELECT * FROM users WHERE id = :id")
    fun getByIdFlow(id: Long): Flow<UserEntity?>

    /**
     * 根据名称获取用户
     */
    @Query("SELECT * FROM users WHERE name = :name")
    suspend fun getByName(name: String): UserEntity?

    /**
     * 搜索用户
     */
    @Query("SELECT * FROM users WHERE name LIKE '%' || :keyword || '%' OR nameShow LIKE '%' || :keyword || '%' ORDER BY fansNum DESC")
    fun search(keyword: String): Flow<List<UserEntity>>

    /**
     * 更新关注状态
     */
    @Query("UPDATE users SET isFollowed = :isFollowed WHERE id = :id")
    suspend fun updateFollowStatus(id: Long, isFollowed: Boolean)

    /**
     * 清除过期缓存
     */
    @Query("DELETE FROM users WHERE cacheTime < :expireTime")
    suspend fun clearExpiredCache(expireTime: Long)
}
