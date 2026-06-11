package com.newtieba.protocol.api.models.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 获取吧帖子列表请求
 * 基于 aiotieba 的 FrsPageReqIdl.proto 迁移
 */
@Serializable
data class FrsPageReq(
    @SerialName("common")
    val common: CommonReq = CommonReq(),

    @SerialName("kw")
    val forumName: String = "", // 吧名

    @SerialName("rn")
    val pageSize: Int = 30, // 请求条数

    @SerialName("rn_need")
    val pageSizeNeed: Int = 0,

    @SerialName("is_good")
    val isGood: Int = 0, // 是否精品区

    @SerialName("cid")
    val categoryId: Int = 0, // 分区ID

    @SerialName("pn")
    val page: Int = 1, // 页码

    @SerialName("sort_type")
    val sortType: Int = 0, // 排序方式

    @SerialName("load_type")
    val loadType: Int = 1 // 加载类型（1=下拉刷新 2=加载更多）
) {
    /**
     * 转换为表单参数列表
     */
    fun toFormParams(): List<Pair<String, String>> {
        val params = mutableListOf<Pair<String, String>>()
        params.addAll(common.toFormParams())
        params.add("kw" to forumName)
        params.add("rn" to pageSize.toString())
        params.add("rn_need" to pageSizeNeed.toString())
        params.add("is_good" to isGood.toString())
        params.add("cid" to categoryId.toString())
        params.add("pn" to page.toString())
        params.add("sort_type" to sortType.toString())
        params.add("load_type" to loadType.toString())
        return params
    }

    companion object {
        /**
         * 创建请求
         */
        fun create(
            forumName: String,
            page: Int = 1,
            pageSize: Int = 30,
            sortType: Int = 0,
            isGood: Int = 0,
            categoryId: Int = 0,
            loadType: Int = 1,
            common: CommonReq = CommonReq()
        ): FrsPageReq {
            return FrsPageReq(
                common = common,
                forumName = forumName,
                pageSize = pageSize,
                page = page,
                sortType = sortType,
                isGood = isGood,
                categoryId = categoryId,
                loadType = loadType
            )
        }
    }
}

/**
 * 排序类型
 */
enum class ForumSortType(val value: Int) {
    REPLY_TIME(0), // 回复时间
    POST_TIME(1), // 发帖时间
    HOT(2), // 热度
    RECOMMEND(3) // 推荐
}
