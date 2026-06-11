package com.newtieba.domain.usecase.forum

import com.newtieba.common.model.Resource
import com.newtieba.domain.model.Forum
import com.newtieba.domain.repository.ForumRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 获取吧列表UseCase
 */
class GetForumListUseCase @Inject constructor(
    private val forumRepository: ForumRepository
) {
    /**
     * 获取关注的吧列表
     * @param page 页码
     * @param pageSize 每页数量
     */
    operator fun invoke(
        page: Int = 1,
        pageSize: Int = 30
    ): Flow<Resource<List<Forum>>> {
        return forumRepository.getLikedForums(page, pageSize)
    }

    /**
     * 获取推荐的吧列表
     * @param page 页码
     * @param pageSize 每页数量
     */
    fun getRecommendForums(
        page: Int = 1,
        pageSize: Int = 30
    ): Flow<Resource<List<Forum>>> {
        return forumRepository.getRecommendForums(page, pageSize)
    }

    /**
     * 搜索吧
     * @param keyword 关键词
     * @param page 页码
     * @param pageSize 每页数量
     */
    fun searchForums(
        keyword: String,
        page: Int = 1,
        pageSize: Int = 30
    ): Flow<Resource<List<Forum>>> {
        return forumRepository.searchForums(keyword, page, pageSize)
    }

    /**
     * 获取吧详情
     * @param forumId 吧ID
     */
    fun getForumDetail(
        forumId: Long
    ): Flow<Resource<Forum>> {
        return forumRepository.getForumDetail(forumId)
    }
}
