package com.newtieba.ui.hot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newtieba.data.model.Thread
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HotUiState(
    val tabs: List<String> = listOf("全站", "数码", "游戏", "动漫", "影视"),
    val selectedTab: Int = 0,
    val threads: List<Thread> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

@HiltViewModel
class HotViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(HotUiState())
    val uiState: StateFlow<HotUiState> = _uiState.asStateFlow()

    init {
        loadHotData()
    }

    fun selectTab(index: Int) {
        _uiState.value = _uiState.value.copy(selectedTab = index)
        loadHotData()
    }

    private fun loadHotData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                // TODO: 接入真实 API 获取各 Tab 的热帖数据
                val sampleData = generateSampleThreads()
                _uiState.value = _uiState.value.copy(
                    threads = sampleData,
                    isLoading = false,
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message,
                )
            }
        }
    }

    private fun generateSampleThreads(): List<Thread> {
        return listOf(
            Thread(
                tid = 101, title = "【热】2026 旗舰手机横向评测",
                author = com.newtieba.data.model.TiebaUser(
                    uid = 2001, name = "数码评测君", portrait = "tb.1.xxx4",
                ),
                replyNum = 1567, agreeNum = 8900,
                lastTime = System.currentTimeMillis() - 1800_000,
                abstract = "今年各大厂商的旗舰机型都出来了，我们来做一个全面的对比评测...",
            ),
            Thread(
                tid = 102, title = "热门讨论：新发布的游戏引擎有多强？",
                author = com.newtieba.data.model.TiebaUser(
                    uid = 2002, name = "游戏玩家", portrait = "tb.1.xxx5",
                ),
                replyNum = 892, agreeNum = 4500,
                lastTime = System.currentTimeMillis() - 3600_000,
            ),
            Thread(
                tid = 103, title = "本周新番推荐（2026夏）",
                author = com.newtieba.data.model.TiebaUser(
                    uid = 2003, name = "动漫爱好者", portrait = "tb.1.xxx6",
                ),
                replyNum = 345, agreeNum = 2100,
                lastTime = System.currentTimeMillis() - 5400_000,
                images = listOf("https://via.placeholder.com/400x225"),
            ),
        )
    }
}
