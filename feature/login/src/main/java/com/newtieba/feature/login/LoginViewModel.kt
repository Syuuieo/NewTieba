package com.newtieba.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newtieba.common.model.Resource
import com.newtieba.domain.usecase.auth.LoginUseCase
import com.newtieba.domain.usecase.auth.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 登录ViewModel
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _webViewState = MutableStateFlow(WebViewLoginState())
    val webViewState: StateFlow<WebViewLoginState> = _webViewState.asStateFlow()

    private val _cookieState = MutableStateFlow(CookieLoginState())
    val cookieState: StateFlow<CookieLoginState> = _cookieState.asStateFlow()

    init {
        checkLoginStatus()
    }

    /**
     * 处理意图
     */
    fun handleIntent(intent: LoginUiIntent) {
        when (intent) {
            is LoginUiIntent.SwitchLoginMethod -> switchLoginMethod(intent.method)
            is LoginUiIntent.WebViewLoginSuccess -> webViewLoginSuccess(intent.cookies)
            is LoginUiIntent.WebViewLoginError -> webViewLoginError(intent.error)
            is LoginUiIntent.CookieLogin -> cookieLogin(intent.bduss, intent.sToken)
            is LoginUiIntent.UpdateBduss -> updateBduss(intent.bduss)
            is LoginUiIntent.UpdateSToken -> updateSToken(intent.sToken)
            is LoginUiIntent.Logout -> logout()
            is LoginUiIntent.ClearError -> clearError()
        }
    }

    /**
     * 检查登录状态
     */
    private fun checkLoginStatus() {
        val isLoggedIn = loginUseCase.isLoggedIn()
        _uiState.update { it.copy(isLoggedIn = isLoggedIn) }

        if (isLoggedIn) {
            loadCurrentUser()
        }
    }

    /**
     * 加载当前用户
     */
    private fun loadCurrentUser() {
        viewModelScope.launch {
            loginUseCase.getCurrentUser().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        val user = result.data
                        _uiState.update {
                            it.copy(
                                userName = user?.name ?: "",
                                userAvatar = user?.avatarUrl ?: ""
                            )
                        }
                    }
                    is Resource.Error -> {
                        // 加载用户信息失败
                    }
                    is Resource.Loading -> {
                        // 加载中
                    }
                }
            }
        }
    }

    /**
     * 切换登录方式
     */
    private fun switchLoginMethod(method: LoginMethod) {
        _uiState.update { it.copy(loginMethod = method, error = null) }
    }

    /**
     * WebView登录成功
     */
    private fun webViewLoginSuccess(cookies: Map<String, String>) {
        val bduss = cookies["BDUSS"] ?: ""
        val sToken = cookies["STOKEN"] ?: ""

        if (bduss.isBlank()) {
            _uiState.update { it.copy(error = "登录失败：未获取到BDUSS") }
            return
        }

        _uiState.update { it.copy(bduss = bduss, sToken = sToken) }
        login(bduss, sToken)
    }

    /**
     * WebView登录失败
     */
    private fun webViewLoginError(error: String) {
        _uiState.update { it.copy(error = error) }
    }

    /**
     * Cookie登录
     */
    private fun cookieLogin(bduss: String, sToken: String) {
        if (bduss.isBlank()) {
            _uiState.update { it.copy(error = "请输入BDUSS") }
            return
        }

        _uiState.update { it.copy(bduss = bduss, sToken = sToken) }
        login(bduss, sToken)
    }

    /**
     * 登录
     */
    private fun login(bduss: String, sToken: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            loginUseCase(bduss, sToken).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isLoggedIn = true,
                                userName = result.data?.name ?: "",
                                userAvatar = result.data?.avatarUrl ?: "",
                                error = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.message ?: "登录失败"
                            )
                        }
                    }
                }
            }
        }
    }

    /**
     * 退出登录
     */
    private fun logout() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            logoutUseCase().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isLoggedIn = false,
                                userName = "",
                                userAvatar = "",
                                bduss = "",
                                sToken = ""
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.message ?: "退出失败"
                            )
                        }
                    }
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                }
            }
        }
    }

    /**
     * 更新BDUSS
     */
    private fun updateBduss(bduss: String) {
        _cookieState.update { it.copy(bduss = bduss) }
    }

    /**
     * 更新STOKEN
     */
    private fun updateSToken(sToken: String) {
        _cookieState.update { it.copy(sToken = sToken) }
    }

    /**
     * 清除错误
     */
    private fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
