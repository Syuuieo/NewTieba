package com.newtieba.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import top.yukonga.miuix.kmp.basic.MiuixTheme

@Composable
fun UserAvatar(
    portrait: String,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
) {
    val avatarUrl = if (portrait.isNotEmpty()) {
        "http://tb.himg.baidu.com/sys/portraith/item/$portrait"
    } else {
        ""
    }
    Box(
        modifier = modifier
            .size(size)
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
                modifier = Modifier.size(size).clip(CircleShape),
            )
        }
    }
}
