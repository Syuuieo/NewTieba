package com.newtieba.data.api

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

/**
 * Protobuf 的 Retrofit Converter Factory
 *
 * 贴吧 App 接口返回 protobuf 二进制数据，
 * 使用此 Converter 将 ResponseBody 的 bytes 直接传给调用方解析。
 *
 * 用法：在 Retrofit 接口中，返回值声明为 ResponseBody，
 * 然后在 Repository 层用对应 proto message 的 parseFrom(bytes) 解析。
 */
class ProtobufConverterFactory : Converter.Factory() {

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<out Annotation>,
        methodAnnotations: Array<out Annotation>,
        retrofit: Retrofit,
    ): Converter<*, RequestBody> {
        return Converter<Any, RequestBody> { value ->
            when (value) {
                is ByteArray -> value.toRequestBody(PROTOBUF_MEDIA_TYPE)
                is RequestBody -> value
                else -> value.toString().toByteArray().toRequestBody(PROTOBUF_MEDIA_TYPE)
            }
        }
    }

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit,
    ): Converter<ResponseBody, *> {
        return Converter<ResponseBody, Any> { body ->
            body.bytes()
        }
    }

    companion object {
        val PROTOBUF_MEDIA_TYPE = "application/x-protobuf".toMediaType()
    }
}
