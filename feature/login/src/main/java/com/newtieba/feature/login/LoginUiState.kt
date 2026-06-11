package com.newtieba.feature.login

/**
 * 登录UI状态
 */
data class LoginUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isLoggedIn: Boolean = false,
    val loginMethod: LoginMethod = LoginMethod.WEBVIEW,
    val bduss: String = "",
    val sToken: String = "",
    val userName: String = "",
    val userAvatar: String = ""
)

/**
 * 登录方式
 */
enum class LoginMethod {
    WEBVIEW,    // WebView登录
    COOKIE      // Cookie登录
}

/**
 * WebView登录状态
 */
data class WebViewLoginState(
    val isLoading: Boolean = false,
    val url: String = "https://tieba.baidu.com/mo/q/setCookie",
    val error: String? = null,
    val cookies: Map<String, String> = emptyMap()
)

/**
 * Cookie登录状态
 */
data class CookieLoginState(
    val isLoading: Boolean = false,
    val bduss: String = "",
    val sToken: String = "",
    val error: String? = null
)
