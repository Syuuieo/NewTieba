package com.newtieba.protocol.api.endpoints

import com.newtieba.protocol.api.models.response.CommonRes
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * 消息相关API
 */
interface MessageApi {

    /**
     * 获取消息提醒数
     * POST /c/s/msg
     */
    @FormUrlEncoded
    @POST("c/s/msg")
    suspend fun getMessageCount(
        @Field("_client_version") clientVersion: String = "12.64.1.1",
        @Field("BDUSS") bduss: String
    ): MessageCountRes

    /**
     * 获取回复我的消息
     * POST /c/s/reply
     */
    @FormUrlEncoded
    @POST("c/s/reply")
    suspend fun getReplyMe(
        @Field("_client_version") clientVersion: String = "12.64.1.1",
        @Field("BDUSS") bduss: String,
        @Field("pn") page: Int = 1,
        @Field("rn") pageSize: Int = 20
    ): MessageListRes

    /**
     * 获取@我的消息
     * POST /c/s/at
     */
    @FormUrlEncoded
    @POST("c/s/at")
    suspend fun getAtMe(
        @Field("_client_version") clientVersion: String = "12.64.1.1",
        @Field("BDUSS") bduss: String,
        @Field("pn") page: Int = 1,
        @Field("rn") pageSize: Int = 20
    ): MessageListRes

    /**
     * 获取赞我的消息
     * POST /c/s/agree
     */
    @FormUrlEncoded
    @POST("c/s/agree")
    suspend fun getAgreeMe(
        @Field("_client_version") clientVersion: String = "12.64.1.1",
        @Field("BDUSS") bduss: String,
        @Field("pn") page: Int = 1,
        @Field("rn") pageSize: Int = 20
    ): MessageListRes

    /**
     * 获取收藏的帖子
     * POST /c/s/store
     */
    @FormUrlEncoded
    @POST("c/s/store")
    suspend fun getStoredThreads(
        @Field("_client_version") clientVersion: String = "12.64.1.1",
        @Field("BDUSS") bduss: String,
        @Field("pn") page: Int = 1,
        @Field("rn") pageSize: Int = 20
    ): StoredThreadsRes
}

/**
 * 消息数量响应
 */
@kotlinx.serialization.Serializable
data class MessageCountRes(
    @kotlinx.serialization.SerialName("error")
    val error: com.newtieba.protocol.api.models.response.ErrorRes? = null,

    @kotlinx.serialization.SerialName("data")
    val data: MessageCountData? = null
) {
    @kotlinx.serialization.Serializable
    data class MessageCountData(
        @kotlinx.serialization.SerialName("reply_me")
        val replyMe: Int = 0,

        @kotlinx.serialization.SerialName("at_me")
        val atMe: Int = 0,

        @kotlinx.serialization.SerialName("agree_me")
        val agreeMe: Int = 0,

        @kotlinx.serialization.SerialName("msg")
        val msg: Int = 0
    )
}

/**
 * 消息列表响应
 */
@kotlinx.serialization.Serializable
data class MessageListRes(
    @kotlinx.serialization.SerialName("error")
    val error: com.newtieba.protocol.api.models.response.ErrorRes? = null,

    @kotlinx.serialization.SerialName("data")
    val data: MessageListData? = null
) {
    @kotlinx.serialization.Serializable
    data class MessageListData(
        @kotlinx.serialization.SerialName("message_list")
        val messageList: List<MessageInfo> = emptyList(),

        @kotlinx.serialization.SerialName("page")
        val page: PbPageRes.PageInfo? = null
    )

    @kotlinx.serialization.Serializable
    data class MessageInfo(
        @kotlinx.serialization.SerialName("msg_id")
        val msgId: Long = 0,

        @kotlinx.serialization.SerialName("msg_type")
        val msgType: Int = 0,

        @kotlinx.serialization.SerialName("content")
        val content: String = "",

        @kotlinx.serialization.SerialName("title")
        val title: String = "",

        @kotlinx.serialization.SerialName("create_time")
        val createTime: Long = 0,

        @kotlinx.serialization.SerialName("is_read")
        val isRead: Int = 0,

        @kotlinx.serialization.SerialName("from_user")
        val fromUser: PbPageRes.UserInfo? = null,

        @kotlinx.serialization.SerialName("thread")
        val thread: ThreadInfo? = null,

        @kotlinx.serialization.SerialName("post")
        val post: PostInfo? = null
    )

    @kotlinx.serialization.Serializable
    data class ThreadInfo(
        @kotlinx.serialization.SerialName("thread_id")
        val threadId: Long = 0,

        @kotlinx.serialization.SerialName("title")
        val title: String = ""
    )

    @kotlinx.serialization.Serializable
    data class PostInfo(
        @kotlinx.serialization.SerialName("post_id")
        val postId: Long = 0,

        @kotlinx.serialization.SerialName("content")
        val content: String = ""
    )
}

/**
 * 收藏帖子响应
 */
@kotlinx.serialization.Serializable
data class StoredThreadsRes(
    @kotlinx.serialization.SerialName("error")
    val error: com.newtieba.protocol.api.models.response.ErrorRes? = null,

    @kotlinx.serialization.SerialName("data")
    val data: StoredThreadsData? = null
) {
    @kotlinx.serialization.Serializable
    data class StoredThreadsData(
        @kotlinx.serialization.SerialName("thread_list")
        val threadList: List<StoredThreadInfo> = emptyList(),

        @kotlinx.serialization.SerialName("page")
        val page: PbPageRes.PageInfo? = null
    )

    @kotlinx.serialization.Serializable
    data class StoredThreadInfo(
        @kotlinx.serialization.SerialName("thread_id")
        val threadId: Long = 0,

        @kotlinx.serialization.SerialName("title")
        val title: String = "",

        @kotlinx.serialization.SerialName("forum_name")
        val forumName: String = "",

        @kotlinx.serialization.SerialName("create_time")
        val createTime: Long = 0,

        @kotlinx.serialization.SerialName("reply_num")
        val replyNum: Int = 0,

        @kotlinx.serialization.SerialName("last_time")
        val lastTime: Long = 0
    )
}
