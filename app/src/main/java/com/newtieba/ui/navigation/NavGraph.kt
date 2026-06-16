package com.newtieba.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.newtieba.ui.forum.ForumScreen
import com.newtieba.ui.home.HomeScreen
import com.newtieba.ui.hot.HotScreen
import com.newtieba.ui.message.MessageScreen
import com.newtieba.ui.profile.ProfileScreen
import com.newtieba.ui.search.SearchScreen
import com.newtieba.ui.settings.SettingsScreen
import com.newtieba.ui.thread.ThreadScreen
import com.newtieba.ui.createpost.CreatePostScreen
import com.newtieba.ui.notifications.NotificationsScreen
import com.newtieba.ui.login.LoginScreen
import com.newtieba.ui.login.LoginViewModel
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier,
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToForum = { name ->
                    navController.navigate(Screen.Forum.createRoute(name))
                },
                onNavigateToThread = { tid ->
                    navController.navigate(Screen.Thread.createRoute(tid))
                },
                onNavigateToSearch = {
                    navController.navigate(Screen.Search.route)
                },
                onNavigateToNotifications = {
                    navController.navigate(Screen.Notifications.route)
                },
            )
        }

        composable(Screen.Hot.route) {
            HotScreen(
                onNavigateToThread = { tid ->
                    navController.navigate(Screen.Thread.createRoute(tid))
                },
            )
        }

        composable(Screen.Message.route) {
            MessageScreen()
        }

        composable(Screen.ProfileMe.route) {
            ProfileScreen(
                isMe = true,
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                },
                onNavigateToProfile = { uid ->
                    navController.navigate(Screen.Profile.createRoute(uid))
                },
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                onBack = { navController.popBackStack() },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route)
                },
            )
        }

        composable(
            route = Screen.Forum.route,
            arguments = listOf(navArgument("name") { type = NavType.StringType }),
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name") ?: return@composable
            ForumScreen(
                forumName = name,
                onBack = { navController.popBackStack() },
                onNavigateToThread = { tid ->
                    navController.navigate(Screen.Thread.createRoute(tid))
                },
            )
        }

        composable(
            route = Screen.Thread.route,
            arguments = listOf(navArgument("tid") { type = NavType.LongType }),
        ) { backStackEntry ->
            val tid = backStackEntry.arguments?.getLong("tid") ?: return@composable
            ThreadScreen(
                tid = tid,
                onBack = { navController.popBackStack() },
                onNavigateToProfile = { uid ->
                    navController.navigate(Screen.Profile.createRoute(uid))
                },
            )
        }

        composable(
            route = Screen.Profile.route,
            arguments = listOf(navArgument("uid") { type = NavType.LongType }),
        ) { backStackEntry ->
            val uid = backStackEntry.arguments?.getLong("uid") ?: return@composable
            ProfileScreen(
                uid = uid,
                isMe = false,
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                },
                onNavigateToProfile = { /* no-op, already on profile */ },
            )
        }

        composable(Screen.Search.route) {
            SearchScreen(
                onBack = { navController.popBackStack() },
                onNavigateToThread = { tid ->
                    navController.navigate(Screen.Thread.createRoute(tid))
                },
                onNavigateToForum = { name ->
                    navController.navigate(Screen.Forum.createRoute(name))
                },
            )
        }

        composable(Screen.CreatePost.route) {
            CreatePostScreen(
                onBack = { navController.popBackStack() },
                onSuccess = { navController.popBackStack() },
            )
        }

        composable(Screen.Notifications.route) {
            NotificationsScreen(
                onBack = { navController.popBackStack() },
            )
        }

        composable(Screen.Login.route) {
            val loginViewModel: LoginViewModel = hiltViewModel()
            LoginScreen(
                onBack = { navController.popBackStack() },
                onLoginSuccess = { bduss, stoken ->
                    loginViewModel.saveCredentials(bduss, stoken)
                    navController.popBackStack()
                },
            )
        }
    }
}
