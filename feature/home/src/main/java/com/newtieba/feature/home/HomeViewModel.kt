package com.newtieba.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newtieba.domain.model.Forum
import com.newtieba.domain.model.Thread
import com.newtieba.domain.usecase.forum.GetForumListUseCase
import com.newtieba.domain.usecase.forum.SignForumUseCase
import com.newtieba.domain.usecase.thread.GetThreadsUseCase
import com.newtieba.common.model.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 首页ViewModel
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getThreadsUseCase: GetThreadsUseCase,
    private val getForumListUseCase: GetForumListUseCase,
    private val signForumUseCase: SignForumUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _recommendState = MutableStateFlow(RecommendUiState())
    val recommendState: StateFlow<RecommendUiState> = _recommendState.asStateFlow()

    private val _forumState = MutableStateFlow(ForumUiState())
    val forumState: StateFlow<ForumUiState> = _forumState.asStateFlow()

    private val _hotState = MutableStateFlow(HotUiState())
    val hotState: StateFlow<HotUiState> = _hotState.asStateFlow()

    init {
        loadRecommendThreads()
    }

    /**
     * 处理意图
     */
    fun handleIntent(intent: HomeUiIntent) {
        when (intent) {
            is HomeUiIntent.Refresh -> refresh()
            is HomeUiIntent.LoadMore -> loadMore()
            is HomeUiIntent.SwitchTab -> switchTab(intent.tab)
            is HomeUiIntent.LikeForum -> likeForum(intent.forumId, intent.forumName)
            is HomeUiIntent.UnlikeForum -> unlikeForum(intent.forumId, intent.forumName)
            is HomeUiIntent.SignForum -> signForum(intent.forumId, intent.forumName)
            is HomeUiIntent.TopForum -> topForum(intent.forum)
            is HomeUiIntent.UntopForum -> untopForum(intent.forumId)
        }
    }

    /**
     * 刷新数据
     */
    private fun refresh() {
        when (_uiState.value.currentTab) {
            HomeTab.RECOMMEND -> refreshRecommend()
            HomeTab.FORUM -> refreshForum()
            HomeTab.HOT -> refreshHot()
        }
    }

    /**
     * 加载更多
     */
    private fun loadMore() {
        when (_uiState.value.currentTab) {
            HomeTab.RECOMMEND -> loadMoreRecommend()
            HomeTab.FORUM -> loadMoreForum()
            HomeTab.HOT -> loadMoreHot()
        }
    }

    /**
     * 切换Tab
     */
    private fun switchTab(tab: HomeTab) {
        _uiState.update { it.copy(currentTab = tab) }
        when (tab) {
            HomeTab.RECOMMEND -> if (_recommendState.value.threads.isEmpty()) loadRecommendThreads()
            HomeTab.FORUM -> if (_forumState.value.forums.isEmpty()) loadForums()
            HomeTab.HOT -> if (_hotState.value.threads.isEmpty()) loadHotThreads()
        }
    }

    /**
     * 加载推荐帖子
     */
    private fun loadRecommendThreads() {
        viewModelScope.launch {
            _recommendState.update { it.copy(isLoading = true, error = null) }
            getThreadsUseCase(page = 1).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _recommendState.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        _recommendState.update {
                            it.copy(
                                isLoading = false,
                                threads = result.data ?: emptyList(),
                                currentPage = 1,
                                hasMore = (result.data?.size ?: 0) >= 30
                            )
                        }
                    }
                    is Resource.Error -> {
                        _recommendState.update {
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
     * 刷新推荐
     */
    private fun refreshRecommend() {
        viewModelScope.launch {
            _recommendState.update { it.copy(isRefreshing = true, error = null) }
            getThreadsUseCase(page = 1).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _recommendState.update { it.copy(isRefreshing = true) }
                    }
                    is Resource.Success -> {
                        _recommendState.update {
                            it.copy(
                                isRefreshing = false,
                                threads = result.data ?: emptyList(),
                                currentPage = 1,
                                hasMore = (result.data?.size ?: 0) >= 30
                            )
                        }
                    }
                    is Resource.Error -> {
                        _recommendState.update {
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
     * 加载更多推荐
     */
    private fun loadMoreRecommend() {
        val currentState = _recommendState.value
        if (currentState.isLoading || !currentState.hasMore) return

        viewModelScope.launch {
            val nextPage = currentState.currentPage + 1
            getThreadsUseCase(page = nextPage).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _recommendState.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        val newThreads = result.data ?: emptyList()
                        _recommendState.update {
                            it.copy(
                                isLoading = false,
                                threads = it.threads + newThreads,
                                currentPage = nextPage,
                                hasMore = newThreads.size >= 30
                            )
                        }
                    }
                    is Resource.Error -> {
                        _recommendState.update {
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
     * 加载关注的吧
     */
    private fun loadForums() {
        viewModelScope.launch {
            _forumState.update { it.copy(isLoading = true, error = null) }
            getForumListUseCase(page = 1).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _forumState.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        _forumState.update {
                            it.copy(
                                isLoading = false,
                                forums = result.data ?: emptyList(),
                                currentPage = 1,
                                hasMore = (result.data?.size ?: 0) >= 30
                            )
                        }
                    }
                    is Resource.Error -> {
                        _forumState.update {
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
     * 刷新关注的吧
     */
    private fun refreshForum() {
        viewModelScope.launch {
            _forumState.update { it.copy(isRefreshing = true, error = null) }
            getForumListUseCase(page = 1).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _forumState.update { it.copy(isRefreshing = true) }
                    }
                    is Resource.Success -> {
                        _forumState.update {
                            it.copy(
                                isRefreshing = false,
                                forums = result.data ?: emptyList(),
                                currentPage = 1,
                                hasMore = (result.data?.size ?: 0) >= 30
                            )
                        }
                    }
                    is Resource.Error -> {
                        _forumState.update {
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
     * 加载更多关注的吧
     */
    private fun loadMoreForum() {
        val currentState = _forumState.value
        if (currentState.isLoading || !currentState.hasMore) return

        viewModelScope.launch {
            val nextPage = currentState.currentPage + 1
            getForumListUseCase(page = nextPage).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _forumState.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        val newForums = result.data ?: emptyList()
                        _forumState.update {
                            it.copy(
                                isLoading = false,
                                forums = it.forums + newForums,
                                currentPage = nextPage,
                                hasMore = newForums.size >= 30
                            )
                        }
                    }
                    is Resource.Error -> {
                        _forumState.update {
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
     * 加载热榜
     */
    private fun loadHotThreads() {
        // TODO: 实现热榜加载
    }

    /**
     * 刷新热榜
     */
    private fun refreshHot() {
        // TODO: 实现热榜刷新
    }

    /**
     * 加载更多热榜
     */
    private fun loadMoreHot() {
        // TODO: 实现热榜加载更多
    }

    /**
     * 关注吧
     */
    private fun likeForum(forumId: Long, forumName: String) {
        viewModelScope.launch {
            // TODO: 实现关注吧
        }
    }

    /**
     * 取关吧
     */
    private fun unlikeForum(forumId: Long, forumName: String) {
        viewModelScope.launch {
            // TODO: 实现取关吧
        }
    }

    /**
     * 签到
     */
    private fun signForum(forumId: Long, forumName: String) {
        viewModelScope.launch {
            signForumUseCase(forumId, forumName).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        // 更新签到状态
                    }
                    is Resource.Error -> {
                        // 显示错误
                    }
                    is Resource.Loading -> {
                        // 显示加载
                    }
                }
            }
        }
    }

    /**
     * 置顶吧
     */
    private fun topForum(forum: Forum) {
        // TODO: 实现置顶吧
    }

    /**
     * 取消置顶
     */
    private fun untopForum(forumId: Long) {
        // TODO: 实现取消置顶
    }
}
