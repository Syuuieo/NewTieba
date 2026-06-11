package com.newtieba.feature.thread

/**
 * 帖子详情UI意图
 */
sealed interface ThreadUiIntent {
    /**
     * 加载帖子详情
     */
    data class LoadThread(val threadId: Long) : ThreadUiIntent

    /**
     * 刷新
     */
    data object Refresh : ThreadUiIntent

    /**
     * 加载更多回复
     */
    data object LoadMorePosts : ThreadUiIntent

    /**
     * 切换只看楼主
     */
    data class ToggleLzOnly(val enabled: Boolean) : ThreadUiIntent

    /**
     * 切换排序方式
     */
    data class ChangeSortOrder(val order: SortOrder) : ThreadUiIntent

    /**
     * 点赞帖子
     */
    data class LikeThread(val threadId: Long) : ThreadUiIntent

    /**
     * 取消点赞
     */
    data class UnlikeThread(val threadId: Long) : ThreadUiIntent

    /**
     * 收藏帖子
     */
    data class CollectThread(val threadId: Long) : ThreadUiIntent

    /**
     * 取消收藏
     */
    data class UncollectThread(val threadId: Long) : ThreadUiIntent

    /**
     * 点赞回复
     */
    data class LikePost(val threadId: Long, val postId: Long) : ThreadUiIntent

    /**
     * 取消点赞回复
     */
    data class UnlikePost(val threadId: Long, val postId: Long) : ThreadUiIntent

    /**
     * 显示回复输入框
     */
    data class ShowReplyInput(val postId: Long, val userName: String) : ThreadUiIntent

    /**
     * 隐藏回复输入框
     */
    data object HideReplyInput : ThreadUiIntent

    /**
     * 更新回复内容
     */
    data class UpdateReplyContent(val content: String) : ThreadUiIntent

    /**
     * 提交回复
     */
    data object SubmitReply : ThreadUiIntent

    /**
     * 加载评论
     */
    data class LoadComments(val postId: Long) : ThreadUiIntent

    /**
     * 加载更多评论
     */
    data object LoadMoreComments : ThreadUiIntent

    /**
     * 删除帖子
     */
    data class DeleteThread(val threadId: Long) : ThreadUiIntent
}
