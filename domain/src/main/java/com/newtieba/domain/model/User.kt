package com.newtieba.domain.model

/**
 * 用户模型
 */
data class User(
    val id: Long,
    val name: String,
    val nameShow: String,
    val portrait: String,
    val intro: String,
    val sex: Int,
    val birthday: String,
    val isVip: Boolean,
    val levelId: Int,
    val isGod: Boolean,
    val godLevel: Int,
    val postNum: Int,
    val fansNum: Int,
    val followNum: Int,
    val friendNum: Int,
    val likeNum: Int,
    val agreeNum: Int,
    val tbAge: String,
    val ipAddress: String,
    val isFollowed: Boolean,
    val hasConcern: Boolean,
    val tiebaUid: Long
) {
    /**
     * 格式化的粉丝数
     */
    val fansNumFormatted: String
        get() = when {
            fansNum < 10000 -> fansNum.toString()
            fansNum < 100000000 -> "${fansNum / 10000}万"
            else -> "${fansNum / 100000000}亿"
        }

    /**
     * 格式化的关注数
     */
    val followNumFormatted: String
        get() = when {
            followNum < 10000 -> followNum.toString()
            followNum < 100000000 -> "${followNum / 10000}万"
            else -> "${followNum / 100000000}亿"
        }

    /**
     * 格式化的帖子数
     */
    val postNumFormatted: String
        get() = when {
            postNum < 10000 -> postNum.toString()
            postNum < 100000000 -> "${postNum / 10000}万"
            else -> "${postNum / 100000000}亿"
        }

    /**
     * 性别文本
     */
    val sexText: String
        get() = when (sex) {
            1 -> "男"
            2 -> "女"
            else -> "未知"
        }

    /**
     * 头像URL
     */
    val avatarUrl: String
        get() = if (portrait.isNotBlank()) {
            "https://himg.bdimg.com/sys/portrait/item/$portrait.jpg"
        } else {
            ""
        }
}
