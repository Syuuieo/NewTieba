package com.newtieba.network.interceptors

import com.newtieba.protocol.auth.AuthManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * 公共参数拦截器
 */
class CommonParamInterceptor @Inject constructor(
    private val authManager: AuthManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val url = original.url.newBuilder()
            .addQueryParameter("_client_version", "12.64.1.1")
            .addQueryParameter("_client_type", "2")
            .addQueryParameter("cuid", authManager.getCuid())
            .addQueryParameter("cuid_galaxy2", authManager.getCuidGalaxy2())
            .addQueryParameter("c3_aid", authManager.getC3Aid())
            .addQueryParameter("android_id", authManager.getAndroidId())
            .addQueryParameter("model", "SM-G988N")
            .addQueryParameter("net_type", "1")
            .addQueryParameter("_timestamp", System.currentTimeMillis().toString())
            .build()

        val request = original.newBuilder()
            .url(url)
            .build()

        return chain.proceed(request)
    }
}
