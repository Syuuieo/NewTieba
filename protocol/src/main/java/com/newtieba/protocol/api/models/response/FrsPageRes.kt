package com.newtieba.protocol.api.models.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 吧帖子列表响应
 * 基于 aiotieba 的 FrsPageResIdl.proto 迁移
 */
@Serializable
data class FrsPageRes(
    @SerialName("error")
    val error: ErrorRes? = null,

    @SerialName("data")
    val data: FrsPageData? = null
) {
    @Serializable
    data class FrsPageData(
        @SerialName("forum")
        val forum: ForumInfo? = null,

        @SerialName("thread_list")
        val threadList: List<ThreadInfo> = emptyList(),

        @SerialName("page")
        val page: PageInfo? = null,

        @SerialName("sort_type")
        val sortType: Int = 0
    )

    @Serializable
    data class ForumInfo(
        @SerialName("id")
        val id: Long = 0,

        @SerialName("name")
        val name: String = "",

        @SerialName("member_num")
        val memberNum: Int = 0,

        @SerialName("thread_num")
        val threadNum: Int = 0,

        @SerialName("post_num")
        val postNum: Int = 0,

        @SerialName("slogan")
        val slogan: String = "",

        @SerialName("avatar")
        val avatar: String = "",

        @SerialName("level_id")
        val levelId: Int = 0,

        @SerialName("level_name")
        val levelName: String = "",

        @SerialName("is_sign")
        val isSign: Int = 0
    )

    @Serializable
    data class ThreadInfo(
        @SerialName("id")
        val id: Long = 0,

        @SerialName("title")
        val title: String = "",

        @SerialName("abstract")
        val abstract: List<AbstractInfo> = emptyList(),

        @SerialName("author")
        val author: UserInfo? = null,

        @SerialName("create_time")
        val createTime: Long = 0,

        @SerialName("last_time_int")
        val lastTimeInt: Long = 0,

        @SerialName("reply_num")
        val replyNum: Int = 0,

        @SerialName("view_num")
        val viewNum: Int = 0,

        @SerialName("is_good")
        val isGood: Int = 0,

        @SerialName("is_top")
        val isTop: Int = 0,

        @SerialName("is_ntitle")
        val isNtitle: Int = 0,

        @SerialName("media")
        val media: List<MediaInfo> = emptyList(),

        @SerialName("zan")
        val zan: ZanInfo? = null,

        @SerialName("agree")
        val agree: AgreeInfo? = null,

        @SerialName("share_num")
        val shareNum: Int = 0,

        @SerialName("is_voice_thread")
        val isVoiceThread: Int = 0
    )

    @Serializable
    data class AbstractInfo(
        @SerialName("type")
        val type: Int = 0,

        @SerialName("text")
        val text: String = ""
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

        @SerialName("level_id")
        val levelId: Int = 0,

        @SerialName("is_bawu")
        val isBawu: Int = 0,

        @SerialName("bawu_type")
        val bawuType: String = "",

        @SerialName("is_god")
        val isGod: Int = 0,

        @SerialName("vip")
        val vip: Int = 0
    )

    @Serializable
    data class MediaInfo(
        @SerialName("type")
        val type: Int = 0,

        @SerialName("src")
        val src: String = "",

        @SerialName("width")
        val width: Int = 0,

        @SerialName("height")
        val height: Int = 0,

        @SerialName("size")
        val size: Long = 0,

        @SerialName("big_src")
        val bigSrc: String = "",

        @SerialName("original_src")
        val originalSrc: String = ""
    )

    @Serializable
    data class ZanInfo(
        @SerialName("num")
        val num: Int = 0,

        @SerialName("status")
        val status: Int = 0
    )

    @Serializable
    data class AgreeInfo(
        @SerialName("agree_num")
        val agreeNum: Int = 0,

        @SerialName("disagree_num")
        val disagreeNum: Int = 0,

        @SerialName("agree_type")
        val agreeType: Int = 0
    )

    @Serializable
    data class PageInfo(
        @SerialName("page_size")
        val pageSize: Int = 0,

        @SerialName("offset")
        val offset: Int = 0,

        @SerialName("current_page")
        val currentPage: Int = 0,

        @SerialName("total_page")
        val totalPage: Int = 0,

        @SerialName("total_count")
        val totalCount: Int = 0,

        @SerialName("has_more")
        val hasMore: Boolean = false,

        @SerialName("has_prev")
        val hasPrev: Boolean = false
    )
}

@Serializable
data class ErrorRes(
    @SerialName("error_code")
    val errorCode: String = "",

    @SerialName("error_msg")
    val errorMsg: String = ""
)
