package com.newtieba.protocol.api.endpoints

import com.newtieba.protocol.api.models.response.FrsPageRes
import com.newtieba.protocol.api.models.response.CommonRes
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * 吧相关API
 */
interface ForumApi {

    /**
     * 获取吧帖子列表
     * POST /c/f/frs/page
     */
    @FormUrlEncoded
    @POST("c/f/frs/page")
    suspend fun getForumPage(
        @Field("_client_version") clientVersion: String = "12.64.1.1",
        @Field("BDUSS") bduss: String = "",
        @Field("kw") forumName: String,
        @Field("pn") page: Int = 1,
        @Field("rn") pageSize: Int = 30,
        @Field("sort_type") sortType: Int = 0,
        @Field("is_good") isGood: Int = 0,
        @Field("cid") categoryId: Int = 0,
        @Field("load_type") loadType: Int = 1
    ): FrsPageRes

    /**
     * 获取吧详情
     * POST /c/f/forum/getforumdetail
     */
    @FormUrlEncoded
    @POST("c/f/forum/getforumdetail")
    suspend fun getForumDetail(
        @Field("_client_version") clientVersion: String = "12.64.1.1",
        @Field("BDUSS") bduss: String = "",
        @Field("forum_id") forumId: Long
    ): ForumDetailRes

    /**
     * 关注吧
     * POST /c/c/forum/like
     */
    @FormUrlEncoded
    @POST("c/c/forum/like")
    suspend fun likeForum(
        @Field("_client_version") clientVersion: String = "12.64.1.1",
        @Field("BDUSS") bduss: String,
        @Field("tbs") tbs: String,
        @Field("forum_id") forumId: Long,
        @Field("forum_name") forumName: String
    ): CommonRes

    /**
     * 取关吧
     * POST /c/c/forum/unlike
     */
    @FormUrlEncoded
    @POST("c/c/forum/unlike")
    suspend fun unlikeForum(
        @Field("_client_version") clientVersion: String = "12.64.1.1",
        @Field("BDUSS") bduss: String,
        @Field("tbs") tbs: String,
        @Field("forum_id") forumId: Long,
        @Field("forum_name") forumName: String
    ): CommonRes

    /**
     * 吧签到
     * POST /c/c/forum/sign
     */
    @FormUrlEncoded
    @POST("c/c/forum/sign")
    suspend fun signForum(
        @Field("_client_version") clientVersion: String = "12.64.1.1",
        @Field("BDUSS") bduss: String,
        @Field("tbs") tbs: String,
        @Field("forum_id") forumId: Long,
        @Field("forum_name") forumName: String
    ): SignForumRes
}

/**
 * 吧详情响应
 */
@kotlinx.serialization.Serializable
data class ForumDetailRes(
    @kotlinx.serialization.SerialName("error")
    val error: com.newtieba.protocol.api.models.response.ErrorRes? = null,

    @kotlinx.serialization.SerialName("data")
    val data: ForumDetailData? = null
) {
    @kotlinx.serialization.Serializable
    data class ForumDetailData(
        @kotlinx.serialization.SerialName("forum")
        val forum: ForumInfo? = null,

        @kotlinx.serialization.RealName("user")
        val user: UserInfo? = null
    )

    @kotlinx.serialization.Serializable
    data class ForumInfo(
        @kotlinx.serialization.SerialName("id")
        val id: Long = 0,

        @kotlinx.serialization.SerialName("name")
        val name: String = "",

        @kotlinx.serialization.SerialName("member_num")
        val memberNum: Int = 0,

        @kotlinx.serialization.SerialName("thread_num")
        val threadNum: Int = 0,

        @kotlinx.serialization.SerialName("post_num")
        val postNum: Int = 0,

        @kotlinx.serialization.SerialName("slogan")
        val slogan: String = "",

        @kotlinx.serialization.SerialName("avatar")
        val avatar: String = "",

        @kotlinx.serialization.SerialName("level_id")
        val levelId: Int = 0,

        @kotlinx.serialization.SerialName("level_name")
        val levelName: String = "",

        @kotlinx.serialization.SerialName("is_sign")
        val isSign: Int = 0,

        @kotlinx.serialization.SerialName("is_like")
        val isLike: Int = 0
    )

    @kotlinx.serialization.Serializable
    data class UserInfo(
        @kotlinx.serialization.SerialName("level_id")
        val levelId: Int = 0,

        @kotlinx.serialization.SerialName("cur_score")
        val curScore: Int = 0,

        @kotlinx.serialization.SerialName("levelup_score")
        val levelupScore: Int = 0
    )
}

/**
 * 签到响应
 */
@kotlinx.serialization.Serializable
data class SignForumRes(
    @kotlinx.serialization.SerialName("error")
    val error: com.newtieba.protocol.api.models.response.ErrorRes? = null,

    @kotlinx.serialization.SerialName("data")
    val data: SignForumData? = null
) {
    @kotlinx.serialization.Serializable
    data class SignForumData(
        @kotlinx.serialization.SerialName("sign_bonus_point")
        val signBonusPoint: Int = 0,

        @kotlinx.serialization.SerialName("user_sign_rank")
        val userSignRank: Int = 0,

        @kotlinx.serialization.SerialName("sign_time")
        val signTime: Long = 0
    )
}
