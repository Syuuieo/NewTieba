package com.newtieba.data.repository

import com.newtieba.common.model.Resource
import com.newtieba.data.datasource.local.AccountLocalDataSource
import com.newtieba.data.datasource.remote.AuthRemoteDataSource
import com.newtieba.data.mapper.toDomain
import com.newtieba.data.mapper.toEntity
import com.newtieba.domain.model.User
import com.newtieba.domain.repository.AuthRepository
import com.newtieba.protocol.auth.AuthManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * 认证仓库实现
 */
class AuthRepositoryImpl @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val accountLocalDataSource: AccountLocalDataSource,
    private val authManager: AuthManager
) : AuthRepository {

    override fun login(bduss: String, sToken: String): Flow<Resource<User>> = flow {
        emit(Resource.Loading)
        try {
            // 设置认证信息
            authManager.setAuthInfo(bduss, sToken)

            // 调用登录API
            val response = authRemoteDataSource.login(bduss, sToken)
            if (response.error != null) {
                emit(Resource.Error(response.error.errorMsg))
                return@flow
            }

            val userData = response.data
            if (userData?.user == null) {
                emit(Resource.Error("登录失败：未获取到用户信息"))
                return@flow
            }

            // 更新认证信息
            authManager.updateLoginInfo(
                uid = userData.user.id.toString(),
                userName = userData.user.name,
                portrait = userData.user.portrait,
                tbs = userData.tbs,
                nameShow = userData.user.nameShow
            )

            // 保存到本地数据库
            val user = userData.user.toDomain()
            val account = user.toEntity(bduss, sToken, userData.tbs)
            accountLocalDataSource.insert(account)

            emit(Resource.Success(user))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "登录失败"))
        }
    }

    override fun logout(): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading)
        try {
            // 清除认证信息
            authManager.clearAuthInfo()

            // 清除本地数据
            val currentAccount = accountLocalDataSource.getCurrentAccount()
            if (currentAccount != null) {
                accountLocalDataSource.delete(currentAccount)
            }

            emit(Resource.Success(true))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "退出失败"))
        }
    }

    override fun getCurrentUser(): Flow<Resource<User>> = flow {
        emit(Resource.Loading)
        try {
            // 先从本地获取
            val localAccount = accountLocalDataSource.getCurrentAccount()
            if (localAccount != null) {
                emit(Resource.Success(localAccount.toDomain()))
                return@flow
            }

            // 从网络获取
            val bduss = authManager.getBduss()
            if (bduss.isBlank()) {
                emit(Resource.Error("未登录"))
                return@flow
            }

            val response = authRemoteDataSource.getUserInfo(bduss)
            if (response.error != null) {
                emit(Resource.Error(response.error.errorMsg))
                return@flow
            }

            val userData = response.data?.user
            if (userData == null) {
                emit(Resource.Error("获取用户信息失败"))
                return@flow
            }

            val user = userData.toDomain()
            emit(Resource.Success(user))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "获取用户信息失败"))
        }
    }

    override fun isLoggedIn(): Boolean {
        return authManager.isLoggedIn()
    }

    override fun getBduss(): String? {
        return authManager.getBduss()
    }

    override fun getSToken(): String? {
        return authManager.getSToken()
    }

    override fun saveLoginInfo(bduss: String, sToken: String, user: User) {
        authManager.setAuthInfo(bduss, sToken)
        authManager.updateLoginInfo(
            uid = user.id.toString(),
            userName = user.name,
            portrait = user.portrait,
            tbs = "",
            nameShow = user.nameShow
        )
    }

    override fun clearLoginInfo() {
        authManager.clearAuthInfo()
    }
}
