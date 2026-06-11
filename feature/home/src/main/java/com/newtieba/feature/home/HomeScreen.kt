package com.newtieba.feature.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.newtieba.ui.components.*
import com.newtieba.ui.theme.TiebaTypography
import top.yukonga.miuix.kmp.basic.*
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * 首页Screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onThreadClick: (Long) -> Unit,
    onForumClick: (Long, String) -> Unit,
    onUserClick: (Long) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val recommendState by viewModel.recommendState.collectAsState()
    val forumState by viewModel.forumState.collectAsState()
    val hotState by viewModel.hotState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = "首页"
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Tab栏
            TabRow(
                selectedTabIndex = uiState.currentTab.ordinal,
                modifier = Modifier.fillMaxWidth()
            ) {
                HomeTab.entries.forEach { tab ->
                    Tab(
                        selected = uiState.currentTab == tab,
                        onClick = { viewModel.handleIntent(HomeUiIntent.SwitchTab(tab)) },
                        text = {
                            Text(
                                text = when (tab) {
                                    HomeTab.RECOMMEND -> "推荐"
                                    HomeTab.FORUM -> "关注"
                                    HomeTab.HOT -> "热榜"
                                }
                            )
                        }
                    )
                }
            }

            // 内容
            when (uiState.currentTab) {
                HomeTab.RECOMMEND -> RecommendContent(
                    state = recommendState,
                    onRefresh = { viewModel.handleIntent(HomeUiIntent.Refresh) },
                    onLoadMore = { viewModel.handleIntent(HomeUiIntent.LoadMore) },
                    onThreadClick = onThreadClick,
                    onUserClick = onUserClick
                )
                HomeTab.FORUM -> ForumContent(
                    state = forumState,
                    onRefresh = { viewModel.handleIntent(HomeUiIntent.Refresh) },
                    onLoadMore = { viewModel.handleIntent(HomeUiIntent.LoadMore) },
                    onForumClick = onForumClick,
                    onSignClick = { forumId, forumName ->
                        viewModel.handleIntent(HomeUiIntent.SignForum(forumId, forumName))
                    }
                )
                HomeTab.HOT -> HotContent(
                    state = hotState,
                    onRefresh = { viewModel.handleIntent(HomeUiIntent.Refresh) },
                    onLoadMore = { viewModel.handleIntent(HomeUiIntent.LoadMore) },
                    onThreadClick = onThreadClick,
                    onUserClick = onUserClick
                )
            }
        }
    }
}

/**
 * 推荐内容
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RecommendContent(
    state: RecommendUiState,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    onThreadClick: (Long) -> Unit,
    onUserClick: (Long) -> Unit
) {
    val listState = rememberLazyListState()

    // 监听滚动到底部，加载更多
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo }
            .collect { layoutInfo ->
                val lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                val totalItemsCount = layoutInfo.totalItemsCount
                if (lastVisibleIndex >= totalItemsCount - 5 && !state.isLoading && state.hasMore) {
                    onLoadMore()
                }
            }
    }

    PullToRefreshBox(
        isRefreshing = state.isRefreshing,
        onRefresh = onRefresh
    ) {
        if (state.isLoading && state.threads.isEmpty()) {
            LoadingState()
        } else if (state.error != null && state.threads.isEmpty()) {
            ErrorState(
                message = state.error,
                onRetry = onRefresh
            )
        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = state.threads,
                    key = { it.id }
                ) { thread ->
                    FeedCard(
                        title = thread.title,
                        content = thread.content,
                        authorName = thread.authorName,
                        authorAvatar = thread.authorAvatar,
                        forumName = thread.forumName,
                        replyCount = thread.replyCount,
                        viewCount = thread.viewCount,
                        createTime = thread.createTime,
                        images = thread.images,
                        onClick = { onThreadClick(thread.id) }
                    )
                }

                // 加载更多指示器
                if (state.isLoading && state.threads.isNotEmpty()) {
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
}

/**
 * 关注的吧内容
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ForumContent(
    state: ForumUiState,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    onForumClick: (Long, String) -> Unit,
    onSignClick: (Long, String) -> Unit
) {
    val listState = rememberLazyListState()

    // 监听滚动到底部，加载更多
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo }
            .collect { layoutInfo ->
                val lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                val totalItemsCount = layoutInfo.totalItemsCount
                if (lastVisibleIndex >= totalItemsCount - 5 && !state.isLoading && state.hasMore) {
                    onLoadMore()
                }
            }
    }

    PullToRefreshBox(
        isRefreshing = state.isRefreshing,
        onRefresh = onRefresh
    ) {
        if (state.isLoading && state.forums.isEmpty()) {
            LoadingState()
        } else if (state.error != null && state.forums.isEmpty()) {
            ErrorState(
                message = state.error,
                onRetry = onRefresh
            )
        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = state.forums,
                    key = { it.id }
                ) { forum ->
                    ForumCard(
                        forum = forum,
                        onClick = { onForumClick(forum.id, forum.name) },
                        onSignClick = { onSignClick(forum.id, forum.name) }
                    )
                }

                // 加载更多指示器
                if (state.isLoading && state.forums.isNotEmpty()) {
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
}

/**
 * 热榜内容
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HotContent(
    state: HotUiState,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    onThreadClick: (Long) -> Unit,
    onUserClick: (Long) -> Unit
) {
    val listState = rememberLazyListState()

    // 监听滚动到底部，加载更多
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo }
            .collect { layoutInfo ->
                val lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                val totalItemsCount = layoutInfo.totalItemsCount
                if (lastVisibleIndex >= totalItemsCount - 5 && !state.isLoading && state.hasMore) {
                    onLoadMore()
                }
            }
    }

    PullToRefreshBox(
        isRefreshing = state.isRefreshing,
        onRefresh = onRefresh
    ) {
        if (state.isLoading && state.threads.isEmpty()) {
            LoadingState()
        } else if (state.error != null && state.threads.isEmpty()) {
            ErrorState(
                message = state.error,
                onRetry = onRefresh
            )
        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = state.threads,
                    key = { it.id }
                ) { thread ->
                    FeedCard(
                        title = thread.title,
                        content = thread.content,
                        authorName = thread.authorName,
                        authorAvatar = thread.authorAvatar,
                        forumName = thread.forumName,
                        replyCount = thread.replyCount,
                        viewCount = thread.viewCount,
                        createTime = thread.createTime,
                        images = thread.images,
                        onClick = { onThreadClick(thread.id) }
                    )
                }

                // 加载更多指示器
                if (state.isLoading && state.threads.isNotEmpty()) {
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
}

/**
 * 吧卡片组件
 */
@Composable
private fun ForumCard(
    forum: com.newtieba.domain.model.Forum,
    onClick: () -> Unit,
    onSignClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        onClick = onClick,
        cornerRadius = 12.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 吧头像
            UserAvatar(
                avatarUrl = forum.avatar,
                size = 48
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                // 吧名
                Text(
                    text = forum.name,
                    style = TiebaTypography.subtitle1,
                    color = MiuixTheme.colorScheme.onSurface
                )

                // 成员数和帖子数
                Text(
                    text = "${forum.memberNum}成员 · ${forum.threadNum}帖子",
                    style = TiebaTypography.caption,
                    color = MiuixTheme.colorScheme.onSurfaceVariant
                )
            }

            // 签到按钮
            if (!forum.isSigned) {
                Button(
                    onClick = onSignClick,
                    text = "签到"
                )
            } else {
                Text(
                    text = "已签到",
                    style = TiebaTypography.caption,
                    color = MiuixTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
