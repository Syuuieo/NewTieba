package com.newtieba.protocol.api.endpoints

import com.newtieba.protocol.api.models.response.PbPageRes
import com.newtieba.protocol.api.models.response.CommonRes
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * 帖子相关API
 */
interface ThreadApi {

    /**
     * 获取帖子详情
     * POST /c/f/pb/page
     */
    @FormUrlEncoded
    @POST("c/f/pb/page")
    suspend fun getThreadDetail(
        @Field("_client_version") clientVersion: String = "12.64.1.1",
        @Field("BDUSS") bduss: String = "",
        @Field("kz") threadId: Long,
        @Field("pn") page: Int = 1,
        @Field("rn") pageSize: Int = 30,
        @Field("lz") seeLz: Int = 0,
        @Field("pid") postId: Long = 0,
        @Field("sort_type") sortType: Int = 0,
        @Field("back") back: Int = 0,
        @Field("forum_id") forumId: Long = 0,
        @Field("st_type") stType: String = "",
        @Field("mark") mark: Int = 0
    ): PbPageRes

    /**
     * 获取楼中楼
     * POST /c/f/pb/floor
     */
    @FormUrlEncoded
    @POST("c/f/pb/floor")
    suspend fun getComments(
        @Field("_client_version") clientVersion: String = "12.64.1.1",
        @Field("BDUSS") bduss: String = "",
        @Field("kz") threadId: Long,
        @Field("pid") postId: Long,
        @Field("spid") subPostId: Long = 0,
        @Field("pn") page: Int = 1,
        @Field("forum_id") forumId: Long = 0
    ): CommentsRes

    /**
     * 发帖
     * POST /c/c/thread/add
     */
    @FormUrlEncoded
    @POST("c/c/thread/add")
    suspend fun createThread(
        @Field("_client_version") clientVersion: String = "12.64.1.1",
        @Field("BDUSS") bduss: String,
        @Field("tbs") tbs: String,
        @Field("fid") forumId: Long,
        @Field("kw") forumName: String,
        @Field("title") title: String,
        @Field("content") content: String,
        @Field("is_hide") isHide: Int = 0,
        @Field("is_title") isTitle: Int = 0
    ): CommonRes

    /**
     * 回复帖子
     * POST /c/c/post/add
     */
    @FormUrlEncoded
    @POST("c/c/post/add")
    suspend fun replyThread(
        @Field("_client_version") clientVersion: String = "12.64.1.1",
        @Field("BDUSS") bduss: String,
        @Field("tbs") tbs: String,
        @Field("fid") forumId: Long,
        @Field("kw") forumName: String,
        @Field("tid") threadId: Long,
        @Field("content") content: String,
        @Field("pid") postId: Long = 0,
        @Field("spid") subPostId: Long = 0,
        @Field("reply_uid") replyUserId: Long = 0
    ): CommonRes

    /**
     * 点赞
     * POST /c/c/agree/opAgree
     */
    @FormUrlEncoded
    @POST("c/c/agree/opAgree")
    suspend fun agree(
        @Field("_client_version") clientVersion: String = "12.64.1.1",
        @Field("BDUSS") bduss: String,
        @Field("tbs") tbs: String,
        @Field("tid") threadId: Long,
        @Field("pid") postId: Long,
        @Field("op_type") opType: Int = 0, // 0=点赞 1=取消点赞
        @Field("obj_type") objType: Int = 0,
        @Field("agree_type") agreeType: Int = 2
    ): CommonRes

    /**
     * 收藏帖子
     * POST /c/c/thread/store
     */
    @FormUrlEncoded
    @POST("c/c/thread/store")
    suspend fun storeThread(
        @Field("_client_version") clientVersion: String = "12.64.1.1",
        @Field("BDUSS") bduss: String,
        @Field("tbs") tbs: String,
        @Field("tid") threadId: Long,
        @Field("pid") postId: Long = 0
    ): CommonRes

    /**
     * 取消收藏
     * POST /c/c/thread/unstore
     */
    @FormUrlEncoded
    @POST("c/c/thread/unstore")
    suspend fun unstoreThread(
        @Field("_client_version") clientVersion: String = "12.64.1.1",
        @Field("BDUSS") bduss: String,
        @Field("tbs") tbs: String,
        @Field("tid") threadId: Long
    ): CommonRes

    /**
     * 删除帖子
     * POST /c/c/thread/del
     */
    @FormUrlEncoded
    @POST("c/c/thread/del")
    suspend fun deleteThread(
        @Field("_client_version") clientVersion: String = "12.64.1.1",
        @Field("BDUSS") bduss: String,
        @Field("tbs") tbs: String,
        @Field("fid") forumId: Long,
        @Field("tid") threadId: Long
    ): CommonRes
}

/**
 * 评论响应
 */
@kotlinx.serialization.Serializable
data class CommentsRes(
    @kotlinx.serialization.SerialName("error")
    val error: com.newtieba.protocol.api.models.response.ErrorRes? = null,

    @kotlinx.serialization.SerialName("data")
    val data: CommentsData? = null
) {
    @kotlinx.serialization.Serializable
    data class CommentsData(
        @kotlinx.serialization.SerialName("subpost_list")
        val subpostList: List<SubPostInfo> = emptyList(),

        @kotlinx.serialization.SerialName("page")
        val page: PbPageRes.PageInfo? = null
    )

    @kotlinx.serialization.Serializable
    data class SubPostInfo(
        @kotlinx.serialization.SerialName("id")
        val id: Long = 0,

        @kotlinx.serialization.SerialName("author")
        val author: PbPageRes.UserInfo? = null,

        @kotlinx.serialization.SerialName("content")
        val content: List<PbPageRes.ContentInfo> = emptyList(),

        @kotlinx.serialization.SerialName("create_time")
        val createTime: Long = 0,

        @kotlinx.serialization.SerialName("zan")
        val zan: PbPageRes.ZanInfo? = null,

        @kotlinx.serialization.SerialName("agree")
        val agree: PbPageRes.AgreeInfo? = null
    )
}
