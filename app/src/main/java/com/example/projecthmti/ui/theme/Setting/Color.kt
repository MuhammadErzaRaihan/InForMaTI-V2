package com.example.projecthmti.ui.theme.Setting

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Light Colors
val PrimaryBlueLight = Color(0xFF3FD0FF)
val SecondaryBlueLight = Color(0xFF00C5FD)
val LightBlueLight = Color(0xFF96D3E3)

val WhiteLight = Color(0xFFFFFFFF)
val BlackLight = Color(0xFF000000)

// Dark Colors
val PrimaryBlueDark = Color(0xFF3FD0FF)
val SecondaryBlueDark = Color(0xFF00C5FD)
val LightBlueDark = Color(0xFF96D3E3)

val WhiteDark = Color(0xFF000000)

val BlackDark = Color(0xFFFFFFFF)

val TransparentBlack = Color(0x80000000)

object AppColors {
    val primaryBlue: Color @Composable get() = if (isSystemInDarkTheme()) PrimaryBlueDark else PrimaryBlueLight

    val background: Color @Composable get() = if (isSystemInDarkTheme()) WhiteDark else WhiteLight

}