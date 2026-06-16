package com.newtieba.data.api

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 贴吧身份凭证管理
 *
 * BDUSS：长度 192 的纯 ASCII 字符串，用于所有需要登录的操作
 * STOKEN：配合 BDUSS 使用，部分写操作（发帖、签到）需要
 * portrait：以 "tb.1." 开头，可用于获取头像 URL
 * uid：用户唯一 ID
 */
@Singleton
class AuthDataStore @Inject constructor(
    @ApplicationContext context: Context,
) {
    private val prefs: SharedPreferences by lazy {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        EncryptedSharedPreferences.create(
            FILE_NAME,
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )
    }

    var bduss: String?
        get() = prefs.getString(KEY_BDUSS, null)
        set(value) = prefs.edit().putString(KEY_BDUSS, value).apply()

    var stoken: String?
        get() = prefs.getString(KEY_STOKEN, null)
        set(value) = prefs.edit().putString(KEY_STOKEN, value).apply()

    var portrait: String?
        get() = prefs.getString(KEY_PORTRAIT, null)
        set(value) = prefs.edit().putString(KEY_PORTRAIT, value).apply()

    var uid: Long
        get() = prefs.getLong(KEY_UID, 0)
        set(value) = prefs.edit().putLong(KEY_UID, value).apply()

    var userName: String?
        get() = prefs.getString(KEY_USER_NAME, null)
        set(value) = prefs.edit().putString(KEY_USER_NAME, value).apply()

    val isLoggedIn: Boolean
        get() = !bduss.isNullOrBlank()

    fun logout() {
        prefs.edit().clear().apply()
    }

    companion object {
        private const val FILE_NAME = "tieba_auth"
        private const val KEY_BDUSS = "bduss"
        private const val KEY_STOKEN = "stoken"
        private const val KEY_PORTRAIT = "portrait"
        private const val KEY_UID = "uid"
        private const val KEY_USER_NAME = "user_name"
    }
}
