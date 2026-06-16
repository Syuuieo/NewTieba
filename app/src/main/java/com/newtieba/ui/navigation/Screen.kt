package com.newtieba.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val title: String = "",
    val icon: ImageVector? = null,
) {
    data object Home : Screen("home", "首页", Icons.Default.Home)
    data object Hot : Screen("hot", "热门", Icons.Default.Star)
    data object Message : Screen("message", "消息", Icons.Default.Email)
    data object ProfileMe : Screen("profile/me", "我的", Icons.Default.Person)
    data object Settings : Screen("settings")

    data object Forum : Screen("forum/{name}") {
        fun createRoute(name: String) = "forum/$name"
    }
    data object Thread : Screen("thread/{tid}") {
        fun createRoute(tid: Long) = "thread/$tid"
    }
    data object Profile : Screen("profile/{uid}") {
        fun createRoute(uid: Long) = "profile/$uid"
    }
    data object Search : Screen("search")
    data object CreatePost : Screen("create_post")
    data object Notifications : Screen("notifications")
    data object Login : Screen("login")
}

val bottomNavItems = listOf(
    Screen.Home,
    Screen.Hot,
    Screen.Message,
    Screen.ProfileMe,
)
