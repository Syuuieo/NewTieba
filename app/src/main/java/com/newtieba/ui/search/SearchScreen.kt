package com.newtieba.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.newtieba.ui.components.ThreadCard
import top.yukonga.miuix.kmp.basic.MiuixTheme
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.SearchBar
import top.yukonga.miuix.kmp.basic.TabRow
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TopAppBar

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchScreen(
    onBack: () -> Unit = {},
    onNavigateToThread: (Long) -> Unit = {},
    onNavigateToForum: (String) -> Unit = {},
    viewModel: SearchViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState

    Scaffold(
        topBar = {
            TopAppBar(
                title = "搜索",
                onBack = onBack,
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            // 搜索栏
            SearchBar(
                query = uiState.query,
                onQueryChange = { viewModel.updateQuery(it) },
                onSearch = { viewModel.search() },
                placeholder = "搜索贴吧、帖子、用户",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
            )

            if (uiState.query.isBlank()) {
                // 热搜/历史记录
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "热搜榜",
                        style = MiuixTheme.textStyles.body1,
                        modifier = Modifier.padding(bottom = 8.dp),
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        uiState.hotQueries.forEachIndexed { index, query ->
                            Text(
                                text = "${index + 1}. $query",
                                style = MiuixTheme.textStyles.body2,
                                color = if (index < 3) MiuixTheme.colorScheme.primary
                                    else MiuixTheme.colorScheme.onSurface,
                                modifier = Modifier.clickable {
                                    viewModel.updateQuery(query)
                                    viewModel.search()
                                },
                            )
                        }
                    }

                    if (uiState.history.isNotEmpty()) {
                        Text(
                            text = "搜索历史",
                            style = MiuixTheme.textStyles.body1,
                            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
                        )
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            uiState.history.forEach { item ->
                                Text(
                                    text = item,
                                    style = MiuixTheme.textStyles.body2,
                                    color = MiuixTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.clickable {
                                        viewModel.updateQuery(item)
                                        viewModel.search()
                                    },
                                )
                            }
                        }
                    }
                }
            } else {
                // 搜索结果
                TabRow(
                    selectedIndex = uiState.selectedTab,
                    onTabChange = { viewModel.selectTab(it) },
                ) {
                    listOf("帖子", "贴吧", "用户").forEach { tab ->
                        Text(
                            text = tab,
                            modifier = Modifier.padding(16.dp),
                            color = MiuixTheme.colorScheme.onSurface,
                        )
                    }
                }

                if (uiState.isLoading && uiState.threads.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "搜索中…",
                            color = MiuixTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                } else {
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
}
