package com.newtieba.protocol.api.models.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 获取帖子详情请求
 * 基于 aiotieba 的 PbPageReqIdl.proto 迁移
 */
@Serializable
data class PbPageReq(
    @SerialName("common")
    val common: CommonReq = CommonReq(),

    @SerialName("kz")
    val threadId: Long = 0, // 帖子ID

    @SerialName("lz")
    val seeLz: Int = 0, // 只看楼主（0=否 1=是）

    @SerialName("r")
    val sortType: Int = 0, // 排序方式

    @SerialName("pid")
    val postId: Long = 0, // 回复ID

    @SerialName("with_floor")
    val withFloor: Int = 1, // 是否带楼中楼

    @SerialName("floor_rn")
    val floorRn: Int = 3, // 楼中楼数量

    @SerialName("rn")
    val pageSize: Int = 30, // 请求条数

    @SerialName("pn")
    val page: Int = 1, // 页码

    @SerialName("floor_sort_type")
    val floorSortType: Int = 0, // 楼中楼排序方式

    @SerialName("back")
    val back: Int = 0, // 是否向前翻页

    @SerialName("st_type")
    val stType: String = "", // 来源类型

    @SerialName("mark")
    val mark: Int = 0, // 是否收藏

    @SerialName("forum_id")
    val forumId: Long = 0 // 吧ID
) {
    /**
     * 转换为表单参数列表
     */
    fun toFormParams(): List<Pair<String, String>> {
        val params = mutableListOf<Pair<String, String>>()
        params.addAll(common.toFormParams())
        params.add("kz" to threadId.toString())
        params.add("lz" to seeLz.toString())
        params.add("r" to sortType.toString())
        params.add("pid" to postId.toString())
        params.add("with_floor" to withFloor.toString())
        params.add("floor_rn" to floorRn.toString())
        params.add("rn" to pageSize.toString())
        params.add("pn" to page.toString())
        params.add("floor_sort_type" to floorSortType.toString())
        params.add("back" to back.toString())
        params.add("st_type" to stType)
        params.add("mark" to mark.toString())
        params.add("forum_id" to forumId.toString())
        return params
    }

    companion object {
        /**
         * 创建请求
         */
        fun create(
            threadId: Long,
            page: Int = 1,
            pageSize: Int = 30,
            seeLz: Boolean = false,
            postId: Long = 0,
            sortType: Int = 0,
            back: Boolean = false,
            forumId: Long = 0,
            stType: String = "",
            mark: Int = 0,
            common: CommonReq = CommonReq()
        ): PbPageReq {
            return PbPageReq(
                common = common,
                threadId = threadId,
                page = page,
                pageSize = pageSize,
                seeLz = if (seeLz) 1 else 0,
                postId = postId,
                sortType = sortType,
                back = if (back) 1 else 0,
                forumId = forumId,
                stType = stType,
                mark = mark
            )
        }
    }
}
