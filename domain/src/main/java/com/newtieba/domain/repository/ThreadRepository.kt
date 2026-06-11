package com.newtieba.domain.repository

import com.newtieba.common.model.Resource
import com.newtieba.domain.model.*
import kotlinx.coroutines.flow.Flow

/**
 * 帖子仓库接口
 */
interface ThreadRepository {
    /**
     * 获取推荐帖子列表
     */
    fun getRecommendThreads(
        page: Int = 1,
        pageSize: Int = 30
    ): Flow<Resource<List<Thread>>>

    /**
     * 获取吧帖子列表
     */
    fun getForumThreads(
        forumId: Long,
        page: Int = 1,
        pageSize: Int = 30,
        sortType: Int = 0
    ): Flow<Resource<List<Thread>>>

    /**
     * 获取热榜帖子
     */
    fun getHotThreads(
        page: Int = 1,
        pageSize: Int = 30
    ): Flow<Resource<List<Thread>>>

    /**
     * 搜索帖子
     */
    fun searchThreads(
        keyword: String,
        page: Int = 1,
        pageSize: Int = 30
    ): Flow<Resource<List<Thread>>>

    /**
     * 获取帖子详情
     */
    fun getThreadDetail(
        threadId: Long,
        page: Int = 1,
        seeLz: Boolean = false
    ): Flow<Resource<ThreadDetail>>

    /**
     * 获取楼中楼评论
     */
    fun getComments(
        threadId: Long,
        postId: Long,
        page: Int = 1
    ): Flow<Resource<List<Comment>>>

    /**
     * 发帖
     */
    fun createThread(
        forumId: Long,
        forumName: String,
        title: String,
        content: String
    ): Flow<Resource<Long>>

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
    ): Flow<Resource<Long>>

    /**
     * 点赞
     */
    fun agree(
        threadId: Long,
        postId: Long,
        opType: Int = 0
    ): Flow<Resource<Boolean>>

    /**
     * 收藏帖子
     */
    fun storeThread(
        threadId: Long,
        postId: Long = 0
    ): Flow<Resource<Boolean>>

    /**
     * 取消收藏
     */
    fun unstoreThread(
        threadId: Long
    ): Flow<Resource<Boolean>>

    /**
     * 删除帖子
     */
    fun deleteThread(
        forumId: Long,
        threadId: Long
    ): Flow<Resource<Boolean>>
}

/**
 * 帖子详情
 */
data class ThreadDetail(
    val thread: Thread,
    val posts: List<Post>,
    val forum: Forum,
    val currentPage: Int,
    val totalPages: Int,
    val hasMore: Boolean
)
