package com.newtieba.domain.model

/**
 * 回复模型
 */
data class Post(
    val id: Long,
    val threadId: Long,
    val floor: Int,
    val authorId: Long,
    val authorName: String,
    val authorAvatar: String,
    val authorLevel: Int,
    val content: List<Content>,
    val createTime: Long,
    val subPostCount: Int,
    val subPosts: List<SubPost>,
    val likeCount: Int,
    val isLiked: Boolean,
    val signature: String?,
    val tail: String?,
    val ipAddress: String?
) {
    /**
     * 格式化的创建时间
     */
    val createTimeFormatted: String
        get() {
            val now = System.currentTimeMillis()
            val diff = now - createTime
            return when {
                diff < 60 * 1000 -> "刚刚"
                diff < 60 * 60 * 1000 -> "${diff / (60 * 1000)}分钟前"
                diff < 24 * 60 * 60 * 1000 -> "${diff / (60 * 60 * 1000)}小时前"
                diff < 7 * 24 * 60 * 60 * 1000 -> "${diff / (24 * 60 * 60 * 1000)}天前"
                else -> {
                    val date = java.util.Date(createTime)
                    val format = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault())
                    format.format(date)
                }
            }
        }

    /**
     * 格式化的点赞数
     */
    val likeCountFormatted: String
        get() = when {
            likeCount < 10000 -> likeCount.toString()
            likeCount < 100000000 -> "${likeCount / 10000}万"
            else -> "${likeCount / 100000000}亿"
        }

    /**
     * 纯文本内容
     */
    val textContent: String
        get() = content.filter { it.type == ContentType.TEXT }
            .joinToString("") { it.text }

    /**
     * 图片列表
     */
    val images: List<String>
        get() = content.filter { it.type == ContentType.IMAGE }
            .map { it.src }
}

/**
 * 子回复模型
 */
data class SubPost(
    val id: Long,
    val authorId: Long,
    val authorName: String,
    val authorAvatar: String,
    val content: List<Content>,
    val createTime: Long,
    val likeCount: Int,
    val isLiked: Boolean
) {
    /**
     * 格式化的创建时间
     */
    val createTimeFormatted: String
        get() {
            val now = System.currentTimeMillis()
            val diff = now - createTime
            return when {
                diff < 60 * 1000 -> "刚刚"
                diff < 60 * 60 * 1000 -> "${diff / (60 * 1000)}分钟前"
                diff < 24 * 60 * 60 * 1000 -> "${diff / (60 * 60 * 1000)}小时前"
                diff < 7 * 24 * 60 * 60 * 1000 -> "${diff / (24 * 60 * 60 * 1000)}天前"
                else -> {
                    val date = java.util.Date(createTime)
                    val format = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault())
                    format.format(date)
                }
            }
        }

    /**
     * 纯文本内容
     */
    val textContent: String
        get() = content.filter { it.type == ContentType.TEXT }
            .joinToString("") { it.text }
}

/**
 * 内容模型
 */
data class Content(
    val type: ContentType,
    val text: String = "",
    val link: String = "",
    val src: String = "",
    val width: Int = 0,
    val height: Int = 0,
    val bsize: Long = 0,
    val originSrc: String = "",
    val bigSrc: String = "",
    val voiceMd5: String = "",
    val duringTime: Int = 0
)

/**
 * 内容类型
 */
enum class ContentType {
    TEXT,       // 文本
    LINK,      // 链接
    IMAGE,     // 图片
    VOICE,     // 语音
    VIDEO,     // 视频
    AT,        // @用户
    EMOJI,     // 表情
    POLL       // 投票
}
