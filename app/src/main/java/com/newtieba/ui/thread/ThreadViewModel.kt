package com.newtieba.ui.thread

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newtieba.data.model.ContentPiece
import com.newtieba.data.model.ContentType
import com.newtieba.data.model.Post
import com.newtieba.data.model.Thread
import com.newtieba.data.model.TiebaUser
import com.newtieba.data.repository.ThreadRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ThreadUiState(
    val tid: Long = 0,
    val thread: Thread? = null,
    val posts: List<Post> = emptyList(),
    val currentPage: Int = 1,
    val hasMore: Boolean = true,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null,
)

@HiltViewModel
class ThreadViewModel @Inject constructor(
    private val threadRepository: ThreadRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ThreadUiState())
    val uiState: StateFlow<ThreadUiState> = _uiState.asStateFlow()

    fun init(tid: Long) {
        if (_uiState.value.tid == tid) return
        _uiState.value = _uiState.value.copy(tid = tid)
        loadPosts()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true, currentPage = 1)
            loadPosts()
        }
    }

    fun loadMore() {
        if (!_uiState.value.hasMore || _uiState.value.isLoading) return
        viewModelScope.launch {
            val nextPage = _uiState.value.currentPage + 1
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val result = threadRepository.getPosts(_uiState.value.tid, nextPage)
                result.onSuccess { morePosts ->
                    _uiState.value = _uiState.value.copy(
                        posts = _uiState.value.posts + morePosts,
                        currentPage = nextPage,
                        hasMore = morePosts.isNotEmpty(),
                        isLoading = false,
                    )
                }.onFailure { e ->
                    _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    private fun loadPosts() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val result = threadRepository.getPosts(_uiState.value.tid)
                result.onSuccess { posts ->
                    val sampleThread = Thread(
                        tid = _uiState.value.tid,
                        title = "帖子 #${_uiState.value.tid}",
                        author = posts.firstOrNull()?.author
                            ?: TiebaUser(5001, "楼主", "tb.1.xxx_lz"),
                        replyNum = posts.size,
                        agreeNum = posts.sumOf { it.agreeNum },
                        createTime = posts.firstOrNull()?.time ?: System.currentTimeMillis(),
                        lastTime = posts.lastOrNull()?.time ?: System.currentTimeMillis(),
                    )
                    _uiState.value = _uiState.value.copy(
                        thread = sampleThread,
                        posts = posts,
                        isLoading = false,
                        isRefreshing = false,
                        hasMore = posts.isNotEmpty(),
                    )
                }.onFailure { e ->
                    // 回退示例数据
                    _uiState.value = _uiState.value.copy(
                        posts = generateSamplePosts(),
                        isLoading = false,
                        isRefreshing = false,
                        error = e.message,
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    posts = generateSamplePosts(),
                    isLoading = false,
                    isRefreshing = false,
                    error = e.message,
                )
            }
        }
    }

    private fun generateSamplePosts(): List<Post> {
        return listOf(
            Post(50001, 1, TiebaUser(5001, "楼主", "tb.1.xxx_lz"),
                content = listOf(ContentPiece(ContentType.TEXT, "这是楼主帖的内容。")),
                time = System.currentTimeMillis() - 86400_000, agreeNum = 128, subPostCount = 3,
            ),
        ) + List(10) { i ->
            val floor = i + 2
            Post(
                pid = 50001L + floor, floor = floor,
                author = TiebaUser(5000L + floor, "吧友$floor", "tb.1.xxx_$floor"),
                content = listOf(ContentPiece(ContentType.TEXT, "回复内容 #$floor")),
                time = System.currentTimeMillis() - (floor * 3600_000L),
                agreeNum = (0..50).random(),
            )
        }
    }
}
