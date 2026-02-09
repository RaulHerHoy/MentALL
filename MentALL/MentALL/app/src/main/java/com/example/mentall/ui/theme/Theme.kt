package com.example.mentall.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.isSystemInDarkTheme

private val LightColors = lightColorScheme(
    primary = MintDark,
    onPrimary = Color.White,
    primaryContainer = MintSoft,
    onPrimaryContainer = TealDeep,

    secondary = Mint,
    onSecondary = TealDeep,
    secondaryContainer = Color(0xFFC9EFE8),
    onSecondaryContainer = TealDeep,

    background = BgLight,
    onBackground = TealDeep,
    surface = SurfaceLight,
    onSurface = TealDeep,

    surfaceVariant = Color(0xFFEAF7F4),
    onSurfaceVariant = Color(0xFF245B55),

    outline = OutlineLight,
    error = Color(0xFFB3261E),
)

private val DarkColors = darkColorScheme(
    primary = Mint,
    onPrimary = Color(0xFF06221E),
    primaryContainer = Color(0xFF1E3F3B),
    onPrimaryContainer = Color(0xFFCFF4EE),

    secondary = Color(0xFF7EDACD),
    onSecondary = Color(0xFF06221E),
    secondaryContainer = Color(0xFF173532),
    onSecondaryContainer = Color(0xFFCFF4EE),

    background = BgDark,
    onBackground = Color(0xFFCFEDE8),
    surface = SurfaceDark,
    onSurface = Color(0xFFCFEDE8),

    surfaceVariant = Color(0xFF173532),
    onSurfaceVariant = Color(0xFFB7E3DC),

    outline = OutlineDark,
    error = Color(0xFFF2B8B5),
)

@Composable
fun MentALLTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // ✅ IMPORTANTE: si tu template tenía Dynamic Color, lo apagamos
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
