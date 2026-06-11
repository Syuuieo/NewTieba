package com.newtieba.data.mapper

import com.newtieba.database.entity.AccountEntity
import com.newtieba.database.entity.UserEntity
import com.newtieba.domain.model.User
import com.newtieba.protocol.api.models.response.UserRes

/**
 * 用户映射器
 */

/**
 * 将UserRes.UserInfo转换为User
 */
fun UserRes.UserInfo.toDomain(): User {
    return User(
        id = id,
        name = name,
        nameShow = nameShow,
        portrait = portrait,
        intro = intro,
        sex = sex,
        birthday = birthday,
        isVip = isVip == 1,
        levelId = levelId,
        isGod = isGod == 1,
        godLevel = godLevel,
        postNum = postNum,
        fansNum = fansNum,
        followNum = followNum,
        friendNum = friendNum,
        likeNum = likeNum,
        agreeNum = agreeNum,
        tbAge = tbAge,
        ipAddress = ipAddress,
        isFollowed = isFollowed == 1,
        hasConcern = hasConcern == 1,
        tiebaUid = tiebaUid
    )
}

/**
 * 将User转换为UserEntity
 */
fun User.toEntity(): UserEntity {
    return UserEntity(
        id = id,
        name = name,
        nameShow = nameShow,
        portrait = portrait,
        intro = intro,
        sex = sex,
        birthday = birthday,
        isVip = isVip,
        levelId = levelId,
        isGod = isGod,
        godLevel = godLevel,
        postNum = postNum,
        fansNum = fansNum,
        followNum = followNum,
        friendNum = friendNum,
        likeNum = likeNum,
        agreeNum = agreeNum,
        tbAge = tbAge,
        ipAddress = ipAddress,
        isFollowed = isFollowed,
        hasConcern = hasConcern,
        tiebaUid = tiebaUid,
        lastUpdateTime = System.currentTimeMillis(),
        cacheTime = System.currentTimeMillis()
    )
}

/**
 * 将UserEntity转换为User
 */
fun UserEntity.toDomain(): User {
    return User(
        id = id,
        name = name,
        nameShow = nameShow,
        portrait = portrait,
        intro = intro,
        sex = sex,
        birthday = birthday,
        isVip = isVip,
        levelId = levelId,
        isGod = isGod,
        godLevel = godLevel,
        postNum = postNum,
        fansNum = fansNum,
        followNum = followNum,
        friendNum = friendNum,
        likeNum = likeNum,
        agreeNum = agreeNum,
        tbAge = tbAge,
        ipAddress = ipAddress,
        isFollowed = isFollowed,
        hasConcern = hasConcern,
        tiebaUid = tiebaUid
    )
}

/**
 * 将User转换为AccountEntity
 */
fun User.toEntity(bduss: String, sToken: String, tbs: String): AccountEntity {
    return AccountEntity(
        uid = id,
        userName = name,
        nameShow = nameShow,
        portrait = portrait,
        bduss = bduss,
        sToken = sToken,
        tbs = tbs,
        intro = intro,
        sex = sex,
        isVip = isVip,
        levelId = levelId,
        postNum = postNum,
        fansNum = fansNum,
        followNum = followNum,
        agreeNum = agreeNum,
        tiebaUid = tiebaUid,
        isLoggedIn = true,
        lastLoginTime = System.currentTimeMillis(),
        createTime = System.currentTimeMillis()
    )
}

/**
 * 将AccountEntity转换为User
 */
fun AccountEntity.toDomain(): User {
    return User(
        id = uid,
        name = userName,
        nameShow = nameShow,
        portrait = portrait,
        intro = intro,
        sex = sex,
        birthday = "",
        isVip = isVip,
        levelId = levelId,
        isGod = false,
        godLevel = 0,
        postNum = postNum,
        fansNum = fansNum,
        followNum = followNum,
        friendNum = 0,
        likeNum = 0,
        agreeNum = agreeNum,
        tbAge = "",
        ipAddress = "",
        isFollowed = false,
        hasConcern = false,
        tiebaUid = tiebaUid
    )
}
