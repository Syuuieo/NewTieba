package com.newtieba.ui.login

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import top.yukonga.miuix.kmp.basic.MiuixTheme
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.basic.rememberScrollBehavior

/**
 * 贴吧 WebView 登录页
 *
 * 加载 tieba.baidu.com 登录页，用户手动登录后，
 * 通过 CookieManager 提取 BDUSS 和 STOKEN。
 */
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun LoginScreen(
    onBack: () -> Unit = {},
    onLoginSuccess: (bduss: String, stoken: String?) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = "登录百度贴吧",
                onBack = onBack,
                scrollBehavior = rememberScrollBehavior(),
            )
        },
    ) { paddingValues ->
        var isLoading by remember { mutableStateOf(true) }
        val loginUrl = remember { "https://tieba.baidu.com" }

        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    settings.userAgentString = "Mozilla/5.0 (Linux; Android 15) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.6099.230 Mobile Safari/537.36 bdtb for Android 12.59.1.0"

                    webViewClient = object : WebViewClient() {
                        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                            super.onPageStarted(view, url, favicon)
                            isLoading = true

                            // 当页面跳转到 tieba.baidu.com 主页时，尝试提取 Cookie
                            if (url?.contains("tieba.baidu.com") == true && !url.contains("passport")) {
                                val cookieManager = CookieManager.getInstance()
                                val cookies = cookieManager.getCookie("https://tieba.baidu.com") ?: ""

                                val bduss = extractCookie(cookies, "BDUSS")
                                if (bduss != null) {
                                    val stoken = extractCookie(cookies, "STOKEN")
                                    stopLoading()
                                    onLoginSuccess(bduss, stoken)
                                }
                            }
                        }

                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            isLoading = false
                        }
                    }

                    loadUrl(loginUrl)
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .then(modifier),
        )

        // 加载提示覆盖层
        if (isLoading) {
            // WebView is loading – 不需要额外 UI
        }
    }
}

/**
 * 从 Cookie 字符串中提取指定 key 的值
 */
private fun extractCookie(cookies: String, key: String): String? {
    val regex = Regex("""(?:^|;\s*)$key=([^;]+)""")
    return regex.find(cookies)?.groupValues?.getOrNull(1)
}
