package com.newtieba.common.model

/**
 * 资源封装类
 * 用于封装网络请求的结果
 */
sealed class Resource<out T> {
    /**
     * 加载中
     */
    data object Loading : Resource<Nothing>()

    /**
     * 成功
     */
    data class Success<T>(val data: T?) : Resource<T>()

    /**
     * 错误
     */
    data class Error(val message: String?, val code: Int? = null) : Resource<Nothing>()

    /**
     * 是否正在加载
     */
    val isLoading: Boolean
        get() = this is Loading

    /**
     * 是否成功
     */
    val isSuccess: Boolean
        get() = this is Success

    /**
     * 是否错误
     */
    val isError: Boolean
        get() = this is Error

    /**
     * 获取数据
     */
    fun getOrNull(): T? = when (this) {
        is Success -> data
        else -> null
    }

    /**
     * 获取数据或默认值
     */
    fun getOrDefault(default: @UnsafeVariance T): T = when (this) {
        is Success -> data ?: default
        else -> default
    }

    /**
     * 获取数据或抛出异常
     */
    fun getOrThrow(): T = when (this) {
        is Success -> data ?: throw IllegalStateException("Data is null")
        is Error -> throw IllegalStateException(message ?: "Unknown error")
        is Loading -> throw IllegalStateException("Still loading")
    }

    /**
     * 转换数据
     */
    fun <R> map(transform: (T) -> R): Resource<R> = when (this) {
        is Loading -> Loading
        is Success -> Success(data?.let(transform))
        is Error -> Error(message, code)
    }

    companion object {
        /**
         * 创建加载中状态
         */
        fun loading(): Resource<Nothing> = Loading

        /**
         * 创建成功状态
         */
        fun <T> success(data: T?): Resource<T> = Success(data)

        /**
         * 创建错误状态
         */
        fun error(message: String?, code: Int? = null): Resource<Nothing> = Error(message, code)
    }
}

/**
 * 扩展函数：将Flow<Resource<T>>转换为只发出成功数据的Flow
 */
fun <T> kotlinx.coroutines.flow.Flow<Resource<T>>.unwrapResource(): kotlinx.coroutines.flow.Flow<T> =
    kotlinx.coroutines.flow.flow {
        collect { resource ->
            when (resource) {
                is Resource.Success -> resource.data?.let { emit(it) }
                is Resource.Error -> throw IllegalStateException(resource.message ?: "Unknown error")
                is Resource.Loading -> { /* do nothing */ }
            }
        }
    }

/**
 * 扩展函数：将Flow<Resource<T>>转换为只发出成功数据的Flow（带默认值）
 */
fun <T> kotlinx.coroutines.flow.Flow<Resource<T>>.unwrapResourceOrDefault(
    default: T
): kotlinx.coroutines.flow.Flow<T> =
    kotlinx.coroutines.flow.flow {
        collect { resource ->
            when (resource) {
                is Resource.Success -> emit(resource.data ?: default)
                is Resource.Error -> throw IllegalStateException(resource.message ?: "Unknown error")
                is Resource.Loading -> { /* do nothing */ }
            }
        }
    }
