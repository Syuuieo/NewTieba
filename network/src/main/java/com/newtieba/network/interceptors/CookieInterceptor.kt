package com.newtieba.network.interceptors

import com.newtieba.protocol.auth.AuthManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * Cookie拦截器
 */
class CookieInterceptor @Inject constructor(
    private val authManager: AuthManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        // 添加Cookie
        val cookie = authManager.getFullCookie()
        val request = if (cookie.isNotBlank()) {
            original.newBuilder()
                .addHeader("Cookie", cookie)
                .build()
        } else {
            original
        }

        return chain.proceed(request)
    }
}
