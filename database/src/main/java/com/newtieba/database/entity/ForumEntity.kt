package com.newtieba.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 吧实体
 */
@Entity(tableName = "forums")
data class ForumEntity(
    @PrimaryKey
    val id: Long,
    val name: String,
    val avatar: String,
    val slogan: String,
    val memberNum: Int,
    val threadNum: Int,
    val postNum: Int,
    val levelId: Int,
    val levelName: String,
    val isSigned: Boolean,
    val isLiked: Boolean,
    val isTop: Boolean,
    val curScore: Int,
    val levelupScore: Int,
    val lastUpdateTime: Long,
    val createTime: Long
)
