package com.newtieba.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.newtieba.ui.components.ThreadCard
import com.newtieba.ui.components.UserAvatar
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.MiuixTheme
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.TabRow
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TopAppBar

@Composable
fun ProfileScreen(
    uid: Long = 0,
    isMe: Boolean = false,
    onNavigateToSettings: () -> Unit = {},
    onNavigateToProfile: (Long) -> Unit = {},
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState

    LaunchedEffect(uid, isMe) {
        viewModel.init(uid, isMe)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = if (isMe) "我的" else "用户",
                actions = {
                    if (isMe) {
                        IconButton(onClick = onNavigateToSettings) {
                            Icon(Icons.Default.Settings, "设置")
                        }
                    }
                },
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            // 用户信息头部
            item(key = "header") {
                uiState.user?.let { user ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        // 大背景头像
                        val avatarUrl = if (user.portrait.isNotEmpty()) {
                            "http://tb.himg.baidu.com/sys/portraith/item/${user.portrait}"
                        } else ""
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(MiuixTheme.colorScheme.surfaceVariant),
                        ) {
                            if (avatarUrl.isNotEmpty()) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(avatarUrl)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.size(80.dp).clip(CircleShape),
                                )
                            }
                        }

                        Spacer(Modifier.height(12.dp))
                        Text(
                            text = user.name,
                            style = MiuixTheme.textStyles.title,
                            color = MiuixTheme.colorScheme.onSurface,
                        )
                        if (user.level > 0) {
                            Text(
                                text = "Lv.${user.level}",
                                style = MiuixTheme.textStyles.body3,
                                color = MiuixTheme.colorScheme.primary,
                                modifier = Modifier.padding(top = 4.dp),
                            )
                        }
                        if (user.intro.isNotEmpty()) {
                            Text(
                                text = user.intro,
                                style = MiuixTheme.textStyles.body3,
                                color = MiuixTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(top = 8.dp),
                                textAlign = TextAlign.Center,
                            )
                        }

                        Spacer(Modifier.height(16.dp))

                        // 数据行
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                        ) {
                            ProfileStat("关注", "12")
                            ProfileStat("粉丝", "34")
                            ProfileStat("获赞", "56")
                        }
                    }
                }
            }

            // TabRow
            item(key = "tabs") {
                TabRow(
                    selectedIndex = uiState.selectedTab,
                    onTabChange = { viewModel.selectTab(it) },
                ) {
                    listOf("帖子", "回复", "关注的吧").forEach { tab ->
                        Text(
                            text = tab,
                            modifier = Modifier.padding(16.dp),
                            color = MiuixTheme.colorScheme.onSurface,
                        )
                    }
                }
            }

            // Tab 内容
            when (uiState.selectedTab) {
                0, 1 -> {
                    // 帖子 / 回复 列表
                    items(
                        items = uiState.threads,
                        key = { it.tid },
                    ) { thread ->
                        ThreadCard(
                            thread = thread,
                            onClick = { },
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        )
                    }
                }
                2 -> {
                    // 关注的吧 - FlowRow 网格
                    item(key = "forums_grid") {
                        FlowRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            uiState.forums.forEach { forum ->
                                Column(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(MiuixTheme.colorScheme.surfaceVariant)
                                        .clickable { },
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(48.dp)
                                            .padding(4.dp)
                                            .clip(CircleShape)
                                            .background(MiuixTheme.colorScheme.surfaceContainer),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        Text(
                                            text = forum.name.take(1),
                                            color = MiuixTheme.colorScheme.primary,
                                        )
                                    }
                                    Text(
                                        text = forum.name,
                                        style = MiuixTheme.textStyles.body3,
                                        color = MiuixTheme.colorScheme.onSurface,
                                        maxLines = 1,
                                        modifier = Modifier.padding(top = 4.dp),
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileStat(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MiuixTheme.textStyles.title,
            color = MiuixTheme.colorScheme.onSurface,
        )
        Text(
            text = label,
            style = MiuixTheme.textStyles.body3,
            color = MiuixTheme.colorScheme.onSurfaceVariant,
        )
    }
}
