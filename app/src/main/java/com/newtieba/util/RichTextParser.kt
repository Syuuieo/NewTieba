package com.newtieba.util

import com.newtieba.data.model.ContentPiece
import com.newtieba.data.model.ContentType

object RichTextParser {

    /**
     * 解析富文本内容碎片列表（占位，后续接入真实 protobuf 时扩展）
     * 目前根据文本内容自动检测 @用户、#话题#、http 链接等
     */
    fun parse(text: String): List<ContentPiece> {
        val pieces = mutableListOf<ContentPiece>()
        val regex = Regex("""(@[一-龥a-zA-Z0-9_]+|#([^#]+)#|(https?://[^\s]+))""")
        var lastIndex = 0

        regex.findAll(text).forEach { match ->
            // 前一段纯文本
            if (match.range.first > lastIndex) {
                pieces.add(
                    ContentPiece(
                        type = ContentType.TEXT,
                        text = text.substring(lastIndex, match.range.first),
                    )
                )
            }
            val matched = match.value
            pieces.add(
                when {
                    matched.startsWith("@") -> ContentPiece(
                        type = ContentType.AT_USER,
                        text = matched,
                    )
                    matched.startsWith("#") -> ContentPiece(
                        type = ContentType.TEXT,
                        text = matched,
                    )
                    matched.startsWith("http") -> ContentPiece(
                        type = ContentType.LINK,
                        text = matched,
                        link = matched,
                    )
                    else -> ContentPiece(type = ContentType.TEXT, text = matched)
                }
            )
            lastIndex = match.range.last + 1
        }

        // 剩余文本
        if (lastIndex < text.length) {
            pieces.add(
                ContentPiece(
                    type = ContentType.TEXT,
                    text = text.substring(lastIndex),
                )
            )
        }

        return pieces.ifEmpty {
            listOf(ContentPiece(type = ContentType.TEXT, text = text))
        }
    }
}
