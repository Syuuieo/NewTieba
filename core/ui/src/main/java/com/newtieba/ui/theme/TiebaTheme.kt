package com.newtieba.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.theme.ThemeController
import top.yukonga.miuix.kmp.theme.ColorSchemeMode

/**
 * NewTieba 主题
 * 基于 Miuix 的 HyperOS 风格主题
 */
@Composable
fun TiebaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorSchemeMode = when {
        dynamicColor && darkTheme -> ColorSchemeMode.MonetDark
        dynamicColor && !darkTheme -> ColorSchemeMode.MonetLight
        darkTheme -> ColorSchemeMode.Dark
        else -> ColorSchemeMode.Light
    }

    val controller = remember { ThemeController(colorSchemeMode) }

    MiuixTheme(controller = controller) {
        content()
    }
}

/**
 * 获取当前主题的颜色方案
 */
object TiebaThemeColors {
    val colorScheme: MiuixTheme.ColorScheme
        @Composable
        get() = MiuixTheme.colorScheme

    val textStyles: MiuixTheme.TextStyles
        @Composable
        get() = MiuixTheme.textStyles
}
