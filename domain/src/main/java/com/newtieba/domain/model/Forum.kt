package com.newtieba.domain.model

/**
 * 吧模型
 */
data class Forum(
    val id: Long,
    val name: String,
    val avatar: String,
    val slogan: String,
    val memberNum: Int,
    val threadNum: Int,
    val postNum: Int,
    val levelId: Int,
    val levelName: String,
    val isSigned: Boolean,
    val isLiked: Boolean,
    val isTop: Boolean,
    val curScore: Int,
    val levelupScore: Int
) {
    /**
     * 格式化的成员数
     */
    val memberNumFormatted: String
        get() = when {
            memberNum < 10000 -> memberNum.toString()
            memberNum < 100000000 -> "${memberNum / 10000}万"
            else -> "${memberNum / 100000000}亿"
        }

    /**
     * 格式化的帖子数
     */
    val threadNumFormatted: String
        get() = when {
            threadNum < 10000 -> threadNum.toString()
            threadNum < 100000000 -> "${threadNum / 10000}万"
            else -> "${threadNum / 100000000}亿"
        }

    /**
     * 等级进度
     */
    val levelProgress: Float
        get() = if (levelupScore > 0) curScore.toFloat() / levelupScore else 0f
}
