package com.newtieba.data.api.interceptor

import com.newtieba.data.api.AuthDataStore
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * 模拟官方 Android 客户端的请求头
 * 参考 aiotieba 的客户端伪装策略
 */
class TiebaAppInterceptor @Inject constructor(
    private val authDataStore: AuthDataStore,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val builder = original.newBuilder()

        // 通用客户端伪装
        builder.header("User-Agent", "bdtb for Android $CLIENT_VERSION")
        builder.header("x_bd_data_type", "protobuf")

        // Cookie 认证
        val cookie = buildCookie()
        if (cookie.isNotEmpty()) {
            builder.header("Cookie", cookie)
        }

        return chain.proceed(builder.build())
    }

    private fun buildCookie(): String {
        return buildString {
            authDataStore.bduss?.let {
                append("BDUSS=$it")
            }
            authDataStore.stoken?.let {
                if (isNotEmpty()) append("; ")
                append("STOKEN=$it")
            }
        }
    }

    companion object {
        const val CLIENT_VERSION = "12.59.1.0"
        const val CLIENT_TYPE = "2" // Android

        // App 接口密钥（用于请求签名）
        const val APP_SECRET = "tiebaclient!!!"
        const val APP_ID = "tiebaclient"
    }
}
