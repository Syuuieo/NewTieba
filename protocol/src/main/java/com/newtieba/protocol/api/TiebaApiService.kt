package com.newtieba.protocol.api

import com.newtieba.protocol.api.endpoints.*
import com.newtieba.protocol.api.models.request.CommonReq
import com.newtieba.protocol.api.models.response.*
import com.newtieba.protocol.auth.AuthManager
import com.newtieba.protocol.crypto.SignCalculator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * 贴吧API服务
 * 封装所有API调用
 */
class TiebaApiService(
    private val authApi: AuthApi,
    private val forumApi: ForumApi,
    private val threadApi: ThreadApi,
    private val userApi: UserApi,
    private val messageApi: MessageApi,
    private val authManager: AuthManager
) {
    /**
     * 获取通用请求参数
     */
    private fun getCommonReq(): CommonReq {
        return CommonReq.create(
            bduss = authManager.getBduss(),
            sToken = authManager.getSToken(),
            tbs = authManager.getTbs(),
            clientId = authManager.getClientId(),
            cuid = authManager.getCuid(),
            cuidGalaxy2 = authManager.getCuidGalaxy2(),
            c3Aid = authManager.getC3Aid(),
            androidId = authManager.getAndroidId(),
            sampleId = "",
            zId = ""
        )
    }

    /**
     * 登录
     */
    fun login(bduss: String, sToken: String): Flow<LoginRes> = flow {
        val response = authApi.getLoginInfo(bduss = bduss, sToken = sToken)
        emit(response)
    }

    /**
     * 获取用户信息
     */
    fun getUserInfo(): Flow<UserRes> = flow {
        val response = authApi.getCurrentUserInfo(bduss = authManager.getBduss())
        emit(response)
    }

    /**
     * 获取吧帖子列表
     */
    fun getForumPage(
        forumName: String,
        page: Int = 1,
        pageSize: Int = 30,
        sortType: Int = 0,
        isGood: Int = 0,
        categoryId: Int = 0,
        loadType: Int = 1
    ): Flow<FrsPageRes> = flow {
        val response = forumApi.getForumPage(
            bduss = authManager.getBduss(),
            forumName = forumName,
            page = page,
            pageSize = pageSize,
            sortType = sortType,
            isGood = isGood,
            categoryId = categoryId,
            loadType = loadType
        )
        emit(response)
    }

    /**
     * 获取吧详情
     */
    fun getForumDetail(forumId: Long): Flow<ForumDetailRes> = flow {
        val response = forumApi.getForumDetail(
            bduss = authManager.getBduss(),
            forumId = forumId
        )
        emit(response)
    }

    /**
     * 关注吧
     */
    fun likeForum(forumId: Long, forumName: String): Flow<CommonRes> = flow {
        val response = forumApi.likeForum(
            bduss = authManager.getBduss(),
            tbs = authManager.getTbs(),
            forumId = forumId,
            forumName = forumName
        )
        emit(response)
    }

    /**
     * 取关吧
     */
    fun unlikeForum(forumId: Long, forumName: String): Flow<CommonRes> = flow {
        val response = forumApi.unlikeForum(
            bduss = authManager.getBduss(),
            tbs = authManager.getTbs(),
            forumId = forumId,
            forumName = forumName
        )
        emit(response)
    }

    /**
     * 吧签到
     */
    fun signForum(forumId: Long, forumName: String): Flow<SignForumRes> = flow {
        val response = forumApi.signForum(
            bduss = authManager.getBduss(),
            tbs = authManager.getTbs(),
            forumId = forumId,
            forumName = forumName
        )
        emit(response)
    }

    /**
     * 获取帖子详情
     */
    fun getThreadDetail(
        threadId: Long,
        page: Int = 1,
        pageSize: Int = 30,
        seeLz: Boolean = false,
        postId: Long = 0,
        sortType: Int = 0,
        back: Boolean = false,
        forumId: Long = 0,
        stType: String = "",
        mark: Int = 0
    ): Flow<PbPageRes> = flow {
        val response = threadApi.getThreadDetail(
            bduss = authManager.getBduss(),
            threadId = threadId,
            page = page,
            pageSize = pageSize,
            seeLz = if (seeLz) 1 else 0,
            postId = postId,
            sortType = sortType,
            back = if (back) 1 else 0,
            forumId = forumId,
            stType = stType,
            mark = mark
        )
        emit(response)
    }

    /**
     * 获取楼中楼
     */
    fun getComments(
        threadId: Long,
        postId: Long,
        subPostId: Long = 0,
        page: Int = 1,
        forumId: Long = 0
    ): Flow<CommentsRes> = flow {
        val response = threadApi.getComments(
            bduss = authManager.getBduss(),
            threadId = threadId,
            postId = postId,
            subPostId = subPostId,
            page = page,
            forumId = forumId
        )
        emit(response)
    }

    /**
     * 发帖
     */
    fun createThread(
        forumId: Long,
        forumName: String,
        title: String,
        content: String,
        isHide: Int = 0,
        isTitle: Int = 0
    ): Flow<CommonRes> = flow {
        val response = threadApi.createThread(
            bduss = authManager.getBduss(),
            tbs = authManager.getTbs(),
            forumId = forumId,
            forumName = forumName,
            title = title,
            content = content,
            isHide = isHide,
            isTitle = isTitle
        )
        emit(response)
    }

    /**
     * 回复帖子
     */
    fun replyThread(
        forumId: Long,
        forumName: String,
        threadId: Long,
        content: String,
        postId: Long = 0,
        subPostId: Long = 0,
        replyUserId: Long = 0
    ): Flow<CommonRes> = flow {
        val response = threadApi.replyThread(
            bduss = authManager.getBduss(),
            tbs = authManager.getTbs(),
            forumId = forumId,
            forumName = forumName,
            threadId = threadId,
            content = content,
            postId = postId,
            subPostId = subPostId,
            replyUserId = replyUserId
        )
        emit(response)
    }

    /**
     * 点赞
     */
    fun agree(
        threadId: Long,
        postId: Long,
        opType: Int = 0,
        objType: Int = 0,
        agreeType: Int = 2
    ): Flow<CommonRes> = flow {
        val response = threadApi.agree(
            bduss = authManager.getBduss(),
            tbs = authManager.getTbs(),
            threadId = threadId,
            postId = postId,
            opType = opType,
            objType = objType,
            agreeType = agreeType
        )
        emit(response)
    }

    /**
     * 收藏帖子
     */
    fun storeThread(threadId: Long, postId: Long = 0): Flow<CommonRes> = flow {
        val response = threadApi.storeThread(
            bduss = authManager.getBduss(),
            tbs = authManager.getTbs(),
            threadId = threadId,
            postId = postId
        )
        emit(response)
    }

    /**
     * 取消收藏
     */
    fun unstoreThread(threadId: Long): Flow<CommonRes> = flow {
        val response = threadApi.unstoreThread(
            bduss = authManager.getBduss(),
            tbs = authManager.getTbs(),
            threadId = threadId
        )
        emit(response)
    }

    /**
     * 删除帖子
     */
    fun deleteThread(forumId: Long, threadId: Long): Flow<CommonRes> = flow {
        val response = threadApi.deleteThread(
            bduss = authManager.getBduss(),
            tbs = authManager.getTbs(),
            forumId = forumId,
            threadId = threadId
        )
        emit(response)
    }

    /**
     * 获取用户信息
     */
    fun getUserProfile(uid: Long): Flow<UserRes> = flow {
        val response = userApi.getUserProfile(
            bduss = authManager.getBduss(),
            uid = uid
        )
        emit(response)
    }

    /**
     * 关注用户
     */
    fun followUser(portrait: String): Flow<CommonRes> = flow {
        val response = userApi.followUser(
            bduss = authManager.getBduss(),
            tbs = authManager.getTbs(),
            portrait = portrait
        )
        emit(response)
    }

    /**
     * 取关用户
     */
    fun unfollowUser(portrait: String): Flow<CommonRes> = flow {
        val response = userApi.unfollowUser(
            bduss = authManager.getBduss(),
            tbs = authManager.getTbs(),
            portrait = portrait
        )
        emit(response)
    }

    /**
     * 获取用户关注的吧列表
     */
    fun getUserForumList(uid: Long, page: Int = 1): Flow<UserForumListRes> = flow {
        val response = userApi.getUserForumList(
            bduss = authManager.getBduss(),
            uid = uid,
            page = page
        )
        emit(response)
    }

    /**
     * 获取用户发布的帖子
     */
    fun getUserThreads(uid: Long, page: Int = 1): Flow<UserThreadsRes> = flow {
        val response = userApi.getUserThreads(
            bduss = authManager.getBduss(),
            uid = uid,
            page = page
        )
        emit(response)
    }

    /**
     * 获取消息数量
     */
    fun getMessageCount(): Flow<MessageCountRes> = flow {
        val response = messageApi.getMessageCount(bduss = authManager.getBduss())
        emit(response)
    }

    /**
     * 获取回复我的消息
     */
    fun getReplyMe(page: Int = 1): Flow<MessageListRes> = flow {
        val response = messageApi.getReplyMe(
            bduss = authManager.getBduss(),
            page = page
        )
        emit(response)
    }

    /**
     * 获取@我的消息
     */
    fun getAtMe(page: Int = 1): Flow<MessageListRes> = flow {
        val response = messageApi.getAtMe(
            bduss = authManager.getBduss(),
            page = page
        )
        emit(response)
    }

    /**
     * 获取赞我的消息
     */
    fun getAgreeMe(page: Int = 1): Flow<MessageListRes> = flow {
        val response = messageApi.getAgreeMe(
            bduss = authManager.getBduss(),
            page = page
        )
        emit(response)
    }

    /**
     * 获取收藏的帖子
     */
    fun getStoredThreads(page: Int = 1): Flow<StoredThreadsRes> = flow {
        val response = messageApi.getStoredThreads(
            bduss = authManager.getBduss(),
            page = page
        )
        emit(response)
    }

    companion object {
        @Volatile
        private var INSTANCE: TiebaApiService? = null

        fun getInstance(
            authApi: AuthApi,
            forumApi: ForumApi,
            threadApi: ThreadApi,
            userApi: UserApi,
            messageApi: MessageApi,
            authManager: AuthManager
        ): TiebaApiService {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: TiebaApiService(
                    authApi,
                    forumApi,
                    threadApi,
                    userApi,
                    messageApi,
                    authManager
                ).also { INSTANCE = it }
            }
        }
    }
}
