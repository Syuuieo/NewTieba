package com.newtieba.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
 * 帖子回复卡片组件
 */
@Composable
fun ThreadCard(
    floor: Int,
    content: String,
    authorName: String,
    authorAvatar: String,
    authorLevel: Int,
    createTime: String,
    likeCount: Int,
    isLiked: Boolean = false,
    images: List<String> = emptyList(),
    onLikeClick: () -> Unit,
    onReplyClick: () -> Unit,
    onUserClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
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
                UserAvatar(
                    avatarUrl = authorAvatar,
                    size = 36,
                    modifier = Modifier.clickable(onClick = onUserClick)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    // 作者名称
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = authorName,
                            style = TiebaTypography.subtitle2,
                            color = MiuixTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        // 等级标签
                        LevelBadge(level = authorLevel)
                    }

                    // 楼层和时间
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${floor}楼",
                            style = TiebaTypography.caption,
                            color = MiuixTheme.colorScheme.onSurfaceVariant
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

            // 内容
            Text(
                text = content,
                style = TiebaTypography.body2,
                color = MiuixTheme.colorScheme.onSurface
            )

            // 图片
            if (images.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                ImageGrid(images = images)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 底部操作栏
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // 点赞按钮
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable(onClick = onLikeClick)
                ) {
                    Icon(
                        painter = painterResource(
                            id = if (isLiked) android.R.drawable.btn_star_big_on
                            else android.R.drawable.btn_star_big_off
                        ),
                        contentDescription = "点赞",
                        modifier = Modifier.size(20.dp),
                        tint = if (isLiked) TiebaColors.Primary
                        else MiuixTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = likeCount.toString(),
                        style = TiebaTypography.caption,
                        color = if (isLiked) TiebaColors.Primary
                        else MiuixTheme.colorScheme.onSurfaceVariant
                    )
                }

                // 回复按钮
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable(onClick = onReplyClick)
                ) {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_menu_comment),
                        contentDescription = "回复",
                        modifier = Modifier.size(20.dp),
                        tint = MiuixTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "回复",
                        style = TiebaTypography.caption,
                        color = MiuixTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

/**
 * 等级标签组件
 */
@Composable
fun LevelBadge(
    level: Int,
    modifier: Modifier = Modifier
) {
    val color = TiebaColors.getLevelColor(level)
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(color.copy(alpha = 0.1f))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = "Lv.$level",
            style = TiebaTypography.tiny,
            color = color
        )
    }
}

/**
 * 评论卡片组件
 */
@Composable
fun CommentCard(
    content: String,
    authorName: String,
    authorAvatar: String,
    createTime: String,
    likeCount: Int,
    isLiked: Boolean = false,
    onLikeClick: () -> Unit,
    onReplyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        cornerRadius = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // 作者信息行
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 头像
                UserAvatar(
                    avatarUrl = authorAvatar,
                    size = 28
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column(modifier = Modifier.weight(1f)) {
                    // 作者名称
                    Text(
                        text = authorName,
                        style = TiebaTypography.caption,
                        color = MiuixTheme.colorScheme.onSurface
                    )

                    // 时间
                    Text(
                        text = createTime,
                        style = TiebaTypography.tiny,
                        color = MiuixTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 内容
            Text(
                text = content,
                style = TiebaTypography.body3,
                color = MiuixTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 底部操作栏
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // 点赞按钮
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable(onClick = onLikeClick)
                ) {
                    Icon(
                        painter = painterResource(
                            id = if (isLiked) android.R.drawable.btn_star_big_on
                            else android.R.drawable.btn_star_big_off
                        ),
                        contentDescription = "点赞",
                        modifier = Modifier.size(16.dp),
                        tint = if (isLiked) TiebaColors.Primary
                        else MiuixTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = likeCount.toString(),
                        style = TiebaTypography.tiny,
                        color = if (isLiked) TiebaColors.Primary
                        else MiuixTheme.colorScheme.onSurfaceVariant
                    )
                }

                // 回复按钮
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable(onClick = onReplyClick)
                ) {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_menu_comment),
                        contentDescription = "回复",
                        modifier = Modifier.size(16.dp),
                        tint = MiuixTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "回复",
                        style = TiebaTypography.tiny,
                        color = MiuixTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
