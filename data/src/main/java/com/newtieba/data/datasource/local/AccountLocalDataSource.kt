package com.newtieba.data.datasource.local

import com.newtieba.database.dao.AccountDao
import com.newtieba.database.entity.AccountEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 账户本地数据源
 */
class AccountLocalDataSource @Inject constructor(
    private val accountDao: AccountDao
) {
    /**
     * 插入账户
     */
    suspend fun insert(account: AccountEntity) {
        accountDao.insert(account)
    }

    /**
     * 更新账户
     */
    suspend fun update(account: AccountEntity) {
        accountDao.update(account)
    }

    /**
     * 删除账户
     */
    suspend fun delete(account: AccountEntity) {
        accountDao.delete(account)
    }

    /**
     * 获取当前登录账户
     */
    suspend fun getCurrentAccount(): AccountEntity? {
        return accountDao.getCurrentAccount()
    }

    /**
     * 获取当前登录账户（Flow）
     */
    fun getCurrentAccountFlow(): Flow<AccountEntity?> {
        return accountDao.getCurrentAccountFlow()
    }

    /**
     * 根据UID获取账户
     */
    suspend fun getByUid(uid: Long): AccountEntity? {
        return accountDao.getByUid(uid)
    }

    /**
     * 获取所有账户
     */
    fun getAllAccounts(): Flow<List<AccountEntity>> {
        return accountDao.getAllAccounts()
    }

    /**
     * 切换账户
     */
    suspend fun switchAccount(uid: Long) {
        accountDao.switchAccount(uid)
    }

    /**
     * 更新登录信息
     */
    suspend fun updateLoginInfo(uid: Long, bduss: String, sToken: String, tbs: String) {
        accountDao.updateLoginInfo(uid, bduss, sToken, tbs)
    }

    /**
     * 检查是否存在账户
     */
    suspend fun hasAccount(): Boolean {
        return accountDao.getCount() > 0
    }
}
