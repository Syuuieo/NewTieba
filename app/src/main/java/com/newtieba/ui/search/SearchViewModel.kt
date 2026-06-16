package com.newtieba.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newtieba.data.model.Forum
import com.newtieba.data.model.Thread
import com.newtieba.data.model.TiebaUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchUiState(
    val query: String = "",
    val selectedTab: Int = 0,
    val threads: List<Thread> = emptyList(),
    val forums: List<Forum> = emptyList(),
    val users: List<TiebaUser> = emptyList(),
    val hotQueries: List<String> = listOf("Android 17", "Compose", "Miuix", "Kotlin", "贴吧开发"),
    val history: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

@HiltViewModel
class SearchViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    fun updateQuery(query: String) {
        _uiState.value = _uiState.value.copy(query = query)
    }

    fun search() {
        val query = _uiState.value.query
        if (query.isBlank()) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                // TODO: 接入真实搜索 API (TiebaWebApiService.search)
                addToHistory(query)
                val sampleThreads = listOf(
                    Thread(
                        tid = 201, title = "搜索结果：$query 相关讨论",
                        author = TiebaUser(uid = 3001, name = "搜索结果", portrait = "tb.1.xxx"),
                        replyNum = 50, agreeNum = 200,
                        lastTime = System.currentTimeMillis(),
                        abstract = "这是关于「$query」的搜索结果占位",
                    )
                )
                _uiState.value = _uiState.value.copy(
                    threads = sampleThreads,
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

    fun selectTab(index: Int) {
        _uiState.value = _uiState.value.copy(selectedTab = index)
    }

    fun clearHistory() {
        _uiState.value = _uiState.value.copy(history = emptyList())
    }

    private fun addToHistory(query: String) {
        val history = _uiState.value.history.toMutableList()
        history.remove(query)
        history.add(0, query)
        _uiState.value = _uiState.value.copy(history = history.take(10))
    }
}
