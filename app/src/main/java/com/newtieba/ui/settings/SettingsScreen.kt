package com.newtieba.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.extra.ArrowPreference
import top.yukonga.miuix.kmp.extra.OverlayDropdownPreference
import top.yukonga.miuix.kmp.extra.SliderPreference
import top.yukonga.miuix.kmp.extra.SmallTitle
import top.yukonga.miuix.kmp.extra.SwitchPreference

@Composable
fun SettingsScreen(
    onBack: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {},
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState

    Scaffold(
        topBar = {
            TopAppBar(
                title = "设置",
                onBack = onBack,
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
        ) {
            SmallTitle("账号")
            ArrowPreference(
                title = "账号管理",
                modifier = Modifier.padding(horizontal = 12.dp),
                onClick = onNavigateToLogin,
            )

            SmallTitle("外观")
            SwitchPreference(
                title = "跟随系统深色模式",
                checked = uiState.darkMode,
                onCheckedChange = { viewModel.setDarkMode(it) },
                modifier = Modifier.padding(horizontal = 12.dp),
            )
            OverlayDropdownPreference(
                title = "主题",
                options = listOf("System", "Light", "Dark", "MonetSystem", "MonetLight", "MonetDark"),
                selectedIndex = listOf("System", "Light", "Dark", "MonetSystem", "MonetLight", "MonetDark")
                    .indexOf(uiState.theme).coerceAtLeast(0),
                onSelect = { index ->
                    viewModel.setTheme(
                        listOf("System", "Light", "Dark", "MonetSystem", "MonetLight", "MonetDark")[index]
                    )
                },
                modifier = Modifier.padding(horizontal = 12.dp),
            )
            SliderPreference(
                title = "字体大小",
                value = uiState.fontSize,
                onValueChange = { viewModel.setFontSize(it) },
                valueRange = 0.5f..1.5f,
                modifier = Modifier.padding(horizontal = 12.dp),
            )
            OverlayDropdownPreference(
                title = "图片加载质量",
                options = listOf("低", "中", "高"),
                selectedIndex = listOf("低", "中", "高").indexOf(uiState.imageQuality).coerceAtLeast(0),
                onSelect = { index ->
                    viewModel.setImageQuality(listOf("低", "中", "高")[index])
                },
                modifier = Modifier.padding(horizontal = 12.dp),
            )

            SmallTitle("其他")
            SwitchPreference(
                title = "通知",
                checked = uiState.notificationsEnabled,
                onCheckedChange = { viewModel.setNotificationsEnabled(it) },
                modifier = Modifier.padding(horizontal = 12.dp),
            )
            ArrowPreference(
                title = "屏蔽词管理",
                modifier = Modifier.padding(horizontal = 12.dp),
                onClick = { /* TODO: 屏蔽词页 */ },
            )
            ArrowPreference(
                title = "清除缓存",
                summary = uiState.cacheSize,
                modifier = Modifier.padding(horizontal = 12.dp),
                onClick = { /* TODO: 清除缓存弹窗 */ },
            )
            ArrowPreference(
                title = "关于",
                summary = "v1.0.0",
                modifier = Modifier.padding(horizontal = 12.dp),
                onClick = { /* TODO: 关于页 */ },
            )
        }
    }
}
