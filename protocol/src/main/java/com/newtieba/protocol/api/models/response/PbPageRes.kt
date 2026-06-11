package com.newtieba.protocol.api.models.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 帖子详情响应
 * 基于 aiotieba 的 PbPageResIdl.proto 迁移
 */
@Serializable
data class PbPageRes(
    @SerialName("error")
    val error: ErrorRes? = null,

    @SerialName("data")
    val data: PbPageData? = null
) {
    @Serializable
    data class PbPageData(
        @SerialName("thread")
        val thread: ThreadDetail? = null,

        @SerialName("post_list")
        val postList: List<PostInfo> = emptyList(),

        @SerialName("page")
        val page: PageInfo? = null,

        @SerialName("forum")
        val forum: ForumInfo? = null,

        @SerialName("user_list")
        val userList: List<UserInfo> = emptyList()
    )

    @Serializable
    data class ThreadDetail(
        @SerialName("id")
        val id: Long = 0,

        @SerialName("title")
        val title: String = "",

        @SerialName("author")
        val author: UserInfo? = null,

        @SerialName("create_time")
        val createTime: Long = 0,

        @SerialName("reply_num")
        val replyNum: Int = 0,

        @SerialName("view_num")
        val viewNum: Int = 0,

        @SerialName("is_good")
        val isGood: Int = 0,

        @SerialName("is_top")
        val isTop: Int = 0,

        @SerialName("is_livepost")
        val isLivepost: Int = 0,

        @SerialName("is_ntitle")
        val isNtitle: Int = 0,

        @SerialName("fid")
        val forumId: Long = 0,

        @SerialName("fname")
        val forumName: String = "",

        @SerialName("share_num")
        val shareNum: Int = 0,

        @SerialName("zan")
        val zan: ZanInfo? = null,

        @SerialName("agree")
        val agree: AgreeInfo? = null
    )

    @Serializable
    data class PostInfo(
        @SerialName("id")
        val id: Long = 0,

        @SerialName("floor")
        val floor: Int = 0,

        @SerialName("author")
        val author: UserInfo? = null,

        @SerialName("content")
        val content: List<ContentInfo> = emptyList(),

        @SerialName("create_time")
        val createTime: Long = 0,

        @SerialName("sub_post_number")
        val subPostNumber: Int = 0,

        @SerialName("sub_post_list")
        val subPostList: SubPostList? = null,

        @SerialName("zan")
        val zan: ZanInfo? = null,

        @SerialName("agree")
        val agree: AgreeInfo? = null,

        @SerialName("signature")
        val signature: SignatureInfo? = null,

        @SerialName("tail")
        val tail: TailInfo? = null
    )

    @Serializable
    data class ContentInfo(
        @SerialName("type")
        val type: Int = 0,

        @SerialName("text")
        val text: String = "",

        @SerialName("link")
        val link: String = "",

        @SerialName("src")
        val src: String = "",

        @SerialName("width")
        val width: Int = 0,

        @SerialName("height")
        val height: Int = 0,

        @SerialName("bsize")
        val bsize: Long = 0,

        @SerialName("origin_src")
        val originSrc: String = "",

        @SerialName("big_src")
        val bigSrc: String = "",

        @SerialName("voice_md5")
        val voiceMd5: String = "",

        @SerialName("during_time")
        val duringTime: Int = 0
    )

    @Serializable
    data class SubPostList(
        @SerialName("sub_post_list")
        val subPosts: List<SubPostInfo> = emptyList(),

        @SerialName("page")
        val page: PageInfo? = null
    )

    @Serializable
    data class SubPostInfo(
        @SerialName("id")
        val id: Long = 0,

        @SerialName("author")
        val author: UserInfo? = null,

        @SerialName("content")
        val content: List<ContentInfo> = emptyList(),

        @SerialName("create_time")
        val createTime: Long = 0,

        @SerialName("zan")
        val zan: ZanInfo? = null,

        @SerialName("agree")
        val agree: AgreeInfo? = null
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
        val vip: Int = 0,

        @SerialName("ip_address")
        val ipAddress: String = "",

        @SerialName("sex")
        val sex: Int = 0,

        @SerialName("fans_num")
        val fansNum: Int = 0,

        @SerialName("post_num")
        val postNum: Int = 0,

        @SerialName("agree_num")
        val agreeNum: Int = 0,

        @SerialName("friend_num")
        val friendNum: Int = 0,

        @SerialName("tieba_uid")
        val tiebaUid: Long = 0
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
    data class SignatureInfo(
        @SerialName("content")
        val content: String = ""
    )

    @Serializable
    data class TailInfo(
        @SerialName("content")
        val content: String = ""
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
        val avatar: String = ""
    )
}
