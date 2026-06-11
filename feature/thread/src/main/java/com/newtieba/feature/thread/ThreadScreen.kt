package com.newtieba.feature.thread

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.newtieba.domain.model.Post
import com.newtieba.ui.components.*
import com.newtieba.ui.theme.TiebaTypography
import top.yukonga.miuix.kmp.basic.*
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * 帖子详情Screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThreadScreen(
    viewModel: ThreadViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onUserClick: (Long) -> Unit,
    onForumClick: (Long, String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val replyInputState by viewModel.replyInputState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = uiState.thread?.title ?: "帖子详情",
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(id = android.R.drawable.ic_menu_revert),
                            contentDescription = "返回"
                        )
                    }
                },
                actions = {
                    // 更多操作
                    IconButton(onClick = { /* TODO: 显示菜单 */ }) {
                        Icon(
                            painter = painterResource(id = android.R.drawable.ic_menu_more),
                            contentDescription = "更多"
                        )
                    }
                }
            )
        },
        bottomBar = {
            // 底部操作栏
            BottomActionBar(
                isLiked = uiState.isLiked,
                isCollected = uiState.isCollected,
                replyCount = uiState.thread?.replyCount ?: 0,
                onLikeClick = {
                    if (uiState.isLiked) {
                        viewModel.handleIntent(ThreadUiIntent.UnlikeThread(uiState.thread?.id ?: 0))
                    } else {
                        viewModel.handleIntent(ThreadUiIntent.LikeThread(uiState.thread?.id ?: 0))
                    }
                },
                onCollectClick = {
                    if (uiState.isCollected) {
                        viewModel.handleIntent(ThreadUiIntent.UncollectThread(uiState.thread?.id ?: 0))
                    } else {
                        viewModel.handleIntent(ThreadUiIntent.CollectThread(uiState.thread?.id ?: 0))
                    }
                },
                onReplyClick = {
                    viewModel.handleIntent(ThreadUiIntent.ShowReplyInput(0, ""))
                },
                onShareClick = { /* TODO: 分享 */ }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (uiState.isLoading && uiState.thread == null) {
                LoadingState()
            } else if (uiState.error != null && uiState.thread == null) {
                ErrorState(
                    message = uiState.error ?: "加载失败",
                    onRetry = { viewModel.handleIntent(ThreadUiIntent.Refresh) }
                )
            } else {
                // 帖子内容
                ThreadContent(
                    uiState = uiState,
                    onRefresh = { viewModel.handleIntent(ThreadUiIntent.Refresh) },
                    onLoadMore = { viewModel.handleIntent(ThreadUiIntent.LoadMorePosts) },
                    onUserClick = onUserClick,
                    onForumClick = onForumClick,
                    onLikePost = { postId ->
                        viewModel.handleIntent(ThreadUiIntent.LikePost(uiState.thread?.id ?: 0, postId))
                    },
                    onReplyPost = { postId, userName ->
                        viewModel.handleIntent(ThreadUiIntent.ShowReplyInput(postId, userName))
                    },
                    onLoadComments = { postId ->
                        viewModel.handleIntent(ThreadUiIntent.LoadComments(postId))
                    }
                )
            }

            // 回复输入框
            if (replyInputState.isVisible) {
                ReplyInputOverlay(
                    state = replyInputState,
                    onContentChange = { viewModel.handleIntent(ThreadUiIntent.UpdateReplyContent(it)) },
                    onSubmit = { viewModel.handleIntent(ThreadUiIntent.SubmitReply) },
                    onDismiss = { viewModel.handleIntent(ThreadUiIntent.HideReplyInput) }
                )
            }
        }
    }
}

/**
 * 帖子内容
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ThreadContent(
    uiState: ThreadUiState,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    onUserClick: (Long) -> Unit,
    onForumClick: (Long, String) -> Unit,
    onLikePost: (Long) -> Unit,
    onReplyPost: (Long, String) -> Unit,
    onLoadComments: (Long) -> Unit
) {
    val listState = rememberLazyListState()

    // 监听滚动到底部，加载更多
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo }
            .collect { layoutInfo ->
                val lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                val totalItemsCount = layoutInfo.totalItemsCount
                if (lastVisibleIndex >= totalItemsCount - 5 && !uiState.isLoading && uiState.hasMore) {
                    onLoadMore()
                }
            }
    }

    PullToRefreshBox(
        isRefreshing = uiState.isRefreshing,
        onRefresh = onRefresh
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 帖子头部
            item {
                ThreadHeader(
                    thread = uiState.thread,
                    forum = uiState.forum,
                    onUserClick = onUserClick,
                    onForumClick = onForumClick
                )
            }

            // 帖子正文
            item {
                ThreadBody(
                    content = uiState.thread?.content ?: "",
                    images = uiState.thread?.images ?: emptyList()
                )
            }

            // 操作栏
            item {
                ThreadActions(
                    isLiked = uiState.isLiked,
                    likeCount = uiState.thread?.likeCount ?: 0,
                    replyCount = uiState.thread?.replyCount ?: 0,
                    shareCount = uiState.thread?.shareCount ?: 0,
                    onLikeClick = { onLikePost(uiState.thread?.id ?: 0) }
                )
            }

            // 分割线
            item {
                Divider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = MiuixTheme.colorScheme.outline
                )
            }

            // 回复列表标题
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "回复 (${uiState.thread?.replyCount ?: 0})",
                        style = TiebaTypography.h5,
                        color = MiuixTheme.colorScheme.onSurface
                    )

                    // 只看楼主开关
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "只看楼主",
                            style = TiebaTypography.body2,
                            color = MiuixTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Switch(
                            checked = uiState.isLzOnly,
                            onCheckedChange = { viewModel.handleIntent(ThreadUiIntent.ToggleLzOnly(it)) }
                        )
                    }
                }
            }

            // 回复列表
            items(
                items = uiState.posts,
                key = { it.id }
            ) { post ->
                PostItem(
                    post = post,
                    onUserClick = { onUserClick(post.authorId) },
                    onLikeClick = { onLikePost(post.id) },
                    onReplyClick = { onReplyPost(post.id, post.authorName) },
                    onLoadComments = { onLoadComments(post.id) }
                )
            }

            // 加载更多指示器
            if (uiState.isLoading && uiState.posts.isNotEmpty()) {
                item {
                    LoadingState(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                    )
                }
            }
        }
    }
}

/**
 * 帖子头部
 */
@Composable
private fun ThreadHeader(
    thread: com.newtieba.domain.model.Thread?,
    forum: com.newtieba.domain.model.Forum?,
    onUserClick: (Long) -> Unit,
    onForumClick: (Long, String) -> Unit
) {
    Column {
        // 标题
        Text(
            text = thread?.title ?: "",
            style = TiebaTypography.h4,
            color = MiuixTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 作者信息
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            UserAvatar(
                avatarUrl = thread?.authorAvatar ?: "",
                size = 40,
                modifier = Modifier.clickable { onUserClick(thread?.authorId ?: 0) }
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = thread?.authorName ?: "",
                    style = TiebaTypography.subtitle2,
                    color = MiuixTheme.colorScheme.onSurface
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Lv.${thread?.authorLevel ?: 0}",
                        style = TiebaTypography.caption,
                        color = TiebaColors.Primary
                    )

                    if (forum != null) {
                        Text(
                            text = " · ",
                            style = TiebaTypography.caption,
                            color = MiuixTheme.colorScheme.onSurfaceVariant
                        )

                        Text(
                            text = forum.name,
                            style = TiebaTypography.caption,
                            color = TiebaColors.Primary,
                            modifier = Modifier.clickable { onForumClick(forum.id, forum.name) }
                        )
                    }
                }
            }
        }
    }
}

/**
 * 帖子正文
 */
@Composable
private fun ThreadBody(
    content: String,
    images: List<String>
) {
    Column {
        Text(
            text = content,
            style = TiebaTypography.body1,
            color = MiuixTheme.colorScheme.onSurface
        )

        if (images.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            ImageGrid(images = images)
        }
    }
}

/**
 * 帖子操作栏
 */
@Composable
private fun ThreadActions(
    isLiked: Boolean,
    likeCount: Int,
    replyCount: Int,
    shareCount: Int,
    onLikeClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        // 点赞
        ActionButton(
            icon = painterResource(
                id = if (isLiked) android.R.drawable.btn_star_big_on
                else android.R.drawable.btn_star_big_off
            ),
            text = likeCount.toString(),
            tint = if (isLiked) TiebaColors.Primary
            else MiuixTheme.colorScheme.onSurfaceVariant,
            onClick = onLikeClick
        )

        // 评论
        ActionButton(
            icon = painterResource(id = android.R.drawable.ic_menu_comment),
            text = replyCount.toString(),
            tint = MiuixTheme.colorScheme.onSurfaceVariant,
            onClick = { }
        )

        // 分享
        ActionButton(
            icon = painterResource(id = android.R.drawable.ic_menu_share),
            text = shareCount.toString(),
            tint = MiuixTheme.colorScheme.onSurfaceVariant,
            onClick = { }
        )
    }
}

/**
 * 操作按钮
 */
@Composable
private fun ActionButton(
    painter: androidx.compose.ui.graphics.painter.Painter,
    text: String,
    tint: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Icon(
            painter = painter,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = tint
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = text,
            style = TiebaTypography.caption,
            color = tint
        )
    }
}

/**
 * 回复项
 */
@Composable
private fun PostItem(
    post: Post,
    onUserClick: () -> Unit,
    onLikeClick: () -> Unit,
    onReplyClick: () -> Unit,
    onLoadComments: () -> Unit
) {
    ThreadCard(
        floor = post.floor,
        content = post.textContent,
        authorName = post.authorName,
        authorAvatar = post.authorAvatar,
        authorLevel = post.authorLevel,
        createTime = post.createTimeFormatted,
        likeCount = post.likeCount,
        isLiked = post.isLiked,
        images = post.images,
        onLikeClick = onLikeClick,
        onReplyClick = onReplyClick,
        onUserClick = onUserClick
    )
}

/**
 * 底部操作栏
 */
@Composable
private fun BottomActionBar(
    isLiked: Boolean,
    isCollected: Boolean,
    replyCount: Int,
    onLikeClick: () -> Unit,
    onCollectClick: () -> Unit,
    onReplyClick: () -> Unit,
    onShareClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 回复输入框
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .clickable(onClick = onReplyClick),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "写回复...",
                    style = TiebaTypography.body2,
                    color = MiuixTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // 点赞
            IconButton(onClick = onLikeClick) {
                Icon(
                    painter = painterResource(
                        id = if (isLiked) android.R.drawable.btn_star_big_on
                        else android.R.drawable.btn_star_big_off
                    ),
                    contentDescription = "点赞",
                    tint = if (isLiked) TiebaColors.Primary
                    else MiuixTheme.colorScheme.onSurfaceVariant
                )
            }

            // 收藏
            IconButton(onClick = onCollectClick) {
                Icon(
                    painter = painterResource(
                        id = if (isCollected) android.R.drawable.btn_star_big_on
                        else android.R.drawable.btn_star_big_off
                    ),
                    contentDescription = "收藏",
                    tint = if (isCollected) TiebaColors.Warning
                    else MiuixTheme.colorScheme.onSurfaceVariant
                )
            }

            // 分享
            IconButton(onClick = onShareClick) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_share),
                    contentDescription = "分享",
                    tint = MiuixTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * 回复输入框覆盖层
 */
@Composable
private fun ReplyInputOverlay(
    state: ReplyInputState,
    onContentChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(onClick = onDismiss)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            shadowElevation = 16.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // 标题
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (state.replyToUserName.isNotBlank()) {
                            "回复 ${state.replyToUserName}"
                        } else {
                            "写回复"
                        },
                        style = TiebaTypography.subtitle1,
                        color = MiuixTheme.colorScheme.onSurface
                    )

                    TextButton(onClick = onDismiss) {
                        Text("取消")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 输入框
                TextField(
                    value = state.content,
                    onValueChange = onContentChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    placeholder = {
                        Text(
                            text = "写下你的回复...",
                            style = TiebaTypography.body2,
                            color = MiuixTheme.colorScheme.onSurfaceVariant
                        )
                    }
                )

                // 错误提示
                if (state.error != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = state.error,
                        style = TiebaTypography.caption,
                        color = TiebaColors.Error
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 提交按钮
                Button(
                    onClick = onSubmit,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.isSubmitting && state.content.isNotBlank()
                ) {
                    if (state.isSubmitting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("发布")
                    }
                }
            }
        }
    }
}
