package com.newtieba.ui.forum

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newtieba.data.model.Forum
import com.newtieba.data.model.Thread
import com.newtieba.data.model.TiebaUser
import com.newtieba.data.repository.ForumRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ForumUiState(
    val forumName: String = "",
    val forumInfo: Forum? = null,
    val threads: List<Thread> = emptyList(),
    val sortType: Int = 5,
    val isGood: Boolean = false,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isFollowed: Boolean = false,
    val error: String? = null,
)

@HiltViewModel
class ForumViewModel @Inject constructor(
    private val forumRepository: ForumRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ForumUiState())
    val uiState: StateFlow<ForumUiState> = _uiState.asStateFlow()

    fun init(name: String) {
        if (_uiState.value.forumName == name) return
        _uiState.value = _uiState.value.copy(forumName = name)
        loadForumData()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true)
            loadForumData()
        }
    }

    fun toggleSort() {
        val newSort = if (_uiState.value.sortType == 5) 1 else 5
        _uiState.value = _uiState.value.copy(sortType = newSort)
        loadForumData()
    }

    fun toggleGood() {
        _uiState.value = _uiState.value.copy(isGood = !_uiState.value.isGood)
        loadForumData()
    }

    private fun loadForumData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val result = forumRepository.getThreads(
                    forumName = _uiState.value.forumName,
                    isGood = _uiState.value.isGood,
                )
                result.onSuccess { threads ->
                    _uiState.value = _uiState.value.copy(
                        threads = threads,
                        isLoading = false,
                        isRefreshing = false,
                        error = null,
                    )
                }.onFailure { e ->
                    // 回退示例数据
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

    fun toggleFollow() {
        val newState = !_uiState.value.isFollowed
        _uiState.value = _uiState.value.copy(isFollowed = newState)
        viewModelScope.launch {
            if (newState) {
                forumRepository.followForum(_uiState.value.forumInfo?.fid ?: 0)
            } else {
                forumRepository.unfollowForum(_uiState.value.forumInfo?.fid ?: 0)
            }
        }
    }

    private fun generateSampleThreads(): List<Thread> {
        return List(10) { i ->
            Thread(
                tid = 1001L + i,
                title = "【讨论】${_uiState.value.forumName} 最新话题 #${i + 1}",
                author = TiebaUser(4001L + i, "吧友${i + 1}", "tb.1.xxx${100 + i}"),
                replyNum = (10..500).random(),
                agreeNum = (5..200).random(),
                lastTime = System.currentTimeMillis() - (i * 3600_000L),
                abstract = if (i % 2 == 0) "这是第 ${i + 1} 个帖子的摘要内容" else "",
                isGood = i < 2,
            )
        }
    }
}
