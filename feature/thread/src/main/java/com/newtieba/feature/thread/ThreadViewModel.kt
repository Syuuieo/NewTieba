package com.newtieba.feature.thread

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newtieba.common.model.Resource
import com.newtieba.domain.usecase.thread.GetThreadDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 帖子详情ViewModel
 */
@HiltViewModel
class ThreadViewModel @Inject constructor(
    private val getThreadDetailUseCase: GetThreadDetailUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val threadId: Long = savedStateHandle.get<Long>("threadId") ?: 0L

    private val _uiState = MutableStateFlow(ThreadUiState())
    val uiState: StateFlow<ThreadUiState> = _uiState.asStateFlow()

    private val _commentsState = MutableStateFlow(CommentsUiState())
    val commentsState: StateFlow<CommentsUiState> = _commentsState.asStateFlow()

    private val _replyInputState = MutableStateFlow(ReplyInputState())
    val replyInputState: StateFlow<ReplyInputState> = _replyInputState.asStateFlow()

    init {
        if (threadId > 0) {
            loadThread(threadId)
        }
    }

    /**
     * 处理意图
     */
    fun handleIntent(intent: ThreadUiIntent) {
        when (intent) {
            is ThreadUiIntent.LoadThread -> loadThread(intent.threadId)
            is ThreadUiIntent.Refresh -> refresh()
            is ThreadUiIntent.LoadMorePosts -> loadMorePosts()
            is ThreadUiIntent.ToggleLzOnly -> toggleLzOnly(intent.enabled)
            is ThreadUiIntent.ChangeSortOrder -> changeSortOrder(intent.order)
            is ThreadUiIntent.LikeThread -> likeThread(intent.threadId)
            is ThreadUiIntent.UnlikeThread -> unlikeThread(intent.threadId)
            is ThreadUiIntent.CollectThread -> collectThread(intent.threadId)
            is ThreadUiIntent.UncollectThread -> uncollectThread(intent.threadId)
            is ThreadUiIntent.LikePost -> likePost(intent.threadId, intent.postId)
            is ThreadUiIntent.UnlikePost -> unlikePost(intent.threadId, intent.postId)
            is ThreadUiIntent.ShowReplyInput -> showReplyInput(intent.postId, intent.userName)
            is ThreadUiIntent.HideReplyInput -> hideReplyInput()
            is ThreadUiIntent.UpdateReplyContent -> updateReplyContent(intent.content)
            is ThreadUiIntent.SubmitReply -> submitReply()
            is ThreadUiIntent.LoadComments -> loadComments(intent.postId)
            is ThreadUiIntent.LoadMoreComments -> loadMoreComments()
            is ThreadUiIntent.DeleteThread -> deleteThread(intent.threadId)
        }
    }

    /**
     * 加载帖子详情
     */
    private fun loadThread(threadId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            getThreadDetailUseCase(
                threadId = threadId,
                page = 1,
                seeLz = _uiState.value.isLzOnly
            ).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        val data = result.data
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                thread = data?.thread,
                                posts = data?.posts ?: emptyList(),
                                forum = data?.forum,
                                currentPage = data?.currentPage ?: 1,
                                totalPages = data?.totalPages ?: 0,
                                hasMore = data?.hasMore ?: false,
                                isLiked = data?.thread?.isLiked ?: false,
                                isCollected = data?.thread?.isCollected ?: false
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.message ?: "加载失败"
                            )
                        }
                    }
                }
            }
        }
    }

    /**
     * 刷新
     */
    private fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true, error = null) }
            getThreadDetailUseCase(
                threadId = threadId,
                page = 1,
                seeLz = _uiState.value.isLzOnly
            ).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isRefreshing = true) }
                    }
                    is Resource.Success -> {
                        val data = result.data
                        _uiState.update {
                            it.copy(
                                isRefreshing = false,
                                thread = data?.thread,
                                posts = data?.posts ?: emptyList(),
                                forum = data?.forum,
                                currentPage = data?.currentPage ?: 1,
                                totalPages = data?.totalPages ?: 0,
                                hasMore = data?.hasMore ?: false,
                                isLiked = data?.thread?.isLiked ?: false,
                                isCollected = data?.thread?.isCollected ?: false
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isRefreshing = false,
                                error = result.message ?: "刷新失败"
                            )
                        }
                    }
                }
            }
        }
    }

    /**
     * 加载更多回复
     */
    private fun loadMorePosts() {
        val currentState = _uiState.value
        if (currentState.isLoading || !currentState.hasMore) return

        viewModelScope.launch {
            val nextPage = currentState.currentPage + 1
            getThreadDetailUseCase(
                threadId = threadId,
                page = nextPage,
                seeLz = currentState.isLzOnly
            ).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        val data = result.data
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                posts = it.posts + (data?.posts ?: emptyList()),
                                currentPage = nextPage,
                                hasMore = data?.hasMore ?: false
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.message ?: "加载失败"
                            )
                        }
                    }
                }
            }
        }
    }

    /**
     * 切换只看楼主
     */
    private fun toggleLzOnly(enabled: Boolean) {
        _uiState.update { it.copy(isLzOnly = enabled) }
        refresh()
    }

    /**
     * 切换排序方式
     */
    private fun changeSortOrder(order: SortOrder) {
        _uiState.update { it.copy(sortOrder = order) }
        refresh()
    }

    /**
     * 点赞帖子
     */
    private fun likeThread(threadId: Long) {
        // TODO: 实现点赞帖子
    }

    /**
     * 取消点赞
     */
    private fun unlikeThread(threadId: Long) {
        // TODO: 实现取消点赞
    }

    /**
     * 收藏帖子
     */
    private fun collectThread(threadId: Long) {
        // TODO: 实现收藏帖子
    }

    /**
     * 取消收藏
     */
    private fun uncollectThread(threadId: Long) {
        // TODO: 实现取消收藏
    }

    /**
     * 点赞回复
     */
    private fun likePost(threadId: Long, postId: Long) {
        // TODO: 实现点赞回复
    }

    /**
     * 取消点赞回复
     */
    private fun unlikePost(threadId: Long, postId: Long) {
        // TODO: 实现取消点赞回复
    }

    /**
     * 显示回复输入框
     */
    private fun showReplyInput(postId: Long, userName: String) {
        _replyInputState.update {
            it.copy(
                isVisible = true,
                replyToPostId = postId,
                replyToUserName = userName
            )
        }
    }

    /**
     * 隐藏回复输入框
     */
    private fun hideReplyInput() {
        _replyInputState.update {
            it.copy(
                isVisible = false,
                content = "",
                replyToPostId = 0,
                replyToUserName = "",
                error = null
            )
        }
    }

    /**
     * 更新回复内容
     */
    private fun updateReplyContent(content: String) {
        _replyInputState.update { it.copy(content = content) }
    }

    /**
     * 提交回复
     */
    private fun submitReply() {
        val replyState = _replyInputState.value
        if (replyState.content.isBlank()) {
            _replyInputState.update { it.copy(error = "请输入回复内容") }
            return
        }

        viewModelScope.launch {
            _replyInputState.update { it.copy(isSubmitting = true, error = null) }
            // TODO: 实现提交回复
            _replyInputState.update {
                it.copy(
                    isSubmitting = false,
                    isVisible = false,
                    content = ""
                )
            }
        }
    }

    /**
     * 加载评论
     */
    private fun loadComments(postId: Long) {
        viewModelScope.launch {
            _commentsState.update { it.copy(isLoading = true, error = null, postId = postId) }
            getThreadDetailUseCase.getComments(
                threadId = threadId,
                postId = postId,
                page = 1
            ).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _commentsState.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        _commentsState.update {
                            it.copy(
                                isLoading = false,
                                comments = result.data ?: emptyList(),
                                currentPage = 1,
                                hasMore = (result.data?.size ?: 0) >= 20
                            )
                        }
                    }
                    is Resource.Error -> {
                        _commentsState.update {
                            it.copy(
                                isLoading = false,
                                error = result.message ?: "加载失败"
                            )
                        }
                    }
                }
            }
        }
    }

    /**
     * 加载更多评论
     */
    private fun loadMoreComments() {
        val currentState = _commentsState.value
        if (currentState.isLoading || !currentState.hasMore) return

        viewModelScope.launch {
            val nextPage = currentState.currentPage + 1
            getThreadDetailUseCase.getComments(
                threadId = threadId,
                postId = currentState.postId,
                page = nextPage
            ).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _commentsState.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        _commentsState.update {
                            it.copy(
                                isLoading = false,
                                comments = it.comments + (result.data ?: emptyList()),
                                currentPage = nextPage,
                                hasMore = (result.data?.size ?: 0) >= 20
                            )
                        }
                    }
                    is Resource.Error -> {
                        _commentsState.update {
                            it.copy(
                                isLoading = false,
                                error = result.message ?: "加载失败"
                            )
                        }
                    }
                }
            }
        }
    }

    /**
     * 删除帖子
     */
    private fun deleteThread(threadId: Long) {
        // TODO: 实现删除帖子
    }
}
