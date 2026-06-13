package com.dendron.quizzer.presentation.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Blue100,
    onPrimary = Blue950,
    primaryContainer = Blue700,
    onPrimaryContainer = Color.White,
    secondary = Blue500,
    tertiary = Pink80,
    background = Blue950,
    onBackground = Color.White,
    surface = Blue900,
    onSurface = Color.White,
    surfaceVariant = Blue700,
    onSurfaceVariant = Color.White,
)

private val LightColorScheme = lightColorScheme(
    primary = Blue900,
    onPrimary = Color.White,
    primaryContainer = Blue100,
    onPrimaryContainer = Blue950,
    secondary = Blue500,
    tertiary = Pink40,
    background = Slate50,
    onBackground = Slate900,
    surface = Color.White,
    onSurface = Slate900,
    surfaceVariant = Blue100,
    onSurfaceVariant = Blue900,
)

@Composable
fun QuizzerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}