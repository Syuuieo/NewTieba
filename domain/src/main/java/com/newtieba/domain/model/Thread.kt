package com.newtieba.domain.model

/**
 * 帖子模型
 */
data class Thread(
    val id: Long,
    val title: String,
    val content: String,
    val authorId: Long,
    val authorName: String,
    val authorAvatar: String,
    val authorLevel: Int,
    val forumId: Long,
    val forumName: String,
    val replyCount: Int,
    val viewCount: Int,
    val likeCount: Int,
    val shareCount: Int,
    val isTop: Boolean,
    val isGood: Boolean,
    val isLiked: Boolean,
    val isCollected: Boolean,
    val createTime: Long,
    val lastReplyTime: Long,
    val images: List<String>,
    val abstract: String
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
                    val format = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                    format.format(date)
                }
            }
        }

    /**
     * 格式化的回复数
     */
    val replyCountFormatted: String
        get() = when {
            replyCount < 10000 -> replyCount.toString()
            replyCount < 100000000 -> "${replyCount / 10000}万"
            else -> "${replyCount / 100000000}亿"
        }

    /**
     * 格式化的浏览数
     */
    val viewCountFormatted: String
        get() = when {
            viewCount < 10000 -> viewCount.toString()
            viewCount < 100000000 -> "${viewCount / 10000}万"
            else -> "${viewCount / 100000000}亿"
        }
}
