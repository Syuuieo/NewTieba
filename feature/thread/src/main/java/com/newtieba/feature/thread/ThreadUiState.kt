package com.newtieba.feature.thread

import com.newtieba.domain.model.Comment
import com.newtieba.domain.model.Forum
import com.newtieba.domain.model.Post
import com.newtieba.domain.model.Thread

/**
 * 帖子详情UI状态
 */
data class ThreadUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val thread: Thread? = null,
    val posts: List<Post> = emptyList(),
    val forum: Forum? = null,
    val currentPage: Int = 1,
    val totalPages: Int = 0,
    val hasMore: Boolean = true,
    val isLiked: Boolean = false,
    val isCollected: Boolean = false,
    val isLzOnly: Boolean = false,
    val sortOrder: SortOrder = SortOrder.ASC
)

/**
 * 评论状态
 */
data class CommentsUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val comments: List<Comment> = emptyList(),
    val postId: Long = 0,
    val hasMore: Boolean = true,
    val currentPage: Int = 1
)

/**
 * 排序方式
 */
enum class SortOrder {
    ASC,    // 正序
    DESC    // 倒序
}

/**
 * 回复输入状态
 */
data class ReplyInputState(
    val isVisible: Boolean = false,
    val content: String = "",
    val replyToPostId: Long = 0,
    val replyToUserName: String = "",
    val isSubmitting: Boolean = false,
    val error: String? = null
)
