package com.newtieba.navigation

/**
 * 导航路由定义
 */
object Routes {
    const val HOME = "home"
    const val LOGIN = "login"
    const val THREAD = "thread/{threadId}"
    const val FORUM = "forum/{forumId}/{forumName}"
    const val PROFILE = "profile/{userId}"
    const val SEARCH = "search"
    const val SETTINGS = "settings"
    const val MESSAGE = "message"
    const val COLLECTION = "collection"
    const val HISTORY = "history"

    /**
     * 创建帖子详情路由
     */
    fun thread(threadId: Long): String {
        return "thread/$threadId"
    }

    /**
     * 创建吧详情路由
     */
    fun forum(forumId: Long, forumName: String): String {
        return "forum/$forumId/$forumName"
    }

    /**
     * 创建用户主页路由
     */
    fun profile(userId: Long): String {
        return "profile/$userId"
    }
}
