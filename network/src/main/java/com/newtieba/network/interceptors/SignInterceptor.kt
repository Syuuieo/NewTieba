package com.newtieba.network.interceptors

import com.newtieba.protocol.crypto.SignCalculator
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * 签名拦截器
 */
class SignInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val body = original.body

        // 如果是POST请求且有表单参数，添加签名
        if (original.method == "POST" && body is FormBody) {
            val params = mutableListOf<Pair<String, String>>()
            for (i in 0 until body.size) {
                params.add(body.name(i) to body.value(i))
            }

            // 计算签名
            val sign = SignCalculator.calculateAppSign(params)

            // 创建新的表单体
            val newBody = FormBody.Builder()
            for (i in 0 until body.size) {
                newBody.add(body.name(i), body.value(i))
            }
            newBody.add("sign", sign)

            val request = original.newBuilder()
                .post(newBody.build())
                .build()

            return chain.proceed(request)
        }

        return chain.proceed(original)
    }
}
