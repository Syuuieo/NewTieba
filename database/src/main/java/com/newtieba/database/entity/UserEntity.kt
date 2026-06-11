package com.newtieba.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 用户实体
 */
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: Long,
    val name: String,
    val nameShow: String,
    val portrait: String,
    val intro: String,
    val sex: Int,
    val birthday: String,
    val isVip: Boolean,
    val levelId: Int,
    val isGod: Boolean,
    val godLevel: Int,
    val postNum: Int,
    val fansNum: Int,
    val followNum: Int,
    val friendNum: Int,
    val likeNum: Int,
    val agreeNum: Int,
    val tbAge: String,
    val ipAddress: String,
    val isFollowed: Boolean,
    val hasConcern: Boolean,
    val tiebaUid: Long,
    val lastUpdateTime: Long,
    val cacheTime: Long
)
