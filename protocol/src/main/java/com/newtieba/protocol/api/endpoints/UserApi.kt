package com.newtieba.protocol.api.endpoints

import com.newtieba.protocol.api.models.response.UserRes
import com.newtieba.protocol.api.models.response.CommonRes
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * 用户相关API
 */
interface UserApi {

    /**
     * 获取用户信息
     * POST /c/u/user/profile
     */
    @FormUrlEncoded
    @POST("c/u/user/profile")
    suspend fun getUserProfile(
        @Field("_client_version") clientVersion: String = "12.64.1.1",
        @Field("BDUSS") bduss: String = "",
        @Field("uid") uid: Long
    ): UserRes

    /**
     * 获取用户信息（通过portrait）
     * POST /c/u/user/profile
     */
    @FormUrlEncoded
    @POST("c/u/user/profile")
    suspend fun getUserProfileByPortrait(
        @Field("_client_version") clientVersion: String = "12.64.1.1",
        @Field("BDUSS") bduss: String = "",
        @Field("portrait") portrait: String
    ): UserRes

    /**
     * 关注用户
     * POST /c/c/user/follow
     */
    @FormUrlEncoded
    @POST("c/c/user/follow")
    suspend fun followUser(
        @Field("_client_version") clientVersion: String = "12.64.1.1",
        @Field("BDUSS") bduss: String,
        @Field("tbs") tbs: String,
        @Field("portrait") portrait: String
    ): CommonRes

    /**
     * 取关用户
     * POST /c/c/user/unfollow
     */
    @FormUrlEncoded
    @POST("c/c/user/unfollow")
    suspend fun unfollowUser(
        @Field("_client_version") clientVersion: String = "12.64.1.1",
        @Field("BDUSS") bduss: String,
        @Field("tbs") tbs: String,
        @Field("portrait") portrait: String
    ): CommonRes

    /**
     * 获取用户关注的吧列表
     * POST /c/u/user/getForumList
     */
    @FormUrlEncoded
    @POST("c/u/user/getForumList")
    suspend fun getUserForumList(
        @Field("_client_version") clientVersion: String = "12.64.1.1",
        @Field("BDUSS") bduss: String = "",
        @Field("uid") uid: Long,
        @Field("pn") page: Int = 1,
        @Field("rn") pageSize: Int = 30
    ): UserForumListRes

    /**
     * 获取用户发布的帖子
     * POST /c/u/user/thread
     */
    @FormUrlEncoded
    @POST("c/u/user/thread")
    suspend fun getUserThreads(
        @Field("_client_version") clientVersion: String = "12.64.1.1",
        @Field("BDUSS") bduss: String = "",
        @Field("uid") uid: Long,
        @Field("pn") page: Int = 1,
        @Field("rn") pageSize: Int = 30,
        @Field("is_thread") isThread: Int = 1
    ): UserThreadsRes

    /**
     * 修改用户资料
     * POST /c/c/user/modify
     */
    @FormUrlEncoded
    @POST("c/c/user/modify")
    suspend fun modifyProfile(
        @Field("_client_version") clientVersion: String = "12.64.1.1",
        @Field("BDUSS") bduss: String,
        @Field("tbs") tbs: String,
        @Field("nick_name") nickName: String,
        @Field("intro") intro: String = "",
        @Field("sex") sex: Int = 0,
        @Field("birthday") birthday: String = ""
    ): CommonRes
}

/**
 * 用户关注的吧列表响应
 */
@kotlinx.serialization.Serializable
data class UserForumListRes(
    @kotlinx.serialization.SerialName("error")
    val error: com.newtieba.protocol.api.models.response.ErrorRes? = null,

    @kotlinx.serialization.SerialName("data")
    val data: UserForumListData? = null
) {
    @kotlinx.serialization.Serializable
    data class UserForumListData(
        @kotlinx.serialization.SerialName("forum_list")
        val forumList: List<ForumInfo> = emptyList(),

        @kotlinx.serialization.SerialName("page")
        val page: PbPageRes.PageInfo? = null
    )

    @kotlinx.serialization.Serializable
    data class ForumInfo(
        @kotlinx.serialization.SerialName("forum_id")
        val forumId: Long = 0,

        @kotlinx.serialization.SerialName("forum_name")
        val forumName: String = "",

        @kotlinx.serialization.SerialName("forum_avatar")
        val forumAvatar: String = "",

        @kotlinx.serialization.SerialName("level_id")
        val levelId: Int = 0,

        @kotlinx.serialization.SerialName("cur_score")
        val curScore: Int = 0,

        @kotlinx.serialization.SerialName("levelup_score")
        val levelupScore: Int = 0
    )
}

/**
 * 用户帖子响应
 */
@kotlinx.serialization.Serializable
data class UserThreadsRes(
    @kotlinx.serialization.SerialName("error")
    val error: com.newtieba.protocol.api.models.response.ErrorRes? = null,

    @kotlinx.serialization.SerialName("data")
    val data: UserThreadsData? = null
) {
    @kotlinx.serialization.Serializable
    data class UserThreadsData(
        @kotlinx.serialization.SerialName("thread_list")
        val threadList: List<ThreadInfo> = emptyList(),

        @kotlinx.serialization.SerialName("page")
        val page: PbPageRes.PageInfo? = null
    )

    @kotlinx.serialization.Serializable
    data class ThreadInfo(
        @kotlinx.serialization.SerialName("thread_id")
        val threadId: Long = 0,

        @kotlinx.serialization.SerialName("title")
        val title: String = "",

        @kotlinx.serialization.SerialName("abstract")
        val abstract: String = "",

        @kotlinx.serialization.SerialName("forum_name")
        val forumName: String = "",

        @kotlinx.serialization.SerialName("create_time")
        val createTime: Long = 0,

        @kotlinx.serialization.SerialName("reply_num")
        val replyNum: Int = 0,

        @kotlinx.serialization.SerialName("view_num")
        val viewNum: Int = 0
    )
}
