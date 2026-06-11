package com.newtieba.data.datasource.remote

import com.newtieba.protocol.api.TiebaApiService
import com.newtieba.protocol.api.endpoints.AuthApi
import com.newtieba.protocol.api.endpoints.SyncRes
import com.newtieba.protocol.api.models.response.LoginRes
import com.newtieba.protocol.api.models.response.UserRes
import javax.inject.Inject

/**
 * 认证远程数据源
 */
class AuthRemoteDataSource @Inject constructor(
    private val authApi: AuthApi,
    private val tiebaApiService: TiebaApiService
) {
    /**
     * 登录
     */
    suspend fun login(bduss: String, sToken: String): LoginRes {
        return authApi.getLoginInfo(bduss = bduss, sToken = sToken)
    }

    /**
     * 获取用户信息
     */
    suspend fun getUserInfo(bduss: String): UserRes {
        return authApi.getCurrentUserInfo(bduss = bduss)
    }

    /**
     * 同步
     */
    suspend fun sync(bduss: String, cuid: String, cuidGalaxy2: String): SyncRes {
        return authApi.sync(bduss = bduss, cuid = cuid, cuidGalaxy2 = cuidGalaxy2)
    }
}
