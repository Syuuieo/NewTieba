package com.newtieba.domain.usecase.thread

import com.newtieba.common.model.Resource
import com.newtieba.domain.model.Comment
import com.newtieba.domain.repository.ThreadDetail
import com.newtieba.domain.repository.ThreadRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 获取帖子详情UseCase
 */
class GetThreadDetailUseCase @Inject constructor(
    private val threadRepository: ThreadRepository
) {
    /**
     * 获取帖子详情
     * @param threadId 帖子ID
     * @param page 页码
     * @param seeLz 是否只看楼主
     */
    operator fun invoke(
        threadId: Long,
        page: Int = 1,
        seeLz: Boolean = false
    ): Flow<Resource<ThreadDetail>> {
        return threadRepository.getThreadDetail(threadId, page, seeLz)
    }

    /**
     * 获取楼中楼评论
     * @param threadId 帖子ID
     * @param postId 回复ID
     * @param page 页码
     */
    fun getComments(
        threadId: Long,
        postId: Long,
        page: Int = 1
    ): Flow<Resource<List<Comment>>> {
        return threadRepository.getComments(threadId, postId, page)
    }
}
