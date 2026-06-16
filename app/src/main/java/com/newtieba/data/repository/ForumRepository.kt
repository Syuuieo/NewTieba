package com.newtieba.data.repository

import com.newtieba.data.api.interceptor.ProtoHelper
import com.newtieba.data.api.interceptor.ProtoMapper
import com.newtieba.data.api.service.TiebaAppApiService
import com.newtieba.data.model.Thread
import javax.inject.Inject

class ForumRepository @Inject constructor(
    private val appApi: TiebaAppApiService,
) {

    suspend fun getThreads(
        forumName: String,
        page: Int = 1,
        sortType: Int = 5,
        isGood: Boolean = false,
    ): Result<List<Thread>> = runCatching {
        val protoBytes = ProtoHelper.buildProtoMessage {
            writeMessageField(1) {
                writeStringField(1, forumName)
                writeUInt32Field(2, page)
                writeUInt32Field(3, 30)
                writeUInt32Field(4, sortType)
                writeUInt32Field(5, if (isGood) 1 else 0)
            }
        }
        val responseBytes = appApi.getThreads(
            ProtoHelper.buildRequestBody(protoBytes)
        ).bytes()
        val decompressed = ProtoHelper.decompress(responseBytes)
        ProtoMapper.parseThreads(decompressed)
    }

    suspend fun followForum(fid: Long): Result<Boolean> = runCatching {
        val protoBytes = ProtoHelper.buildProtoMessage {
            writeUInt64Field(1, fid)
        }
        appApi.followForum(ProtoHelper.buildRequestBody(protoBytes))
        true
    }

    suspend fun unfollowForum(fid: Long): Result<Boolean> = runCatching {
        val protoBytes = ProtoHelper.buildProtoMessage {
            writeUInt64Field(1, fid)
        }
        appApi.unfollowForum(ProtoHelper.buildRequestBody(protoBytes))
        true
    }
}
