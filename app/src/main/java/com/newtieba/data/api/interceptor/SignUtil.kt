package com.newtieba.data.api.interceptor

/**
 * 请求签名工具（参考 aiotieba 的 sign 方法）
 *
 * 贴吧 App 接口使用 MD5 对参数排序后签名
 * 用于部分需要签名的写操作接口
 */
object SignUtil {

    /**
     * 生成请求签名
     * @param params 请求参数字典
     * @param secret 密钥（贴吧客户端固定为 tiebaclient!!!）
     * @return 32 位小写 MD5
     */
    fun sign(params: Map<String, String>, secret: String = TiebaAppInterceptor.APP_SECRET): String {
        val sorted = params.entries
            .filter { it.key != "sign" }
            .sortedBy { it.key }
            .joinToString("") { "${it.key}=${it.value}" }
        return "${sorted}$secret".md5()
    }

    private fun String.md5(): String {
        val md = java.security.MessageDigest.getInstance("MD5")
        val digest = md.digest(this.toByteArray())
        return digest.joinToString("") { "%02x".format(it) }
    }
}
