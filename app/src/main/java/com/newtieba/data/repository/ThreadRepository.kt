package com.newtieba.data.repository

import com.newtieba.data.api.interceptor.ProtoHelper
import com.newtieba.data.api.interceptor.ProtoMapper
import com.newtieba.data.api.service.TiebaAppApiService
import com.newtieba.data.model.Post
import javax.inject.Inject

class ThreadRepository @Inject constructor(
    private val appApi: TiebaAppApiService,
) {

    suspend fun getPosts(
        tid: Long,
        page: Int = 1,
    ): Result<List<Post>> = runCatching {
        val protoBytes = ProtoHelper.buildProtoMessage {
            writeMessageField(1) {
                writeUInt64Field(1, tid)
                writeUInt32Field(2, page)
                writeUInt32Field(3, 30)
                writeUInt32Field(4, 0)
            }
        }
        val responseBytes = appApi.getPosts(
            ProtoHelper.buildRequestBody(protoBytes)
        ).bytes()
        val decompressed = ProtoHelper.decompress(responseBytes)
        ProtoMapper.parsePosts(decompressed)
    }

    suspend fun getComments(
        tid: Long,
        pid: Long,
        page: Int = 1,
    ): Result<List<Post>> = runCatching {
        val protoBytes = ProtoHelper.buildProtoMessage {
            writeMessageField(1) {
                writeUInt64Field(1, tid)
                writeUInt64Field(2, pid)
                writeUInt32Field(3, page)
            }
        }
        val responseBytes = appApi.getComments(
            ProtoHelper.buildRequestBody(protoBytes)
        ).bytes()
        val decompressed = ProtoHelper.decompress(responseBytes)
        emptyList()
    }

    suspend fun agreePost(tid: Long, pid: Long, isAgree: Boolean = true): Result<Boolean> =
        runCatching {
            val protoBytes = ProtoHelper.buildProtoMessage {
                writeUInt64Field(1, tid)
                writeUInt64Field(2, pid)
            }
            appApi.agree(ProtoHelper.buildRequestBody(protoBytes))
            true
        }
}
