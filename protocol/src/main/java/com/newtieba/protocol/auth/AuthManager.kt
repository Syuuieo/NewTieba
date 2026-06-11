package com.newtieba.protocol.auth

import com.newtieba.protocol.crypto.CuidGenerator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 贴吧认证管理器
 * 管理 BDUSS、STOKEN、tbs 等认证信息
 */
class AuthManager {

    /**
     * 用户认证信息
     */
    data class AuthInfo(
        val bduss: String = "",
        val sToken: String = "",
        val tbs: String = "",
        val uid: String = "",
        val userName: String = "",
        val portrait: String = "",
        val nameShow: String = "",
        val isLogin: Boolean = false
    )

    /**
     * 设备信息
     */
    data class DeviceInfo(
        val androidId: String = CuidGenerator.generateAndroidId(),
        val uuid: String = CuidGenerator.generateUuid(),
        val cuid: String = "baidutiebaapp${CuidGenerator.generateUuid()}",
        val cuidGalaxy2: String = "",
        val c3Aid: String = "",
        val clientId: String = CuidGenerator.generateClientId()
    ) {
        init {
            // 延迟初始化 cuidGalaxy2 和 c3Aid
        }
    }

    private val _authInfo = MutableStateFlow(AuthInfo())
    val authInfo: StateFlow<AuthInfo> = _authInfo.asStateFlow()

    private val _deviceInfo = MutableStateFlow(DeviceInfo())
    val deviceInfo: StateFlow<DeviceInfo> = _deviceInfo.asStateFlow()

    init {
        // 初始化设备信息
        val androidId = _deviceInfo.value.androidId
        val uuid = _deviceInfo.value.uuid
        _deviceInfo.value = _deviceInfo.value.copy(
            cuidGalaxy2 = CuidGenerator.generateCuidGalaxy2(androidId),
            c3Aid = CuidGenerator.generateC3Aid(androidId, uuid)
        )
    }

    /**
     * 设置认证信息
     */
    fun setAuthInfo(bduss: String, sToken: String) {
        _authInfo.value = _authInfo.value.copy(
            bduss = bduss,
            sToken = sToken,
            isLogin = bduss.isNotBlank()
        )
    }

    /**
     * 更新登录信息
     */
    fun updateLoginInfo(
        uid: String,
        userName: String,
        portrait: String,
        tbs: String,
        nameShow: String = ""
    ) {
        _authInfo.value = _authInfo.value.copy(
            uid = uid,
            userName = userName,
            portrait = portrait,
            tbs = tbs,
            nameShow = nameShow.ifBlank { userName },
            isLogin = true
        )
    }

    /**
     * 清除认证信息
     */
    fun clearAuthInfo() {
        _authInfo.value = AuthInfo()
    }

    /**
     * 获取 BDUSS
     */
    fun getBduss(): String = _authInfo.value.bduss

    /**
     * 获取 STOKEN
     */
    fun getSToken(): String = _authInfo.value.sToken

    /**
     * 获取 tbs
     */
    fun getTbs(): String = _authInfo.value.tbs

    /**
     * 获取 UID
     */
    fun getUid(): String = _authInfo.value.uid

    /**
     * 获取用户名
     */
    fun getUserName(): String = _authInfo.value.userName

    /**
     * 获取头像
     */
    fun getPortrait(): String = _authInfo.value.portrait

    /**
     * 获取显示名称
     */
    fun getNameShow(): String = _authInfo.value.nameShow

    /**
     * 是否已登录
     */
    fun isLoggedIn(): Boolean = _authInfo.value.isLogin

    /**
     * 获取设备ID
     */
    fun getAndroidId(): String = _deviceInfo.value.androidId

    /**
     * 获取 UUID
     */
    fun getUuid(): String = _deviceInfo.value.uuid

    /**
     * 获取 CUID
     */
    fun getCuid(): String = _deviceInfo.value.cuid

    /**
     * 获取 CUID Galaxy2
     */
    fun getCuidGalaxy2(): String = _deviceInfo.value.cuidGalaxy2

    /**
     * 获取 C3 AID
     */
    fun getC3Aid(): String = _deviceInfo.value.c3Aid

    /**
     * 获取客户端ID
     */
    fun getClientId(): String = _deviceInfo.value.clientId

    /**
     * 获取 BDUSS Cookie
     */
    fun getBdussCookie(): String {
        val bduss = getBduss()
        return if (bduss.isNotBlank()) {
            "BDUSS=$bduss; Path=/; Max-Age=315360000; Domain=.baidu.com; Httponly"
        } else {
            ""
        }
    }

    /**
     * 获取 STOKEN Cookie
     */
    fun getSTokenCookie(): String {
        val sToken = getSToken()
        return if (sToken.isNotBlank()) {
            "STOKEN=$sToken; Path=/; Max-Age=315360000; Domain=.tieba.baidu.com; Httponly"
        } else {
            ""
        }
    }

    /**
     * 获取完整的 Cookie 字符串
     */
    fun getFullCookie(): String {
        val cookies = mutableListOf<String>()
        val bduss = getBduss()
        val sToken = getSToken()
        val cuid = getCuid()
        val cuidGalaxy2 = getCuidGalaxy2()

        if (bduss.isNotBlank()) cookies.add("BDUSS=$bduss")
        if (sToken.isNotBlank()) cookies.add("STOKEN=$sToken")
        if (cuid.isNotBlank()) cookies.add("CUID=$cuid")
        if (cuidGalaxy2.isNotBlank()) cookies.add("cuid_galaxy2=$cuidGalaxy2")

        return cookies.joinToString("; ")
    }

    companion object {
        @Volatile
        private var INSTANCE: AuthManager? = null

        fun getInstance(): AuthManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: AuthManager().also { INSTANCE = it }
            }
        }
    }
}
