package com.newtieba.ui.forum

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.newtieba.ui.components.ThreadCard
import top.yukonga.miuix.kmp.basic.Button
import top.yukonga.miuix.kmp.basic.MiuixTheme
import top.yukonga.miuix.kmp.basic.PullToRefresh
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TopAppBar

@Composable
fun ForumScreen(
    forumName: String,
    onBack: () -> Unit = {},
    onNavigateToThread: (Long) -> Unit = {},
    viewModel: ForumViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState

    LaunchedEffect(forumName) {
        viewModel.init(forumName)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = forumName,
                onBack = onBack,
            )
        },
    ) { paddingValues ->
        PullToRefresh(
            isRefreshing = uiState.isRefreshing,
            onRefresh = { viewModel.refresh() },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // 吧信息栏
                uiState.forumInfo?.let { info ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "${info.memberNum} 关注",
                                style = MiuixTheme.textStyles.body3,
                                color = MiuixTheme.colorScheme.onSurfaceVariant,
                            )
                            Text(
                                text = "${info.threadNum} 帖子",
                                style = MiuixTheme.textStyles.body3,
                                color = MiuixTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        Button(
                            onClick = { viewModel.toggleFollow() },
                        ) {
                            Text(
                                text = if (uiState.isFollowed) "已关注" else "关注",
                                color = MiuixTheme.colorScheme.onPrimary,
                            )
                        }
                    }
                }

                // 分类/排序栏
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Text(
                        text = "全部",
                        style = MiuixTheme.textStyles.body2,
                        color = if (!uiState.isGood) MiuixTheme.colorScheme.primary
                            else MiuixTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 4.dp),
                    )
                    Text(
                        text = "精品",
                        style = MiuixTheme.textStyles.body2,
                        color = if (uiState.isGood) MiuixTheme.colorScheme.primary
                            else MiuixTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 4.dp),
                    )
                    Spacer(Modifier.weight(1f))
                }

                LazyColumn(
                    contentPadding = PaddingValues(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(
                        items = uiState.threads,
                        key = { it.tid },
                    ) { thread ->
                        ThreadCard(
                            thread = thread,
                            onClick = { onNavigateToThread(thread.tid) },
                        )
                    }
                }
            }
        }
    }
}
