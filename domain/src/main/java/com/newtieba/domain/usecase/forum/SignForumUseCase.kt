package com.newtieba.domain.usecase.forum

import com.newtieba.common.model.Resource
import com.newtieba.domain.repository.ForumRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 吧签到UseCase
 */
class SignForumUseCase @Inject constructor(
    private val forumRepository: ForumRepository
) {
    /**
     * 吧签到
     * @param forumId 吧ID
     * @param forumName 吧名
     */
    operator fun invoke(
        forumId: Long,
        forumName: String
    ): Flow<Resource<SignResult>> {
        return forumRepository.signForum(forumId, forumName)
    }
}

/**
 * 签到结果
 */
data class SignResult(
    val signBonusPoint: Int,
    val userSignRank: Int,
    val signTime: Long
)
