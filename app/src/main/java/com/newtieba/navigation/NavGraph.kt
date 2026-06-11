package com.newtieba.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.newtieba.feature.home.HomeScreen
import com.newtieba.feature.login.LoginScreen
import com.newtieba.feature.thread.ThreadScreen

/**
 * 导航路由
 */
object Screen {
    const val HOME = "home"
    const val LOGIN = "login"
    const val THREAD = "thread/{threadId}"
    const val FORUM = "forum/{forumId}/{forumName}"
    const val PROFILE = "profile/{userId}"
    const val SEARCH = "search"
    const val SETTINGS = "settings"
}

/**
 * 导航图
 */
@Composable
fun TiebaNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.HOME
    ) {
        // 首页
        composable(Screen.HOME) {
            HomeScreen(
                onThreadClick = { threadId ->
                    navController.navigate("thread/$threadId")
                },
                onForumClick = { forumId, forumName ->
                    navController.navigate("forum/$forumId/$forumName")
                },
                onUserClick = { userId ->
                    navController.navigate("profile/$userId")
                }
            )
        }

        // 登录
        composable(Screen.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.popBackStack()
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // 帖子详情
        composable(
            route = Screen.THREAD,
            arguments = listOf(
                navArgument("threadId") { type = NavType.LongType }
            )
        ) {
            ThreadScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onUserClick = { userId ->
                    navController.navigate("profile/$userId")
                },
                onForumClick = { forumId, forumName ->
                    navController.navigate("forum/$forumId/$forumName")
                }
            )
        }

        // TODO: 吧详情
        // composable(Screen.FORUM) { ... }

        // TODO: 用户主页
        // composable(Screen.PROFILE) { ... }

        // TODO: 搜索
        // composable(Screen.SEARCH) { ... }

        // TODO: 设置
        // composable(Screen.SETTINGS) { ... }
    }
}
