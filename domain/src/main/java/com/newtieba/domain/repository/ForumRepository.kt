package com.newtieba.domain.repository

import com.newtieba.common.model.Resource
import com.newtieba.domain.model.Forum
import com.newtieba.domain.usecase.forum.SignResult
import kotlinx.coroutines.flow.Flow

/**
 * 吧仓库接口
 */
interface ForumRepository {
    /**
     * 获取关注的吧列表
     */
    fun getLikedForums(
        page: Int = 1,
        pageSize: Int = 30
    ): Flow<Resource<List<Forum>>>

    /**
     * 获取推荐的吧列表
     */
    fun getRecommendForums(
        page: Int = 1,
        pageSize: Int = 30
    ): Flow<Resource<List<Forum>>>

    /**
     * 搜索吧
     */
    fun searchForums(
        keyword: String,
        page: Int = 1,
        pageSize: Int = 30
    ): Flow<Resource<List<Forum>>>

    /**
     * 获取吧详情
     */
    fun getForumDetail(
        forumId: Long
    ): Flow<Resource<Forum>>

    /**
     * 关注吧
     */
    fun likeForum(
        forumId: Long,
        forumName: String
    ): Flow<Resource<Boolean>>

    /**
     * 取关吧
     */
    fun unlikeForum(
        forumId: Long,
        forumName: String
    ): Flow<Resource<Boolean>>

    /**
     * 吧签到
     */
    fun signForum(
        forumId: Long,
        forumName: String
    ): Flow<Resource<SignResult>>
}
