package com.newtieba.ui.hot

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
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
import top.yukonga.miuix.kmp.basic.TabRow
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TopAppBar

@Composable
fun HotScreen(
    onNavigateToThread: (Long) -> Unit = {},
    viewModel: HotViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState

    Scaffold(
        topBar = {
            TopAppBar(title = "热门")
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
        ) {
            TabRow(
                selectedIndex = uiState.selectedTab,
                onTabChange = { viewModel.selectTab(it) },
            ) {
                uiState.tabs.forEach { tab ->
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
                        text = "加载中…",
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
