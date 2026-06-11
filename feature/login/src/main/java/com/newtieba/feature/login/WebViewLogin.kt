package com.newtieba.feature.login

import android.annotation.SuppressLint
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import top.yukonga.miuix.kmp.basic.*
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * WebView登录组件
 */
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewLogin(
    onLoginSuccess: (Map<String, String>) -> Unit,
    onError: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isLoading by remember { mutableStateOf(true) }
    var currentUrl by remember { mutableStateOf("") }

    Box(modifier = modifier.fillMaxSize()) {
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    settings.allowFileAccess = true
                    settings.allowContentAccess = true
                    settings.userAgentString = "Mozilla/5.0 (Linux; Android 13; Pixel 7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Mobile Safari/537.36"

                    webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            isLoading = false
                            currentUrl = url ?: ""

                            // 检查是否登录成功
                            val cookies = CookieManager.getInstance().getCookie(url)
                            if (cookies != null) {
                                val cookieMap = parseCookies(cookies)
                                if (cookieMap.containsKey("BDUSS")) {
                                    onLoginSuccess(cookieMap)
                                }
                            }
                        }

                        override fun onReceivedError(
                            view: WebView?,
                            errorCode: Int,
                            description: String?,
                            failingUrl: String?
                        ) {
                            super.onReceivedError(view, errorCode, description, failingUrl)
                            onError(description ?: "加载失败")
                        }
                    }

                    loadUrl("https://tieba.baidu.com/mo/q/setCookie")
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // 加载指示器
        if (isLoading) {
            LoadingState(
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

/**
 * 解析Cookie
 */
private fun parseCookies(cookieString: String): Map<String, String> {
    val cookies = mutableMapOf<String, String>()
    cookieString.split(";").forEach { cookie ->
        val parts = cookie.trim().split("=")
        if (parts.size >= 2) {
            cookies[parts[0].trim()] = parts.drop(1).joinToString("=").trim()
        }
    }
    return cookies
}

/**
 * WebView登录Screen
 */
@Composable
fun WebViewLoginScreen(
    onLoginSuccess: (Map<String, String>) -> Unit,
    onError: (String) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = "百度登录",
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(id = android.R.drawable.ic_menu_revert),
                            contentDescription = "返回"
                        )
                    }
                }
            )
        }
    ) { padding ->
        WebViewLogin(
            onLoginSuccess = onLoginSuccess,
            onError = onError,
            modifier = Modifier.padding(padding)
        )
    }
}
