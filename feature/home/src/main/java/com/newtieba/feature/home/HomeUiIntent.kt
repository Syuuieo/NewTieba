package com.newtieba.feature.home

import com.newtieba.domain.model.Forum

/**
 * 首页UI意图
 */
sealed interface HomeUiIntent {
    /**
     * 刷新数据
     */
    data object Refresh : HomeUiIntent

    /**
     * 加载更多
     */
    data object LoadMore : HomeUiIntent

    /**
     * 切换Tab
     */
    data class SwitchTab(val tab: HomeTab) : HomeUiIntent

    /**
     * 关注吧
     */
    data class LikeForum(val forumId: Long, val forumName: String) : HomeUiIntent

    /**
     * 取关吧
     */
    data class UnlikeForum(val forumId: Long, val forumName: String) : HomeUiIntent

    /**
     * 签到
     */
    data class SignForum(val forumId: Long, val forumName: String) : HomeUiIntent

    /**
     * 置顶吧
     */
    data class TopForum(val forum: Forum) : HomeUiIntent

    /**
     * 取消置顶
     */
    data class UntopForum(val forumId: Long) : HomeUiIntent
}

/**
 * 推荐帖子意图
 */
sealed interface RecommendUiIntent {
    data object Refresh : RecommendUiIntent
    data object LoadMore : RecommendUiIntent
}

/**
 * 关注的吧意图
 */
sealed interface ForumUiIntent {
    data object Refresh : ForumUiIntent
    data object LoadMore : ForumUiIntent
    data class LikeForum(val forumId: Long, val forumName: String) : ForumUiIntent
    data class UnlikeForum(val forumId: Long, val forumName: String) : ForumUiIntent
    data class SignForum(val forumId: Long, val forumName: String) : ForumUiIntent
}

/**
 * 热榜意图
 */
sealed interface HotUiIntent {
    data object Refresh : HotUiIntent
    data object LoadMore : HotUiIntent
}
