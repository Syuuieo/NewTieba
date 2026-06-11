package com.newtieba.feature.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.newtieba.ui.theme.TiebaTypography
import top.yukonga.miuix.kmp.basic.*
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * 登录Screen
 */
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    // 登录成功后跳转
    LaunchedEffect(uiState.isLoggedIn) {
        if (uiState.isLoggedIn) {
            onLoginSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = "登录",
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Logo
            Box(
                modifier = Modifier
                    .size(100.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_dialog_info),
                    contentDescription = "Logo",
                    modifier = Modifier.size(80.dp),
                    tint = TiebaColors.Primary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 标题
            Text(
                text = "NewTieba",
                style = TiebaTypography.h3,
                color = MiuixTheme.colorScheme.onSurface
            )

            Text(
                text = "现代化百度贴吧客户端",
                style = TiebaTypography.body2,
                color = MiuixTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(48.dp))

            // 登录方式切换
            TabRow(
                selectedTabIndex = uiState.loginMethod.ordinal,
                modifier = Modifier.fillMaxWidth()
            ) {
                Tab(
                    selected = uiState.loginMethod == LoginMethod.WEBVIEW,
                    onClick = { viewModel.handleIntent(LoginUiIntent.SwitchLoginMethod(LoginMethod.WEBVIEW)) },
                    text = { Text("网页登录") }
                )
                Tab(
                    selected = uiState.loginMethod == LoginMethod.COOKIE,
                    onClick = { viewModel.handleIntent(LoginUiIntent.SwitchLoginMethod(LoginMethod.COOKIE)) },
                    text = { Text("Cookie登录") }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 登录内容
            when (uiState.loginMethod) {
                LoginMethod.WEBVIEW -> WebViewLoginContent(
                    onLoginSuccess = { cookies ->
                        viewModel.handleIntent(LoginUiIntent.WebViewLoginSuccess(cookies))
                    },
                    onError = { error ->
                        viewModel.handleIntent(LoginUiIntent.WebViewLoginError(error))
                    }
                )
                LoginMethod.COOKIE -> CookieLoginContent(
                    isLoading = uiState.isLoading,
                    onLogin = { bduss, sToken ->
                        viewModel.handleIntent(LoginUiIntent.CookieLogin(bduss, sToken))
                    }
                )
            }

            // 错误提示
            if (uiState.error != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = uiState.error ?: "",
                    style = TiebaTypography.body2,
                    color = TiebaColors.Error
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 提示信息
            Text(
                text = "登录即表示同意《用户协议》和《隐私政策》",
                style = TiebaTypography.caption,
                color = MiuixTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * WebView登录内容
 */
@Composable
private fun WebViewLoginContent(
    onLoginSuccess: (Map<String, String>) -> Unit,
    onError: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "点击下方按钮打开百度登录页面",
            style = TiebaTypography.body2,
            color = MiuixTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // TODO: 打开WebView登录页面
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("打开登录页面")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "登录成功后会自动获取Cookie",
            style = TiebaTypography.caption,
            color = MiuixTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * Cookie登录内容
 */
@Composable
private fun CookieLoginContent(
    isLoading: Boolean,
    onLogin: (String, String) -> Unit
) {
    var bduss by remember { mutableStateOf("") }
    var sToken by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "请输入百度贴吧的Cookie信息",
            style = TiebaTypography.body2,
            color = MiuixTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        // BDUSS输入框
        TextField(
            value = bduss,
            onValueChange = { bduss = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("BDUSS") },
            placeholder = { Text("请输入BDUSS") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // STOKEN输入框
        TextField(
            value = sToken,
            onValueChange = { sToken = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("STOKEN（可选）") },
            placeholder = { Text("请输入STOKEN") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 帮助信息
        Text(
            text = "如何获取Cookie？",
            style = TiebaTypography.link,
            color = TiebaColors.Primary,
            modifier = Modifier.clickable {
                // TODO: 显示帮助信息
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 登录按钮
        Button(
            onClick = { onLogin(bduss, sToken) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading && bduss.isNotBlank()
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text("登录")
            }
        }
    }
}
