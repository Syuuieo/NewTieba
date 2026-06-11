package com.newtieba.database.dao

import androidx.room.*
import com.newtieba.database.entity.AccountEntity
import kotlinx.coroutines.flow.Flow

/**
 * 账户DAO
 */
@Dao
interface AccountDao {
    /**
     * 插入账户
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(account: AccountEntity)

    /**
     * 更新账户
     */
    @Update
    suspend fun update(account: AccountEntity)

    /**
     * 删除账户
     */
    @Delete
    suspend fun delete(account: AccountEntity)

    /**
     * 根据UID删除账户
     */
    @Query("DELETE FROM accounts WHERE uid = :uid")
    suspend fun deleteByUid(uid: Long)

    /**
     * 获取当前登录账户
     */
    @Query("SELECT * FROM accounts WHERE isLoggedIn = 1 LIMIT 1")
    suspend fun getCurrentAccount(): AccountEntity?

    /**
     * 获取当前登录账户（Flow）
     */
    @Query("SELECT * FROM accounts WHERE isLoggedIn = 1 LIMIT 1")
    fun getCurrentAccountFlow(): Flow<AccountEntity?>

    /**
     * 根据UID获取账户
     */
    @Query("SELECT * FROM accounts WHERE uid = :uid")
    suspend fun getByUid(uid: Long): AccountEntity?

    /**
     * 获取所有账户
     */
    @Query("SELECT * FROM accounts ORDER BY lastLoginTime DESC")
    fun getAllAccounts(): Flow<List<AccountEntity>>

    /**
     * 设置当前登录账户
     */
    @Query("UPDATE accounts SET isLoggedIn = 0")
    suspend fun logoutAll()

    @Query("UPDATE accounts SET isLoggedIn = 1 WHERE uid = :uid")
    suspend fun login(uid: Long)

    @Transaction
    suspend fun switchAccount(uid: Long) {
        logoutAll()
        login(uid)
    }

    /**
     * 更新登录信息
     */
    @Query("UPDATE accounts SET bduss = :bduss, sToken = :sToken, tbs = :tbs WHERE uid = :uid")
    suspend fun updateLoginInfo(uid: Long, bduss: String, sToken: String, tbs: String)

    /**
     * 检查是否存在账户
     */
    @Query("SELECT COUNT(*) FROM accounts")
    suspend fun getCount(): Int
}
