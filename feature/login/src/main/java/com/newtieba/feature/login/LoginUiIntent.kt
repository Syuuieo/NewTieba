package com.newtieba.feature.login

/**
 * 登录UI意图
 */
sealed interface LoginUiIntent {
    /**
     * 切换登录方式
     */
    data class SwitchLoginMethod(val method: LoginMethod) : LoginUiIntent

    /**
     * WebView登录成功
     */
    data class WebViewLoginSuccess(val cookies: Map<String, String>) : LoginUiIntent

    /**
     * WebView登录失败
     */
    data class WebViewLoginError(val error: String) : LoginUiIntent

    /**
     * Cookie登录
     */
    data class CookieLogin(val bduss: String, val sToken: String) : LoginUiIntent

    /**
     * 更新BDUSS
     */
    data class UpdateBduss(val bduss: String) : LoginUiIntent

    /**
     * 更新STOKEN
     */
    data class UpdateSToken(val sToken: String) : LoginUiIntent

    /**
     * 退出登录
     */
    data object Logout : LoginUiIntent

    /**
     * 清除错误
     */
    data object ClearError : LoginUiIntent
}
