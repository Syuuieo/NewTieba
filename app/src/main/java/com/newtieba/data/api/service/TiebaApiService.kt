package com.newtieba.data.api.service

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * 贴吧 App 客户端 Protobuf 接口
 * 域名: c.tieba.baidu.com
 *
 * 参考 aiotieba (lumina37/aiotieba) 的接口路径和参数命名
 */
interface TiebaAppApiService {

    // ── 数据读取接口 ──────────────────────────────────────────────

    /** 获取关注的吧列表 + 签到状态 */
    @POST("/c/f/forum/gettimeline")
    suspend fun getSelfFollowForums(@Body body: RequestBody): ResponseBody

    /** 获取贴吧广场推荐 */
    @POST("/c/f/forum/msign")
    suspend fun getSquareForums(@Body body: RequestBody): ResponseBody

    /** 获取主题帖列表（第一页约 13 个帖子） */
    @POST("/c/f/frs/page")
    suspend fun getThreads(@Body body: RequestBody): ResponseBody

    /** 获取帖子楼层 */
    @POST("/c/f/pb/page")
    suspend fun getPosts(@Body body: RequestBody): ResponseBody

    /** 获取楼中楼 */
    @POST("/c/f/pb/floor")
    suspend fun getComments(@Body body: RequestBody): ResponseBody

    /** 获取用户信息 */
    @POST("/c/u/user/getuserinfo")
    suspend fun getUserInfo(@Body body: RequestBody): ResponseBody

    /** 获取用户主页帖子 */
    @POST("/c/u/feed/userthread")
    suspend fun getUserThreads(@Body body: RequestBody): ResponseBody

    /** 获取回复消息 */
    @POST("/c/u/feed/personalreply")
    suspend fun getReplys(@Body body: RequestBody): ResponseBody

    /** 获取@消息 */
    @POST("/c/u/feed/atme")
    suspend fun getAts(@Body body: RequestBody): ResponseBody

    /** 获取私信列表 */
    @POST("/c/msg/chat/getlist")
    suspend fun getMsgList(@Body body: RequestBody): ResponseBody

    // ── 写操作接口 ──────────────────────────────────────────────

    /** 发主题帖 */
    @POST("/c/c/post/add")
    suspend fun addPost(@Body body: RequestBody): ResponseBody

    /** 点赞帖子 */
    @POST("/c/c/agree/agreePortal")
    suspend fun agree(@Body body: RequestBody): ResponseBody

    /** 关注贴吧 */
    @POST("/c/c/forum/like")
    suspend fun followForum(@Body body: RequestBody): ResponseBody

    /** 取关贴吧 */
    @POST("/c/c/forum/unlike")
    suspend fun unfollowForum(@Body body: RequestBody): ResponseBody

    /** 签到 */
    @POST("/c/c/forum/sign")
    suspend fun signForum(@Body body: RequestBody): ResponseBody
}

/**
 * 贴吧 Web JSON 接口
 * 域名: tieba.baidu.com
 */
interface TiebaWebApiService {

    /** 搜索 */
    @GET("/f/search/res")
    suspend fun search(
        @Query("ie") encoding: String = "utf-8",
        @Query("kw") keyword: String? = null,
        @Query("qw") query: String,
        @Query("pn") page: Int = 1,
        @Query("rn") pageSize: Int = 20,
    ): ResponseBody
}
