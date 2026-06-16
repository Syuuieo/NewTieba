package com.newtieba.ui.message

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.newtieba.data.model.TiebaUser
import com.newtieba.ui.components.UserAvatar
import com.newtieba.util.DateFormatter
import top.yukonga.miuix.kmp.basic.Badge
import top.yukonga.miuix.kmp.basic.MiuixTheme
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.TabRow
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TopAppBar

@Composable
fun MessageScreen(
    viewModel: MessageViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState

    Scaffold(
        topBar = {
            TopAppBar(title = "消息")
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            TabRow(
                selectedIndex = uiState.selectedTab,
                onTabChange = { viewModel.selectTab(it) },
            ) {
                listOf("回复", "@我的", "私信").forEachIndexed { i, tab ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(16.dp),
                    ) {
                        Text(
                            text = tab,
                            color = MiuixTheme.colorScheme.onSurface,
                        )
                        val badgeCount = when (i) {
                            0 -> uiState.replyMessages.count { !it.isRead }
                            1 -> uiState.atMessages.count { !it.isRead }
                            else -> uiState.conversations.sumOf { it.unreadCount }
                        }
                        if (badgeCount > 0) {
                            Spacer(Modifier.width(4.dp))
                            Badge(
                                text = badgeCount.toString(),
                            )
                        }
                    }
                }
            }

            if (uiState.isLoading) {
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
                when (uiState.selectedTab) {
                    0 -> MessageList(
                        items = uiState.replyMessages,
                        getTitle = { "${it.fromUser.name} 回复了你" },
                        getSubtitle = { it.content },
                    )
                    1 -> MessageList(
                        items = uiState.atMessages,
                        getTitle = { "${it.fromUser.name} @了你" },
                        getSubtitle = { it.content },
                    )
                    2 -> ConversationList(
                        conversations = uiState.conversations,
                    )
                }
            }
        }
    }
}

@Composable
private fun MessageList(
    items: List<MessageItem>,
    getTitle: (MessageItem) -> String,
    getSubtitle: (MessageItem) -> String,
) {
    LazyColumn(
        contentPadding = PaddingValues(vertical = 4.dp),
    ) {
        items(items, key = { it.id }) { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { }
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                UserAvatar(portrait = item.fromUser.portrait, size = 44.dp)
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = getTitle(item),
                            style = MiuixTheme.textStyles.body2,
                            color = if (item.isRead) MiuixTheme.colorScheme.onSurface
                                else MiuixTheme.colorScheme.onSurface,
                            maxLines = 1,
                        )
                        Text(
                            text = DateFormatter.format(item.time),
                            style = MiuixTheme.textStyles.body3,
                            color = MiuixTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = getSubtitle(item),
                        style = MiuixTheme.textStyles.body3,
                        color = MiuixTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}

@Composable
private fun ConversationList(conversations: List<PrivateConversation>) {
    LazyColumn(
        contentPadding = PaddingValues(vertical = 4.dp),
    ) {
        items(conversations, key = { it.id }) { conv ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { }
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                UserAvatar(portrait = conv.withUser.portrait, size = 44.dp)
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = conv.withUser.name,
                            style = MiuixTheme.textStyles.body2,
                            color = MiuixTheme.colorScheme.onSurface,
                            maxLines = 1,
                        )
                        Text(
                            text = DateFormatter.format(conv.lastTime),
                            style = MiuixTheme.textStyles.body3,
                            color = MiuixTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Spacer(Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = conv.lastMessage,
                            style = MiuixTheme.textStyles.body3,
                            color = MiuixTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f),
                        )
                        if (conv.unreadCount > 0) {
                            Spacer(Modifier.width(8.dp))
                            Badge(text = conv.unreadCount.toString())
                        }
                    }
                }
            }
        }
    }
}
