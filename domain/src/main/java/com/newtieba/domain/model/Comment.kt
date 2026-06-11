package com.newtieba.domain.model

/**
 * 评论模型（楼中楼）
 */
data class Comment(
    val id: Long,
    val postId: Long,
    val threadId: Long,
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
 * 消息模型
 */
data class Message(
    val id: Long,
    val type: MessageType,
    val title: String,
    val content: String,
    val fromUserId: Long,
    val fromUserName: String,
    val fromUserAvatar: String,
    val threadId: Long?,
    val threadTitle: String?,
    val postId: Long?,
    val createTime: Long,
    val isRead: Boolean
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
}

/**
 * 消息类型
 */
enum class MessageType {
    REPLY,      // 回复
    AT,         // @
    AGREE,      // 点赞
    SYSTEM      // 系统消息
}

/**
 * 通知计数模型
 */
data class NotificationCount(
    val replyMe: Int = 0,
    val atMe: Int = 0,
    val agreeMe: Int = 0,
    val systemMsg: Int = 0
) {
    val total: Int
        get() = replyMe + atMe + agreeMe + systemMsg
}
