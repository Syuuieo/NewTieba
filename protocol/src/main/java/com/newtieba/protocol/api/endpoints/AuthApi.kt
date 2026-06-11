package com.newtieba.protocol.api.endpoints

import com.newtieba.protocol.api.models.request.CommonReq
import com.newtieba.protocol.api.models.response.LoginRes
import com.newtieba.protocol.api.models.response.UserRes
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * 认证相关API
 */
interface AuthApi {

    /**
     * 登录
     * POST /c/s/login
     */
    @FormUrlEncoded
    @POST("c/s/login")
    suspend fun login(
        @Field("_client_version") clientVersion: String = "12.64.1.1",
        @Field("bdusstoken") bdussToken: String,
        @Field("stoken") sToken: String = ""
    ): LoginRes

    /**
     * 获取登录信息
     * POST /c/s/login
     */
    @FormUrlEncoded
    @POST("c/s/login")
    suspend fun getLoginInfo(
        @Field("_client_version") clientVersion: String = "22.5.1.0",
        @Field("BDUSS") bduss: String,
        @Field("stoken") sToken: String = ""
    ): LoginRes

    /**
     * 同步接口
     * POST /c/s/sync
     */
    @FormUrlEncoded
    @POST("c/s/sync")
    suspend fun sync(
        @Field("_client_version") clientVersion: String = "12.64.1.1",
        @Field("BDUSS") bduss: String = "",
        @Field("cuid") cuid: String = "",
        @Field("cuid_galaxy2") cuidGalaxy2: String = ""
    ): SyncRes

    /**
     * 获取用户信息
     * POST /c/u/user/getuserinfo
     */
    @FormUrlEncoded
    @POST("c/u/user/getuserinfo")
    suspend fun getUserInfo(
        @Field("_client_version") clientVersion: String = "12.64.1.1",
        @Field("BDUSS") bduss: String = "",
        @Field("uid") uid: String = ""
    ): UserRes

    /**
     * 获取当前用户信息
     * POST /c/u/user/getuserinfo
     */
    @FormUrlEncoded
    @POST("c/u/user/getuserinfo")
    suspend fun getCurrentUserInfo(
        @Field("_client_version") clientVersion: String = "12.64.1.1",
        @Field("BDUSS") bduss: String = ""
    ): UserRes
}

/**
 * 同步响应
 */
@kotlinx.serialization.Serializable
data class SyncRes(
    @kotlinx.serialization.SerialName("error")
    val error: com.newtieba.protocol.api.models.response.ErrorRes? = null,

    @kotlinx.serialization.SerialName("data")
    val data: SyncData? = null
) {
    @kotlinx.serialization.Serializable
    data class SyncData(
        @kotlinx.serialization.SerialName("client_id")
        val clientId: String = "",

        @kotlinx.serialization.SerialName("sample_id")
        val sampleId: String = ""
    )
}
