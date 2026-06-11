package com.newtieba.protocol.crypto

import java.security.MessageDigest

/**
 * 贴吧签名计算器
 * 基于 aiotieba 的签名逻辑迁移
 */
object SignCalculator {

    /**
     * APP端签名盐值
     */
    private const val APP_SALT = "tiebaclient!!!"

    /**
     * PC端签名盐值
     */
    private const val PC_SALT = "36770b1f34c9bbf2e7d1a99d2b82fa9e"

    /**
     * 杂项签名盐值
     */
    private const val MISC_SALT = "0039d79dc3cc2075129745a30237a3c4"

    /**
     * 计算APP端签名
     * @param params 参数列表，每个参数为 key-value 对
     * @return 签名字符串
     */
    fun calculateAppSign(params: List<Pair<String, String>>): String {
        return calculateSign(params, APP_SALT)
    }

    /**
     * 计算PC端签名
     * @param params 参数列表
     * @return 签名字符串
     */
    fun calculatePcSign(params: List<Pair<String, String>>): String {
        return calculateSign(params, PC_SALT)
    }

    /**
     * 计算杂项签名
     * @param params 参数列表
     * @return 签名字符串
     */
    fun calculateMiscSign(params: List<Pair<String, String>>): String {
        return calculateSign(params, MISC_SALT)
    }

    /**
     * 通用签名计算
     * 算法：将参数按key字母排序，拼接为 "key=value" 形式，追加密盐，然后计算MD5
     * @param params 参数列表
     * @param salt 盐值
     * @return 签名字符串
     */
    fun calculateSign(params: List<Pair<String, String>>, salt: String): String {
        val sortedParams = params.sortedBy { it.first }
        val sb = StringBuilder()
        for ((key, value) in sortedParams) {
            sb.append(key)
            sb.append("=")
            sb.append(value)
        }
        sb.append(salt)
        return md5(sb.toString())
    }

    /**
     * 计算MD5哈希
     * @param input 输入字符串
     * @return MD5哈希字符串
     */
    fun md5(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        val digest = md.digest(input.toByteArray())
        return digest.joinToString("") { "%02x".format(it) }
    }

    /**
     * 计算MD5哈希（字节数组输入）
     * @param input 输入字节数组
     * @return MD5哈希字符串
     */
    fun md5(input: ByteArray): String {
        val md = MessageDigest.getInstance("MD5")
        val digest = md.digest(input)
        return digest.joinToString("") { "%02x".format(it) }
    }

    /**
     * 计算SHA1哈希
     * @param input 输入字符串
     * @return SHA1哈希字符串
     */
    fun sha1(input: String): String {
        val md = MessageDigest.getInstance("SHA-1")
        val digest = md.digest(input.toByteArray())
        return digest.joinToString("") { "%02x".format(it) }
    }

    /**
     * 计算SHA1哈希（字节数组输入）
     * @param input 输入字节数组
     * @return SHA1哈希字符串
     */
    fun sha1(input: ByteArray): String {
        val md = MessageDigest.getInstance("SHA-1")
        val digest = md.digest(input)
        return digest.joinToString("") { "%02x".format(it) }
    }

    /**
     * 计算CRC32校验和
     * @param input 输入字节数组
     * @return CRC32值
     */
    fun crc32(input: ByteArray): Long {
        val crc = java.util.zip.CRC32()
        crc.update(input)
        return crc.value
    }

    /**
     * 计算CRC32校验和（字符串输入）
     * @param input 输入字符串
     * @return CRC32值
     */
    fun crc32(input: String): Long {
        return crc32(input.toByteArray())
    }
}
