package com.newtieba.data.api.model

sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val message: String, val code: Int = -1) : ApiResult<Nothing>()
}

class TiebaException(
    message: String,
    val code: Int = -1,
) : Exception(message)

// 通用 API 响应包装
data class TiebaResponse<T>(
    val errorCode: Int = 0,
    val errorMsg: String? = null,
    val data: T? = null,
) {
    val isSuccess: Boolean get() = errorCode == 0
}
