package com.newtieba.domain.usecase.thread

import com.newtieba.common.model.Resource
import com.newtieba.domain.model.Thread
import com.newtieba.domain.repository.ThreadRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 获取帖子列表UseCase
 */
class GetThreadsUseCase @Inject constructor(
    private val threadRepository: ThreadRepository
) {
    /**
     * 获取推荐帖子列表
     * @param page 页码
     * @param pageSize 每页数量
     */
    operator fun invoke(
        page: Int = 1,
        pageSize: Int = 30
    ): Flow<Resource<List<Thread>>> {
        return threadRepository.getRecommendThreads(page, pageSize)
    }

    /**
     * 获取吧帖子列表
     * @param forumId 吧ID
     * @param page 页码
     * @param pageSize 每页数量
     * @param sortType 排序类型
     */
    fun getForumThreads(
        forumId: Long,
        page: Int = 1,
        pageSize: Int = 30,
        sortType: Int = 0
    ): Flow<Resource<List<Thread>>> {
        return threadRepository.getForumThreads(forumId, page, pageSize, sortType)
    }

    /**
     * 获取热榜帖子
     * @param page 页码
     * @param pageSize 每页数量
     */
    fun getHotThreads(
        page: Int = 1,
        pageSize: Int = 30
    ): Flow<Resource<List<Thread>>> {
        return threadRepository.getHotThreads(page, pageSize)
    }

    /**
     * 搜索帖子
     * @param keyword 关键词
     * @param page 页码
     * @param pageSize 每页数量
     */
    fun searchThreads(
        keyword: String,
        page: Int = 1,
        pageSize: Int = 30
    ): Flow<Resource<List<Thread>>> {
        return threadRepository.searchThreads(keyword, page, pageSize)
    }
}
