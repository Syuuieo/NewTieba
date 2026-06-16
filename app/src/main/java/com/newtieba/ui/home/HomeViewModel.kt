package com.newtieba.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newtieba.data.model.Thread
import com.newtieba.data.repository.ForumRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val threads: List<Thread> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null,
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val forumRepository: ForumRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHomeData()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true, error = null)
            loadHomeData()
        }
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val result = forumRepository.getThreads(forumName = "首页动态")
                result.onSuccess { threads ->
                    _uiState.value = _uiState.value.copy(
                        threads = threads,
                        isLoading = false,
                        isRefreshing = false,
                    )
                }.onFailure { e ->
                    // API 调用失败，回退到示例数据
                    _uiState.value = _uiState.value.copy(
                        threads = generateSampleThreads(),
                        isLoading = false,
                        isRefreshing = false,
                        error = e.message,
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    threads = generateSampleThreads(),
                    isLoading = false,
                    isRefreshing = false,
                    error = e.message,
                )
            }
        }
    }

    private fun generateSampleThreads(): List<Thread> {
        return listOf(
            Thread(
                tid = 1, title = "2026 年最值得期待的 Android 新特性",
                author = com.newtieba.data.model.TiebaUser(1001, "科技达人", "tb.1.xxx1"),
                replyNum = 234, agreeNum = 1890,
                lastTime = System.currentTimeMillis() - 3600_000,
                abstract = "随着 Android 17 的发布，带来了众多令人激动的新特性...",
                images = listOf("https://via.placeholder.com/400x225"),
            ),
            Thread(
                tid = 2, title = "Jetpack Compose 性能优化实战总结",
                author = com.newtieba.data.model.TiebaUser(1002, "Compose开发者", "tb.1.xxx2"),
                replyNum = 89, agreeNum = 567,
                lastTime = System.currentTimeMillis() - 7200_000,
            ),
            Thread(
                tid = 3, title = "Miuix 组件库使用心得分享",
                author = com.newtieba.data.model.TiebaUser(1003, "UI工程师", "tb.1.xxx3"),
                replyNum = 45, agreeNum = 321,
                lastTime = System.currentTimeMillis() - 14400_000,
                images = listOf("https://via.placeholder.com/400x225", "https://via.placeholder.com/400x225"),
            ),
        )
    }
}
