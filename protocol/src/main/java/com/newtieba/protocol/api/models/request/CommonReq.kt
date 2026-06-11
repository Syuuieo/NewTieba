package com.newtieba.protocol.api.models.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 通用请求参数
 * 基于 aiotieba 的 CommonReq.proto 迁移
 */
@Serializable
data class CommonReq(
    @SerialName("_client_type")
    val clientType: Int = 2, // Android客户端

    @SerialName("_client_version")
    val clientVersion: String = "12.64.1.1",

    @SerialName("_client_id")
    val clientId: String = "",

    @SerialName("_phone_imei")
    val phoneImei: String = "000000000000000",

    @SerialName("_from")
    val from: String = "1008621x",

    @SerialName("cuid")
    val cuid: String = "",

    @SerialName("_timestamp")
    val timestamp: Long = System.currentTimeMillis(),

    @SerialName("model")
    val model: String = "SM-G988N",

    @SerialName("BDUSS")
    val bduss: String = "",

    @SerialName("tbs")
    val tbs: String = "",

    @SerialName("net_type")
    val netType: Int = 1, // wifi

    @SerialName("stoken")
    val sToken: String = "",

    @SerialName("z_id")
    val zId: String = "",

    @SerialName("cuid_galaxy2")
    val cuidGalaxy2: String = "",

    @SerialName("c3_aid")
    val c3Aid: String = "",

    @SerialName("sample_id")
    val sampleId: String = "",

    @SerialName("android_id")
    val androidId: String = "",

    @SerialName("os_version")
    val osVersion: String = android.os.Build.VERSION.SDK_INT.toString(),

    @SerialName("brand")
    val brand: String = android.os.Build.BRAND,

    @SerialName("screen_width")
    val screenWidth: Int = 1080,

    @SerialName("screen_height")
    val screenHeight: Int = 2340,

    @SerialName("sdk_ver")
    val sdkVer: String = "2.34.0",

    @SerialName("framework_ver")
    val frameworkVer: String = "3340042",

    @SerialName("first_install_time")
    val firstInstallTime: Long = 0,

    @SerialName("last_update_time")
    val lastUpdateTime: Long = 0,

    @SerialName("active_timestamp")
    val activeTimestamp: Long = 0,

    @SerialName("start_type")
    val startType: Int = 1,

    @SerialName("start_scheme")
    val startScheme: String = "",

    @SerialName("is_teenager")
    val isTeenager: Int = 0,

    @SerialName("extra")
    val extra: String = "",

    @SerialName("mac")
    val mac: String = "02:00:00:00:00:00",

    @SerialName("ba_id")
    val baId: String = "",

    @SerialName("cmode")
    val cmode: Int = 1,

    @SerialName("personalized_rec_switch")
    val personalizedRecSwitch: Int = 1
) {
    /**
     * 转换为表单参数列表
     */
    fun toFormParams(): List<Pair<String, String>> {
        return listOf(
            "_client_type" to clientType.toString(),
            "_client_version" to clientVersion,
            "_client_id" to clientId,
            "_phone_imei" to phoneImei,
            "_from" to from,
            "cuid" to cuid,
            "_timestamp" to timestamp.toString(),
            "model" to model,
            "BDUSS" to bduss,
            "tbs" to tbs,
            "net_type" to netType.toString(),
            "stoken" to sToken,
            "z_id" to zId,
            "cuid_galaxy2" to cuidGalaxy2,
            "c3_aid" to c3Aid,
            "sample_id" to sampleId,
            "android_id" to androidId,
            "os_version" to osVersion,
            "brand" to brand,
            "screen_width" to screenWidth.toString(),
            "screen_height" to screenHeight.toString(),
            "sdk_ver" to sdkVer,
            "framework_ver" to frameworkVer,
            "first_install_time" to firstInstallTime.toString(),
            "last_update_time" to lastUpdateTime.toString(),
            "active_timestamp" to activeTimestamp.toString(),
            "start_type" to startType.toString(),
            "start_scheme" to startScheme,
            "is_teenager" to isTeenager.toString(),
            "extra" to extra,
            "mac" to mac,
            "ba_id" to baId,
            "cmode" to cmode.toString(),
            "personalized_rec_switch" to personalizedRecSwitch.toString()
        ).filter { it.second.isNotBlank() }
    }

    companion object {
        /**
         * 创建带有默认值的请求
         */
        fun create(
            bduss: String = "",
            sToken: String = "",
            tbs: String = "",
            clientId: String = "",
            cuid: String = "",
            cuidGalaxy2: String = "",
            c3Aid: String = "",
            androidId: String = "",
            sampleId: String = "",
            zId: String = ""
        ): CommonReq {
            return CommonReq(
                bduss = bduss,
                sToken = sToken,
                tbs = tbs,
                clientId = clientId,
                cuid = cuid,
                cuidGalaxy2 = cuidGalaxy2,
                c3Aid = c3Aid,
                androidId = androidId,
                sampleId = sampleId,
                zId = zId,
                timestamp = System.currentTimeMillis(),
                firstInstallTime = System.currentTimeMillis(),
                lastUpdateTime = System.currentTimeMillis(),
                activeTimestamp = System.currentTimeMillis()
            )
        }
    }
}
