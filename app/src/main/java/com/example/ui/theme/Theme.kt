package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.frostedGlass(
    shape: Shape = RoundedCornerShape(24.dp),
    useDarkTheme: Boolean = false,
    borderWidth: Dp = 1.dp
): Modifier {
    val bgColor = if (useDarkTheme) AuraSurfaceDark else AuraSurfaceLight
    val borderColor = if (useDarkTheme) GlassBorderDark else GlassBorderLight
    return this
        .clip(shape)
        .background(bgColor)
        .border(borderWidth, borderColor, shape)
}

private val DarkColorScheme = darkColorScheme(
    primary = AuraPurple,
    secondary = SecondaryPurple,
    tertiary = AuraCyan,
    background = AuraBackgroundDark,
    surface = AuraSurfaceDark,
    onPrimary = AuraBackgroundDark,
    onSecondary = AuraOnSurfaceDark,
    onBackground = AuraOnBackgroundDark,
    onSurface = AuraOnSurfaceDark,
    primaryContainer = AuraSurfaceDark,
    secondaryContainer = AuraPurple
)

private val LightColorScheme = lightColorScheme(
    primary = AuraPurple,
    secondary = SecondaryPurple,
    tertiary = AuraCyan,
    background = AuraBackgroundLight,
    surface = AuraSurfaceLight,
    onPrimary = AuraSurfaceLight,
    onSecondary = AuraOnSurfaceLight,
    onBackground = AuraOnBackgroundLight,
    onSurface = AuraOnSurfaceLight,
    primaryContainer = AuraLavender,
    secondaryContainer = AuraLavender
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+, but for strong artistic brand identity,
    // we override it or allow the option to force our premium theme.
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
