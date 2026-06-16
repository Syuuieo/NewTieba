package com.newtieba.ui.thread

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.newtieba.ui.components.PostCard
import com.newtieba.ui.components.UserAvatar
import com.newtieba.util.DateFormatter
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.MiuixTheme
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextField
import top.yukonga.miuix.kmp.basic.TopAppBar

@Composable
fun ThreadScreen(
    tid: Long,
    onBack: () -> Unit = {},
    onNavigateToProfile: (Long) -> Unit = {},
    viewModel: ThreadViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState

    LaunchedEffect(tid) {
        viewModel.init(tid)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = uiState.thread?.title ?: "帖子详情",
                onBack = onBack,
                actions = {
                    IconButton(onClick = { /* TODO: 分享 */ }) {
                        Icon(Icons.Default.Share, "分享")
                    }
                },
            )
        },
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            if (uiState.isLoading && uiState.posts.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "加载中…",
                        color = MiuixTheme.colorScheme.onSurfaceVariant,
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    // 楼主帖
                    uiState.thread?.let { thread ->
                        item(key = "header") {
                            Column(modifier = Modifier.padding(bottom = 8.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    UserAvatar(
                                        portrait = thread.author.portrait,
                                        size = 36.dp,
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = thread.author.name,
                                            style = MiuixTheme.textStyles.body2,
                                            color = MiuixTheme.colorScheme.onSurface,
                                        )
                                        Text(
                                            text = DateFormatter.format(thread.createTime),
                                            style = MiuixTheme.textStyles.body3,
                                            color = MiuixTheme.colorScheme.onSurfaceVariant,
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // 所有楼层
                    items(
                        items = uiState.posts,
                        key = { it.pid },
                    ) { post ->
                        PostCard(
                            post = post,
                            onUserClick = { onNavigateToProfile(post.author.uid) },
                        )
                    }

                    // 加载更多
                    if (uiState.hasMore && !uiState.isLoading) {
                        item(key = "load_more") {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = "点击加载更多",
                                    color = MiuixTheme.colorScheme.primary,
                                )
                            }
                        }
                    }
                }

                // 底部回复栏
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TextField(
                        value = "",
                        onValueChange = { },
                        placeholder = "说点什么…",
                        modifier = Modifier.weight(1f),
                    )
                    Spacer(Modifier.width(8.dp))
                    // TODO: 发送按钮
                }
            }
        }
    }
}
