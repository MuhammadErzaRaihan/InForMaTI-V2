package com.example.projecthmti.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.example.projecthmti.ui.theme.Setting.BlackDark
import com.example.projecthmti.ui.theme.Setting.BlackLight
import com.example.projecthmti.ui.theme.Setting.PrimaryBlueDark
import com.example.projecthmti.ui.theme.Setting.PrimaryBlueLight
import com.example.projecthmti.ui.theme.Setting.SecondaryBlueDark
import com.example.projecthmti.ui.theme.Setting.SecondaryBlueLight
import com.example.projecthmti.ui.theme.Setting.Typography
import com.example.projecthmti.ui.theme.Setting.WhiteDark
import com.example.projecthmti.ui.theme.Setting.WhiteLight

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlueLight,
    secondary = SecondaryBlueLight,
    background = WhiteLight,
    surface = WhiteLight,
    onPrimary = WhiteLight,
    onSecondary = WhiteLight,
    onBackground = BlackLight,
    onSurface = BlackLight,
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlueDark,
    secondary = SecondaryBlueDark,
    background = WhiteDark, // Background dark
    surface = WhiteDark,
    onPrimary = WhiteDark,
    onSecondary = WhiteDark,
    onBackground = BlackDark, // Text putih di dark mode
    onSurface = BlackDark,
)

@Composable
fun ProjectHMTITheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}