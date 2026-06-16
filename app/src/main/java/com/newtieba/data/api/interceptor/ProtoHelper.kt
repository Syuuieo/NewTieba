package com.newtieba.data.api.interceptor

import com.newtieba.data.api.ProtobufConverterFactory
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.util.zip.Inflater

/**
 * Protobuf 请求构建与响应解压工具
 */
object ProtoHelper {

    const val CLIENT_VERSION = "12.59.1.0"

    fun buildRequestBody(protoBytes: ByteArray): RequestBody {
        return protoBytes.toRequestBody(ProtobufConverterFactory.PROTOBUF_MEDIA_TYPE)
    }

    fun decompress(data: ByteArray): ByteArray {
        if (data.size < 2 || (data[0].toInt() and 0xFF) != 0x78) {
            return data
        }
        val inflater = Inflater()
        inflater.setInput(data)
        val output = ByteArrayOutputStream()
        val buffer = ByteArray(4096)
        try {
            while (!inflater.finished()) {
                val count = inflater.inflate(buffer)
                output.write(buffer, 0, count)
            }
            return output.toByteArray()
        } finally {
            inflater.end()
            output.close()
        }
    }

    fun buildProtoMessage(block: ProtoBuffer.() -> Unit): ByteArray {
        val buf = ProtoBuffer()
        buf.block()
        return buf.toByteArray()
    }

    inline fun ProtoBuffer.writeMessageField(fieldNumber: Int, crossinline block: ProtoBuffer.() -> Unit) {
        writeTag(fieldNumber, 2)
        val nested = buildProtoMessage(block)
        writeBytes(nested)
    }

    fun ProtoBuffer.writeStringField(fieldNumber: Int, value: String) {
        if (value.isEmpty()) return
        writeTag(fieldNumber, 2)
        writeString(value)
    }

    fun ProtoBuffer.writeUInt32Field(fieldNumber: Int, value: Int) {
        if (value == 0) return
        writeTag(fieldNumber, 0)
        writeVarint(value.toLong() and 0xFFFFFFFFL)
    }

    fun ProtoBuffer.writeUInt64Field(fieldNumber: Int, value: Long) {
        if (value == 0L) return
        writeTag(fieldNumber, 0)
        writeVarint(value)
    }
}

class ProtoBuffer {
    val data = ByteArrayOutputStream()

    fun writeVarint(value: Long) {
        var v = value
        while (v >= 0x80) {
            data.write(((v and 0x7F) or 0x80).toInt())
            v = v shr 7
        }
        data.write((v and 0x7F).toInt())
    }

    fun writeTag(fieldNumber: Int, wireType: Int) {
        writeVarint((fieldNumber shl 3) or wireType.toLong())
    }

    fun writeString(value: String) {
        val bytes = value.toByteArray(Charsets.UTF_8)
        writeVarint(bytes.size.toLong())
        data.write(bytes)
    }

    fun writeBytes(bytes: ByteArray) {
        writeVarint(bytes.size.toLong())
        data.write(bytes)
    }

    fun toByteArray() = data.toByteArray()
}
