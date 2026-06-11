package com.newtieba.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 历史记录实体
 */
@Entity(tableName = "history")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val threadId: Long,
    val threadTitle: String,
    val threadContent: String,
    val forumId: Long,
    val forumName: String,
    val authorId: Long,
    val authorName: String,
    val authorAvatar: String,
    val replyCount: Int,
    val viewCount: Int,
    val lastViewTime: Long,
    val viewProgress: Int, // 浏览进度（楼层）
    val createTime: Long
)
