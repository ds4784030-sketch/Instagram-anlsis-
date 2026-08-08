package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val BentoColorScheme = lightColorScheme(
    primary = BentoPurple,
    secondary = InstaPink,
    tertiary = InstaBlue,
    background = BentoBackground,
    surface = BentoSurface,
    surfaceVariant = BentoSurfaceVariant,
    onPrimary = BentoSurface,
    onSecondary = BentoSurface,
    onBackground = BentoTextPrimary,
    onSurface = BentoTextPrimary,
    onSurfaceVariant = BentoTextSecondary,
    outline = BentoBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = BentoColorScheme,
        typography = Typography,
        content = content
    )
}


