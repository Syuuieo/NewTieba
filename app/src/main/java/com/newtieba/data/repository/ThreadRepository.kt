package com.newtieba.data.repository

import com.newtieba.data.api.interceptor.ProtoHelper
import com.newtieba.data.api.interceptor.ProtoMapper
import com.newtieba.data.api.service.TiebaAppApiService
import com.newtieba.data.model.Post
import javax.inject.Inject

class ThreadRepository @Inject constructor(
    private val appApi: TiebaAppApiService,
) {

    /**
     * 获取帖子楼层列表
     * 使用 ProtoReader 直接解析 PbPageResIdl
     */
    suspend fun getPosts(
        tid: Long,
        page: Int = 1,
    ): Result<List<Post>> = runCatching {
        val requestBody = ProtoHelper.buildPbPageRequest(tid = tid, page = page)
        val responseBytes = appApi.getPosts(requestBody).bytes()
        val decompressed = ProtoHelper.decompress(responseBytes)
        ProtoMapper.parsePosts(decompressed)
    }

    /**
     * 获取楼中楼
     * TODO: 实现 PbFloorResIdl 解析器
     */
    suspend fun getComments(
        tid: Long,
        pid: Long,
        page: Int = 1,
    ): Result<List<Post>> = runCatching {
        val protoBytes = ProtoHelper.buildProtoMessage {
            ProtoHelper.writeMessageField(1) {
                ProtoHelper.writeUInt64Field(1, tid)
                ProtoHelper.writeUInt64Field(2, pid)
                ProtoHelper.writeUInt32Field(3, page)
            }
        }
        val responseBytes = appApi.getComments(
            ProtoHelper.buildRequestBody(protoBytes)
        ).bytes()
        val decompressed = ProtoHelper.decompress(responseBytes)
        // TODO: 实现 PbFloorResIdl → List<Post> 解析
        emptyList()
    }

    suspend fun agreePost(tid: Long, pid: Long, isAgree: Boolean = true): Result<Boolean> =
        runCatching {
            appApi.agree(ProtoHelper.buildProtoMessage { })
            true
        }
}
