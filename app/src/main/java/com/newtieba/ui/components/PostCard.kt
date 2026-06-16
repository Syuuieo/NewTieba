package com.newtieba.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.MiuixTheme
import top.yukonga.miuix.kmp.basic.Text
import com.newtieba.data.model.ContentPiece
import com.newtieba.data.model.ContentType
import com.newtieba.data.model.Post
import com.newtieba.util.DateFormatter
import top.yukonga.miuix.kmp.basic.MiuixTheme

@Composable
fun PostCard(
    post: Post,
    onUserClick: () -> Unit = {},
    onImageClick: (Int) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier.padding(vertical = 4.dp)) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                UserAvatar(portrait = post.author.portrait, size = 28.dp)
                Spacer(Modifier.width(8.dp))
                Text(
                    text = post.author.name,
                    style = MiuixTheme.textStyles.body2,
                    color = MiuixTheme.colorScheme.onSurface,
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = "${post.floor}楼",
                    style = MiuixTheme.textStyles.body3,
                    color = MiuixTheme.colorScheme.onSurfaceVariant,
                )
            }

            Spacer(Modifier.height(8.dp))

            RichTextContent(pieces = post.content)

            if (post.images.isNotEmpty()) {
                Spacer(Modifier.height(4.dp))
                ImageGrid(images = post.images, onImageClick = onImageClick)
            }

            Spacer(Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = DateFormatter.format(post.time),
                    style = MiuixTheme.textStyles.body3,
                    color = MiuixTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = "点赞",
                    modifier = Modifier.size(16.dp),
                    tint = MiuixTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = post.agreeNum.toString(),
                    style = MiuixTheme.textStyles.body3,
                    color = MiuixTheme.colorScheme.onSurfaceVariant,
                )
            }

            if (post.subPostCount > 0) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "共 ${post.subPostCount} 条回复 >",
                    style = MiuixTheme.textStyles.body3,
                    color = MiuixTheme.colorScheme.primary,
                )
            }
        }
    }
}

@Composable
fun RichTextContent(
    pieces: List<ContentPiece>,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        for (piece in pieces) {
            when (piece.type) {
                ContentType.TEXT -> Text(
                    text = piece.text,
                    style = MiuixTheme.textStyles.body2,
                    color = MiuixTheme.colorScheme.onSurface,
                )
                ContentType.AT_USER -> Text(
                    text = piece.text,
                    style = MiuixTheme.textStyles.body2,
                    color = MiuixTheme.colorScheme.primary,
                )
                ContentType.LINK -> Text(
                    text = piece.text,
                    style = MiuixTheme.textStyles.body2,
                    color = MiuixTheme.colorScheme.primary,
                    modifier = Modifier.clickable { /* TODO: CustomTabsIntent */ },
                )
                ContentType.EMOJI -> Text(
                    text = piece.text,
                    style = MiuixTheme.textStyles.body2,
                    color = MiuixTheme.colorScheme.onSurface,
                )
                else -> Text(
                    text = piece.text,
                    style = MiuixTheme.textStyles.body2,
                    color = MiuixTheme.colorScheme.onSurface,
                )
            }
        }
    }
}
