package com.newtieba.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 帖子实体
 */
@Entity(tableName = "threads")
data class ThreadEntity(
    @PrimaryKey
    val id: Long,
    val title: String,
    val content: String,
    val authorId: Long,
    val authorName: String,
    val authorAvatar: String,
    val authorLevel: Int,
    val forumId: Long,
    val forumName: String,
    val replyCount: Int,
    val viewCount: Int,
    val likeCount: Int,
    val shareCount: Int,
    val isTop: Boolean,
    val isGood: Boolean,
    val isLiked: Boolean,
    val isCollected: Boolean,
    val createTime: Long,
    val lastReplyTime: Long,
    val images: String, // JSON数组
    val abstract: String,
    val lastUpdateTime: Long,
    val cacheTime: Long
)
