package com.newtieba.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.newtieba.ui.theme.TiebaColors
import com.newtieba.ui.theme.TiebaTypography
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * 帖子卡片组件
 */
@Composable
fun FeedCard(
    title: String,
    content: String,
    authorName: String,
    authorAvatar: String,
    forumName: String,
    replyCount: Int,
    viewCount: Int,
    createTime: String,
    images: List<String> = emptyList(),
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        cornerRadius = 12.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 作者信息行
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 头像
                AsyncImage(
                    model = authorAvatar,
                    contentDescription = "作者头像",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    // 作者名称
                    Text(
                        text = authorName,
                        style = TiebaTypography.subtitle2,
                        color = MiuixTheme.colorScheme.onSurface
                    )

                    // 吧名和时间
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = forumName,
                            style = TiebaTypography.caption,
                            color = TiebaColors.Primary
                        )

                        Text(
                            text = " · ",
                            style = TiebaTypography.caption,
                            color = MiuixTheme.colorScheme.onSurfaceVariant
                        )

                        Text(
                            text = createTime,
                            style = TiebaTypography.caption,
                            color = MiuixTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 标题
            if (title.isNotBlank()) {
                Text(
                    text = title,
                    style = TiebaTypography.h5,
                    color = MiuixTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // 内容
            Text(
                text = content,
                style = TiebaTypography.body2,
                color = MiuixTheme.colorScheme.onSurfaceVariant,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            // 图片
            if (images.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                ImageGrid(images = images.take(3))
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 底部信息
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 回复数
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_menu_comment),
                        contentDescription = "回复",
                        modifier = Modifier.size(16.dp),
                        tint = MiuixTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = replyCount.toString(),
                        style = TiebaTypography.caption,
                        color = MiuixTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // 浏览数
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_menu_view),
                        contentDescription = "浏览",
                        modifier = Modifier.size(16.dp),
                        tint = MiuixTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = viewCount.toString(),
                        style = TiebaTypography.caption,
                        color = MiuixTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

/**
 * 图片网格组件
 */
@Composable
fun ImageGrid(
    images: List<String>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        images.forEach { imageUrl ->
            AsyncImage(
                model = imageUrl,
                contentDescription = "帖子图片",
                modifier = Modifier
                    .weight(1f)
                    .height(100.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}

/**
 * 用户头像组件
 */
@Composable
fun UserAvatar(
    avatarUrl: String,
    size: Int = 40,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = avatarUrl,
        contentDescription = "用户头像",
        modifier = modifier
            .size(size.dp)
            .clip(CircleShape),
        contentScale = ContentScale.Crop
    )
}

/**
 * 加载状态组件
 */
@Composable
fun LoadingState(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.material3.CircularProgressIndicator()
    }
}

/**
 * 错误状态组件
 */
@Composable
fun ErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            style = TiebaTypography.body1,
            color = MiuixTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        top.yukonga.miuix.kmp.basic.Button(
            onClick = onRetry,
            text = "重试"
        )
    }
}

/**
 * 空状态组件
 */
@Composable
fun EmptyState(
    message: String = "暂无数据",
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = TiebaTypography.body1,
            color = MiuixTheme.colorScheme.onSurfaceVariant
        )
    }
}
