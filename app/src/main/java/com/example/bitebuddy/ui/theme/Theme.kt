package com.example.bitebuddy.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryYellow,
    onPrimary = OnPrimaryYellow,
    primaryContainer = PrimaryYellowVariant,
    onPrimaryContainer = OnPrimaryYellow,
    secondary = DarkSurfaceElevated,
    onSecondary = TextWhite,
    secondaryContainer = DarkInput,
    onSecondaryContainer = TextWhite,
    tertiary = PrimaryYellow,
    onTertiary = OnPrimaryYellow,
    background = DarkBackground,
    onBackground = TextWhite,
    surface = DarkSurface,
    onSurface = TextWhite,
    surfaceVariant = DarkInput,
    onSurfaceVariant = TextSecondary,
    outline = DarkInputBorder,
    outlineVariant = CardBorder,
    error = AccentRed,
    onError = TextWhite
)

@Composable
fun BiteBuddyTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = DarkBackground.toArgb()
            window.navigationBarColor = DarkBackground.toArgb()
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = false
            insetsController.isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}