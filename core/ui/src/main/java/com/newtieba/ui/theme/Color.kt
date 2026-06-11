package com.newtieba.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * 自定义颜色定义
 * 用于特定场景的颜色
 */
object TiebaColors {
    // 主题色
    val Primary = Color(0xFF00D4AA) // 贴吧绿
    val PrimaryVariant = Color(0xFF00B894)
    val Secondary = Color(0xFF6C5CE7) // 紫色
    val SecondaryVariant = Color(0xFF5B4CDB)

    // 背景色
    val Background = Color(0xFFF5F5F5)
    val Surface = Color(0xFFFFFFFF)
    val SurfaceVariant = Color(0xFFF0F0F0)

    // 深色模式背景色
    val DarkBackground = Color(0xFF121212)
    val DarkSurface = Color(0xFF1E1E1E)
    val DarkSurfaceVariant = Color(0xFF2C2C2C)

    // 文本颜色
    val TextPrimary = Color(0xFF333333)
    val TextSecondary = Color(0xFF666666)
    val TextTertiary = Color(0xFF999999)
    val TextHint = Color(0xFFCCCCCC)

    // 深色模式文本颜色
    val DarkTextPrimary = Color(0xFFFFFFFF)
    val DarkTextSecondary = Color(0xFFB0B0B0)
    val DarkTextTertiary = Color(0xFF808080)
    val DarkTextHint = Color(0xFF505050)

    // 状态颜色
    val Success = Color(0xFF00C853)
    val Warning = Color(0xFFFFAB00)
    val Error = Color(0xFFFF5252)
    val Info = Color(0xFF2196F3)

    // 等级颜色
    val Level1 = Color(0xFF9E9E9E)
    val Level2 = Color(0xFF4CAF50)
    val Level3 = Color(0xFF2196F3)
    val Level4 = Color(0xFF9C27B0)
    val Level5 = Color(0xFFFF9800)
    val Level6 = Color(0xFFF44336)
    val Level7 = Color(0xFFE91E63)
    val Level8 = Color(0xFF673AB7)
    val Level9 = Color(0xFF3F51B5)
    val Level10 = Color(0xFF00BCD4)
    val Level11 = Color(0xFF009688)
    val Level12 = Color(0xFF8BC34A)
    val Level13 = Color(0xFFFFEB3B)
    val Level14 = Color(0xFFFF5722)
    val Level15 = Color(0xFF795548)
    val Level16 = Color(0xFF607D8B)

    // VIP 颜色
    val VipYellow = Color(0xFFFFD700)
    val VipRed = Color(0xFFFF4444)

    // 性别颜色
    val MaleBlue = Color(0xFF2196F3)
    val FemalePink = Color(0xFFE91E63)

    // 链接颜色
    val LinkBlue = Color(0xFF1E88E5)

    // 分割线颜色
    val Divider = Color(0xFFE0E0E0)
    val DarkDivider = Color(0xFF404040)

    // 卡片颜色
    val CardBackground = Color(0xFFFFFFFF)
    val DarkCardBackground = Color(0xFF2C2C2C)

    // 按钮颜色
    val ButtonPrimary = Color(0xFF00D4AA)
    val ButtonSecondary = Color(0xFFE0E0E0)
    val DarkButtonSecondary = Color(0xFF404040)

    // 输入框颜色
    val InputBackground = Color(0xFFF5F5F5)
    val DarkInputBackground = Color(0xFF333333)

    // 图片占位符颜色
    val ImagePlaceholder = Color(0xFFE0E0E0)
    val DarkImagePlaceholder = Color(0xFF404040)

    // 骨架屏颜色
    val Skeleton = Color(0xFFE0E0E0)
    val DarkSkeleton = Color(0xFF404040)

    /**
     * 根据等级获取颜色
     */
    fun getLevelColor(level: Int): Color {
        return when {
            level <= 0 -> Level1
            level <= 3 -> Level2
            level <= 6 -> Level3
            level <= 9 -> Level4
            level <= 12 -> Level5
            level <= 15 -> Level6
            level <= 18 -> Level7
            level <= 21 -> Level8
            level <= 24 -> Level9
            level <= 27 -> Level10
            level <= 30 -> Level11
            level <= 33 -> Level12
            level <= 36 -> Level13
            level <= 39 -> Level14
            level <= 42 -> Level15
            else -> Level16
        }
    }
}
