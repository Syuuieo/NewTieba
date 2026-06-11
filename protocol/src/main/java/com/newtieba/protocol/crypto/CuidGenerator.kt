package com.newtieba.protocol.crypto

import java.security.MessageDigest
import java.util.UUID
import java.util.zip.CRC32

/**
 * 贴吧设备ID生成器
 * 基于 aiotieba 的 cuid_galaxy2 / c3_aid 生成逻辑迁移
 */
object CuidGenerator {

    /**
     * 生成 Android ID（16字符hex）
     */
    fun generateAndroidId(): String {
        val bytes = ByteArray(8)
        java.security.SecureRandom().nextBytes(bytes)
        return bytes.joinToString("") { "%02x".format(it) }
    }

    /**
     * 生成 UUID
     */
    fun generateUuid(): String {
        return UUID.randomUUID().toString()
    }

    /**
     * 生成旧版 CUID（9.x版本）
     * 格式：baidutiebaapp + UUID
     */
    fun generateOldCuid(uuid: String): String {
        return "baidutiebaapp$uuid"
    }

    /**
     * 生成 CUID Galaxy2（12.x版本）
     * 算法：MD5("com.baidu" + android_id) -> heliosHash -> base32
     */
    fun generateCuidGalaxy2(androidId: String): String {
        val md5Input = "com.baidu$androidId"
        val md5Hash = SignCalculator.md5(md5Input)
        val heliosResult = heliosHash(md5Hash.toByteArray())
        return base32Encode(heliosResult)
    }

    /**
     * 生成 C3 AID（12.x版本）
     * 算法：SHA1("com.helios" + android_id + uuid) -> base32 -> heliosHash -> base32
     */
    fun generateC3Aid(androidId: String, uuid: String): String {
        val sha1Input = "com.helios$androidId$uuid"
        val sha1Hash = SignCalculator.sha1(sha1Input)
        val base32Part = base32Encode(sha1Hash.toByteArray())
        val heliosResult = heliosHash(base32Part.toByteArray())
        return base32Encode(heliosResult)
    }

    /**
     * HeliosHash 算法
     * 输入 -> CRC32 -> xxHash32(两次) -> CRC32 -> 4次迭代 -> 5字节输出
     */
    fun heliosHash(input: ByteArray): ByteArray {
        // 第一步：CRC32
        val crc = CRC32()
        crc.update(input)
        var hash1 = crc.value.toInt()

        // 第二步：xxHash32（两次）
        val xxhash1 = xxHash32(input, 0)
        val xxhash2 = xxHash32(input, xxhash1)

        // 第三步：再次CRC32
        crc.reset()
        crc.update(intToBytes(xxhash2))
        var hash2 = crc.value.toInt()

        // 第四步：4次迭代
        var result = hash1 xor hash2
        for (i in 0 until 4) {
            result = result xor (result shr 16)
            result = result * 0x85ebca6b.toInt()
            result = result xor (result shr 13)
            result = result * 0xc2b2ae35.toInt()
            result = result xor (result shr 16)
        }

        // 第五步：5字节输出
        return intToBytes(result).copyOf(5)
    }

    /**
     * xxHash32 算法实现
     */
    private fun xxHash32(input: ByteArray, seed: Int): Int {
        val len = input.size
        var h32: Int
        var index = 0

        if (len >= 16) {
            val limit = len - 16
            var v1 = seed + PRIME32_1 + PRIME32_2
            var v2 = seed + PRIME32_2
            var v3 = seed + 0
            var v4 = seed - PRIME32_1

            do {
                v1 = round(v1, getLittleEndianInt(input, index))
                index += 4
                v2 = round(v2, getLittleEndianInt(input, index))
                index += 4
                v3 = round(v3, getLittleEndianInt(input, index))
                index += 4
                v4 = round(v4, getLittleEndianInt(input, index))
                index += 4
            } while (index <= limit)

            h32 = Integer.rotateLeft(v1, 1) +
                    Integer.rotateLeft(v2, 7) +
                    Integer.rotateLeft(v3, 12) +
                    Integer.rotateLeft(v4, 18)
        } else {
            h32 = seed + PRIME32_5
        }

        h32 += len

        while (index <= len - 4) {
            h32 += getLittleEndianInt(input, index) * PRIME32_3
            h32 = Integer.rotateLeft(h32, 17) * PRIME32_4
            index += 4
        }

        while (index < len) {
            h32 += (input[index].toInt() and 0xFF) * PRIME32_5
            h32 = Integer.rotateLeft(h32, 11) * PRIME32_1
            index++
        }

        h32 = h32 xor (h32 ushr 15)
        h32 *= PRIME32_2
        h32 = h32 xor (h32 ushr 13)
        h32 *= PRIME32_3
        h32 = h32 xor (h32 ushr 16)

        return h32
    }

    private const val PRIME32_1 = -1640531535 // 0x9E3779B1
    private const val PRIME32_2 = -2048144777 // 0x85EBCA77
    private const val PRIME32_3 = -2048144777 // 0x85EBCA77
    private const val PRIME32_4 = -2048144777 // 0x85EBCA77
    private const val PRIME32_5 = -1640531535 // 0x9E3779B1

    private fun round(acc: Int, input: Int): Int {
        var result = acc + input * PRIME32_2
        result = Integer.rotateLeft(result, 13)
        result *= PRIME32_1
        return result
    }

    private fun getLittleEndianInt(data: ByteArray, index: Int): Int {
        return (data[index].toInt() and 0xFF) or
                ((data[index + 1].toInt() and 0xFF) shl 8) or
                ((data[index + 2].toInt() and 0xFF) shl 16) or
                ((data[index + 3].toInt() and 0xFF) shl 24)
    }

    private fun intToBytes(value: Int): ByteArray {
        return byteArrayOf(
            (value and 0xFF).toByte(),
            ((value shr 8) and 0xFF).toByte(),
            ((value shr 16) and 0xFF).toByte(),
            ((value shr 24) and 0xFF).toByte()
        )
    }

    /**
     * Base32 编码
     */
    fun base32Encode(input: ByteArray): String {
        val alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567"
        val result = StringBuilder()
        var bits = 0
        var value = 0

        for (byte in input) {
            value = (value shl 8) or (byte.toInt() and 0xFF)
            bits += 8

            while (bits >= 5) {
                result.append(alphabet[(value shr (bits - 5)) and 0x1F])
                bits -= 5
            }
        }

        if (bits > 0) {
            result.append(alphabet[(value shl (5 - bits)) and 0x1F])
        }

        return result.toString()
    }

    /**
     * Base32 解码
     */
    fun base32Decode(input: String): ByteArray {
        val alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567"
        val result = mutableListOf<Byte>()
        var bits = 0
        var value = 0

        for (char in input) {
            val index = alphabet.indexOf(char)
            if (index == -1) continue

            value = (value shl 5) or index
            bits += 5

            while (bits >= 8) {
                result.add(((value shr (bits - 8)) and 0xFF).toByte())
                bits -= 8
            }
        }

        return result.toByteArray()
    }

    /**
     * 生成随机字节数组
     */
    fun generateRandomBytes(length: Int): ByteArray {
        val bytes = ByteArray(length)
        java.security.SecureRandom().nextBytes(bytes)
        return bytes
    }

    /**
     * 生成客户端ID
     * 格式：wappc_时间戳_随机数
     */
    fun generateClientId(): String {
        val timestamp = System.currentTimeMillis()
        val random = (Math.random() * 1000).toInt()
        return "wappc_${timestamp}_$random"
    }
}
