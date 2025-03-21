package com.example.FlyHigh.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorPalette = lightColorScheme(
    primary = Blue500,
    onPrimary = Color.White,
    primaryContainer = Blue300,
    onPrimaryContainer = Color.Black,
    // ... otros colores
)

private val DarkColorPalette = darkColorScheme(
    primary = Blue700,
    onPrimary = Color.White,
    primaryContainer = Blue500,
    onPrimaryContainer = Color.Black,
    // ... otros colores
)

@Composable
fun FlyHighTheme(
    isDarkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (isDarkTheme) DarkColorPalette else LightColorPalette,
        typography = Typography,
        content = content
    )
}
