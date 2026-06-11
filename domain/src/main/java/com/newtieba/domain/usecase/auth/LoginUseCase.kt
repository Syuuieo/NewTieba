package com.newtieba.domain.usecase.auth

import com.newtieba.common.model.Resource
import com.newtieba.domain.model.User
import com.newtieba.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 登录UseCase
 */
class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    /**
     * 登录
     * @param bduss BDUSS
     * @param sToken STOKEN
     */
    operator fun invoke(bduss: String, sToken: String): Flow<Resource<User>> {
        return authRepository.login(bduss, sToken)
    }

    /**
     * 检查是否已登录
     */
    fun isLoggedIn(): Boolean {
        return authRepository.isLoggedIn()
    }

    /**
     * 获取当前用户
     */
    fun getCurrentUser(): Flow<Resource<User>> {
        return authRepository.getCurrentUser()
    }
}

/**
 * 退出登录UseCase
 */
class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    /**
     * 退出登录
     */
    operator fun invoke(): Flow<Resource<Boolean>> {
        return authRepository.logout()
    }
}
