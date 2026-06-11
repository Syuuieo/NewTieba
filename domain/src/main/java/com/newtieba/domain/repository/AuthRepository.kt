package com.newtieba.domain.repository

import com.newtieba.common.model.Resource
import com.newtieba.domain.model.User
import kotlinx.coroutines.flow.Flow

/**
 * 认证仓库接口
 */
interface AuthRepository {
    /**
     * 登录
     * @param bduss BDUSS
     * @param sToken STOKEN
     */
    fun login(bduss: String, sToken: String): Flow<Resource<User>>

    /**
     * 退出登录
     */
    fun logout(): Flow<Resource<Boolean>>

    /**
     * 获取当前用户
     */
    fun getCurrentUser(): Flow<Resource<User>>

    /**
     * 检查是否已登录
     */
    fun isLoggedIn(): Boolean

    /**
     * 获取BDUSS
     */
    fun getBduss(): String?

    /**
     * 获取STOKEN
     */
    fun getSToken(): String?

    /**
     * 保存登录信息
     */
    fun saveLoginInfo(bduss: String, sToken: String, user: User)

    /**
     * 清除登录信息
     */
    fun clearLoginInfo()
}
