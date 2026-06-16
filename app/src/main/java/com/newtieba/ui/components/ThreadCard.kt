package com.newtieba.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.MiuixTheme
import top.yukonga.miuix.kmp.basic.Text
import com.newtieba.data.model.Thread
import com.newtieba.util.DateFormatter

@Composable
fun ThreadCard(
    thread: Thread,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val tint = MiuixTheme.colorScheme.onSurfaceVariant

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
        ) {
            // 作者行
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                UserAvatar(
                    portrait = thread.author.portrait,
                    size = 32.dp,
                )
                Spacer(Modifier.width(8.dp))
                Column {
                    Text(
                        text = thread.author.name,
                        style = MiuixTheme.textStyles.body2,
                        color = MiuixTheme.colorScheme.onSurface,
                    )
                    Text(
                        text = DateFormatter.format(thread.lastTime),
                        style = MiuixTheme.textStyles.body3,
                        color = MiuixTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            // 标题
            Text(
                text = thread.title,
                style = MiuixTheme.textStyles.body1,
                color = MiuixTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

            // 摘要
            if (thread.abstract.isNotEmpty()) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = thread.abstract,
                    style = MiuixTheme.textStyles.body2,
                    color = MiuixTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            // 图片
            if (thread.images.isNotEmpty()) {
                Spacer(Modifier.height(4.dp))
                ImageGrid(images = thread.images)
            }

            Spacer(Modifier.height(8.dp))

            // 底部操作栏
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.ChatBubbleOutline,
                        contentDescription = "回复",
                        modifier = Modifier.size(16.dp),
                        tint = tint,
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = formatCount(thread.replyNum),
                        style = MiuixTheme.textStyles.body3,
                        color = MiuixTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "点赞",
                        modifier = Modifier.size(16.dp),
                        tint = tint,
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = formatCount(thread.agreeNum),
                        style = MiuixTheme.textStyles.body3,
                        color = MiuixTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "分享",
                        modifier = Modifier.size(16.dp),
                        tint = tint,
                    )
                }
            }
        }
    }
}

private fun formatCount(count: Int): String {
    return when {
        count >= 10000 -> "${count / 10000}万"
        count >= 1000 -> "${count / 1000}k"
        else -> count.toString()
    }
}
