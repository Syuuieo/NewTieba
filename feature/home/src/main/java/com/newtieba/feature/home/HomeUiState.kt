package com.newtieba.feature.home

import com.newtieba.domain.model.Forum
import com.newtieba.domain.model.Thread

/**
 * 首页UI状态
 */
data class HomeUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val forums: List<Forum> = emptyList(),
    val threads: List<Thread> = emptyList(),
    val currentTab: HomeTab = HomeTab.RECOMMEND,
    val hasMore: Boolean = true,
    val currentPage: Int = 1
)

/**
 * 首页Tab
 */
enum class HomeTab {
    RECOMMEND,  // 推荐
    FORUM,      // 关注的吧
    HOT         // 热榜
}

/**
 * 推荐帖子UI状态
 */
data class RecommendUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val threads: List<Thread> = emptyList(),
    val hasMore: Boolean = true,
    val currentPage: Int = 1
)

/**
 * 关注的吧UI状态
 */
data class ForumUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val forums: List<Forum> = emptyList(),
    val hasMore: Boolean = true,
    val currentPage: Int = 1
)

/**
 * 热榜UI状态
 */
data class HotUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val threads: List<Thread> = emptyList(),
    val hasMore: Boolean = true,
    val currentPage: Int = 1
)
