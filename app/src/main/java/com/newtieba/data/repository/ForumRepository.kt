package com.newtieba.data.repository

import com.newtieba.data.api.interceptor.ProtoHelper
import com.newtieba.data.api.interceptor.ProtoMapper
import com.newtieba.data.api.service.TiebaAppApiService
import com.newtieba.data.model.Thread
import javax.inject.Inject

class ForumRepository @Inject constructor(
    private val appApi: TiebaAppApiService,
) {

    /**
     * 获取贴吧主题帖列表
     * 使用 ProtoReader 直接解析响应 bytes
     */
    suspend fun getThreads(
        forumName: String,
        page: Int = 1,
        sortType: Int = 5,
        isGood: Boolean = false,
    ): Result<List<Thread>> = runCatching {
        val requestBody = ProtoHelper.buildFrsPageRequest(
            forumName = forumName,
            page = page,
            sortType = sortType,
            isGood = if (isGood) 1 else 0,
        )
        val responseBytes = appApi.getThreads(requestBody).bytes()
        val decompressed = ProtoHelper.decompress(responseBytes)
        ProtoMapper.parseThreads(decompressed)
    }

    suspend fun followForum(fid: Long): Result<Boolean> = runCatching {
        val protoBytes = ProtoHelper.buildProtoMessage {
            // forum_id in proto body — exact field depends on tbindent.protobuf
        }
        appApi.followForum(ProtoHelper.buildRequestBody(protoBytes))
        true
    }

    suspend fun unfollowForum(fid: Long): Result<Boolean> = runCatching {
        appApi.unfollowForum(ProtoHelper.buildProtoMessage { })
        true
    }
}
