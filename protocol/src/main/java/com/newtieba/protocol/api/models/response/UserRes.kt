package com.newtieba.protocol.api.models.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 用户信息响应
 * 基于 aiotieba 的 GetUserRes.proto 迁移
 */
@Serializable
data class UserRes(
    @SerialName("error")
    val error: ErrorRes? = null,

    @SerialName("data")
    val data: UserData? = null
) {
    @Serializable
    data class UserData(
        @SerialName("user")
        val user: UserInfo? = null,

        @SerialName("forum_list")
        val forumList: List<ForumInfo> = emptyList(),

        @SerialName("post_list")
        val postList: List<PostInfo> = emptyList()
    )

    @Serializable
    data class UserInfo(
        @SerialName("id")
        val id: Long = 0,

        @SerialName("name")
        val name: String = "",

        @SerialName("name_show")
        val nameShow: String = "",

        @SerialName("portrait")
        val portrait: String = "",

        @SerialName("intro")
        val intro: String = "",

        @SerialName("sex")
        val sex: Int = 0,

        @SerialName("birthday")
        val birthday: String = "",

        @SerialName("is_vip")
        val isVip: Int = 0,

        @SerialName("vip")
        val vip: Int = 0,

        @SerialName("level_id")
        val levelId: Int = 0,

        @SerialName("is_god")
        val isGod: Int = 0,

        @SerialName("god_level")
        val godLevel: Int = 0,

        @SerialName("post_num")
        val postNum: Int = 0,

        @SerialName("fans_num")
        val fansNum: Int = 0,

        @SerialName("follow_num")
        val followNum: Int = 0,

        @SerialName("friend_num")
        val friendNum: Int = 0,

        @SerialName("like_num")
        val likeNum: Int = 0,

        @SerialName("agree_num")
        val agreeNum: Int = 0,

        @SerialName("tb_age")
        val tbAge: String = "",

        @SerialName("ip_address")
        val ipAddress: String = "",

        @SerialName("priv_sets")
        val privSets: PrivSets? = null,

        @SerialName("is_followed")
        val isFollowed: Int = 0,

        @SerialName("has_concern")
        val hasConcern: Int = 0,

        @SerialName("tieba_uid")
        val tiebaUid: Long = 0
    )

    @Serializable
    data class PrivSets(
        @SerialName("like")
        val like: Int = 1,

        @SerialName("reply")
        val reply: Int = 1,

        @SerialName("chat")
        val chat: Int = 1
    )

    @Serializable
    data class ForumInfo(
        @SerialName("forum_id")
        val forumId: Long = 0,

        @SerialName("forum_name")
        val forumName: String = "",

        @SerialName("forum_avatar")
        val forumAvatar: String = "",

        @SerialName("level_id")
        val levelId: Int = 0,

        @SerialName("cur_score")
        val curScore: Int = 0,

        @SerialName("levelup_score")
        val levelupScore: Int = 0
    )

    @Serializable
    data class PostInfo(
        @SerialName("thread_id")
        val threadId: Long = 0,

        @SerialName("title")
        val title: String = "",

        @SerialName("abstract")
        val abstract: String = "",

        @SerialName("forum_name")
        val forumName: String = "",

        @SerialName("create_time")
        val createTime: Long = 0,

        @SerialName("reply_num")
        val replyNum: Int = 0
    )
}

/**
 * 登录响应
 */
@Serializable
data class LoginRes(
    @SerialName("error")
    val error: ErrorRes? = null,

    @SerialName("data")
    val data: LoginData? = null
) {
    @Serializable
    data class LoginData(
        @SerialName("user")
        val user: UserRes.UserInfo? = null,

        @SerialName("tbs")
        val tbs: String = "",

        @SerialName("anti")
        val anti: AntiInfo? = null
    )

    @Serializable
    data class AntiInfo(
        @SerialName("tbs")
        val tbs: String = "",

        @SerialName("sample_id")
        val sampleId: String = ""
    )
}

/**
 * 通用响应
 */
@Serializable
data class CommonRes(
    @SerialName("error")
    val error: ErrorRes? = null,

    @SerialName("data")
    val data: CommonData? = null
) {
    @Serializable
    data class CommonData(
        @SerialName("msg")
        val msg: String = "",

        @SerialName("tbs")
        val tbs: String = ""
    )
}
