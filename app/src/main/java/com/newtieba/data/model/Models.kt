package com.newtieba.data.model

data class TiebaUser(
    val uid: Long,
    val name: String,
    val portrait: String,
    val level: Int = 0,
    val intro: String = "",
    val sex: Int = 0,
)

data class Forum(
    val fid: Long,
    val name: String,
    val avatar: String = "",
    val memberNum: Int = 0,
    val threadNum: Int = 0,
    val isFollowed: Boolean = false,
)

data class Thread(
    val tid: Long,
    val title: String,
    val author: TiebaUser,
    val replyNum: Int = 0,
    val agreeNum: Int = 0,
    val createTime: Long = 0,
    val lastTime: Long = 0,
    val abstract: String = "",
    val images: List<String> = emptyList(),
    val isGood: Boolean = false,
    val isTop: Boolean = false,
)

data class Post(
    val pid: Long,
    val floor: Int,
    val author: TiebaUser,
    val content: List<ContentPiece> = emptyList(),
    val images: List<String> = emptyList(),
    val time: Long = 0,
    val agreeNum: Int = 0,
    val subPostCount: Int = 0,
)

data class ContentPiece(
    val type: ContentType,
    val text: String = "",
    val src: String = "",
    val link: String = "",
)

enum class ContentType {
    TEXT,
    LINK,
    EMOJI,
    IMAGE,
    AT_USER,
    VIDEO,
}

data class Comment(
    val pid: Long,
    val author: TiebaUser,
    val content: List<ContentPiece> = emptyList(),
    val time: Long = 0,
)
