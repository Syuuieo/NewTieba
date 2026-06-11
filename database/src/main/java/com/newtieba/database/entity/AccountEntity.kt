package com.newtieba.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 账户实体
 */
@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey
    val uid: Long,
    val userName: String,
    val nameShow: String,
    val portrait: String,
    val bduss: String,
    val sToken: String,
    val tbs: String,
    val intro: String,
    val sex: Int,
    val isVip: Boolean,
    val levelId: Int,
    val postNum: Int,
    val fansNum: Int,
    val followNum: Int,
    val agreeNum: Int,
    val tiebaUid: Long,
    val isLoggedIn: Boolean,
    val lastLoginTime: Long,
    val createTime: Long
)
