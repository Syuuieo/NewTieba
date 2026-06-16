package com.newtieba.data.api.interceptor

import okhttp3.internal.and
import java.io.ByteArrayInputStream

/**
 * Proto2 Wire Format 解码器
 *
 * 无需编译 .proto 文件，直接从二进制 bytes 中按字段号提取数据。
 * 字段类型参考: https://protobuf.dev/programming-guides/encoding/
 *
 * 用法:
 *   val reader = ProtoReader(bytes)
 *   val kw = reader.getString(1)  // field 1 = string
 *   val pn = reader.getUInt32(2) // field 2 = uint32
 */
class ProtoReader(private val data: ByteArray) {

    private val stream = ByteArrayInputStream(data)
    private var currentTag: Long = 0
    private var currentField: Int = 0
    private var currentWireType: Int = 0

    /**
     * 读取字段值列表（用于 repeated 字段）
     */
    class FieldValues(
        private val values: List<Any?>,
    ) {
        val strings: List<String> get() = values.filterIsInstance<String>()
        val longs: List<Long> get() = values.filterIsInstance<Long>()
        val ints: List<Int> get() = values.mapNotNull {
            when (it) {
                is Int -> it
                is Long -> it.toInt()
                else -> null
            }
        }
        val bytesList: List<ByteArray> get() = values.filterIsInstance<ByteArray>()
        fun isEmpty() = values.isEmpty()
    }

    // ── 顶层读取方法（读取所有字段，过滤需要的） ─────────────────

    /**
     * 读取指定字段的 string 值
     */
    fun getString(fieldNumber: Int): String {
        reset()
        while (hasNext()) {
            if (currentField == fieldNumber && currentWireType == 2) {
                return readStringBytes().decodeToString()
            }
        }
        return ""
    }

    /**
     * 读取指定字段的 uint32 值
     */
    fun getUInt32(fieldNumber: Int): Int {
        reset()
        while (hasNext()) {
            if (currentField == fieldNumber && currentWireType == 0) {
                return readVarint().toInt()
            }
        }
        return 0
    }

    /**
     * 读取指定字段的 uint64 值
     */
    fun getUInt64(fieldNumber: Int): Long {
        reset()
        while (hasNext()) {
            if (currentField == fieldNumber && currentWireType == 0) {
                return readVarint()
            }
        }
        return 0L
    }

    /**
     * 读取指定字段的嵌套 message bytes
     */
    fun getMessage(fieldNumber: Int): ByteArray? {
        reset()
        while (hasNext()) {
            if (currentField == fieldNumber && currentWireType == 2) {
                return readBytes()
            }
        }
        return null
    }

    /**
     * 读取 repeated string 字段的所有值
     */
    fun getRepeatedString(fieldNumber: Int): List<String> {
        reset()
        val result = mutableListOf<String>()
        while (hasNext()) {
            if (currentField == fieldNumber && currentWireType == 2) {
                result.add(readStringBytes().decodeToString())
            }
        }
        return result
    }

    /**
     * 读取 repeated message 字段的所有嵌套 message bytes
     */
    fun getRepeatedMessage(fieldNumber: Int): List<ByteArray> {
        reset()
        val result = mutableListOf<ByteArray>()
        while (hasNext()) {
            if (currentField == fieldNumber && currentWireType == 2) {
                result.add(readBytes())
            }
        }
        return result
    }

    /**
     * 读取 repeated uint32 字段
     */
    fun getRepeatedUInt32(fieldNumber: Int): List<Int> {
        reset()
        val result = mutableListOf<Int>()
        while (hasNext()) {
            if (currentField == fieldNumber) {
                when (currentWireType) {
                    0 -> result.add(readVarint().toInt())
                    2 -> { // packed repeated
                        val packed = readBytes()
                        val packedReader = ProtoReader(packed)
                        while (packedReader.hasNextDirect()) {
                            result.add(packedReader.readVarint().toInt())
                        }
                    }
                }
            }
        }
        return result
    }

    // ── 低层级流读取 ──────────────────────────────────────────

    private fun reset() {
        stream.reset()
        currentTag = 0
        currentField = 0
        currentWireType = 0
    }

    private fun hasNext(): Boolean {
        if (stream.available() <= 0) return false
        currentTag = readVarint()
        currentField = (currentTag shr 3).toInt()
        currentWireType = (currentTag and 0x07).toInt()
        return true
    }

    /**
     * 直接读取下一条（不重置），供 packed 场景使用
     */
    private fun hasNextDirect(): Boolean {
        if (stream.available() <= 0) return false
        currentTag = readVarint()
        currentField = (currentTag shr 3).toInt()
        currentWireType = (currentTag and 0x07).toInt()
        return true
    }

    private fun readVarint(): Long {
        var result = 0L
        var shift = 0
        while (true) {
            val byte = stream.read()
            if (byte == -1) throw IllegalStateException("Unexpected end of varint")
            result = result or ((byte and 0x7FL).toLong() shl shift)
            if (byte and 0x80 == 0) return result
            shift += 7
        }
    }

    private fun readBytes(): ByteArray {
        val size = readVarint().toInt()
        val bytes = ByteArray(size)
        var offset = 0
        while (offset < size) {
            val read = stream.read(bytes, offset, size - offset)
            if (read == -1) throw IllegalStateException("Unexpected end of bytes")
            offset += read
        }
        return bytes
    }

    private fun readStringBytes(): ByteArray = readBytes()

    /**
     * 跳过当前字段的值
     */
    private fun skipField() {
        when (currentWireType) {
            0 -> readVarint()       // varint
            1 -> stream.skip(8)     // 64-bit
            2 -> readBytes()         // length-delimited
            5 -> stream.skip(4)     // 32-bit
            else -> throw IllegalStateException("Unknown wire type: $currentWireType")
        }
    }

    companion object {
        /**
         * 从大 message 中提取其中一个嵌套 message 内特定字段的值。
         * 三步链式调用：readNested(String|Int|Long|List...)
         */
        inline fun <reified T> readFrom(
            data: ByteArray,
            messageField: Int,
            targetField: Int,
        ): T? {
            val msgBytes = ProtoReader(data).getMessage(messageField) ?: return null
            val reader = ProtoReader(msgBytes)
            return when (T::class) {
                String::class -> reader.getString(targetField) as? T
                Int::class -> reader.getUInt32(targetField) as? T
                Long::class -> reader.getUInt64(targetField) as? T
                else -> null
            }
        }
    }
}
