package com.example.jelajahku.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val JelajahDarkColorScheme = darkColorScheme(
    primary = JelajahBlueDark,
    onPrimary = DarkBackground,
    primaryContainer = Color(0xFF0D47A1),
    onPrimaryContainer = TextOnDark,
    secondary = JelajahTealDark,
    onSecondary = DarkBackground,
    tertiary = JelajahOrangeDark,
    onTertiary = DarkBackground,
    background = DarkBackground,
    onBackground = TextOnDark,
    surface = DarkSurface,
    onSurface = TextOnDark,
    surfaceVariant = Color(0xFF2A3946),
    onSurfaceVariant = Color(0xFFD0DAE2)
)

private val JelajahLightColorScheme = lightColorScheme(
    primary = JelajahBlue,
    onPrimary = LightSurface,
    primaryContainer = Color(0xFFD6E7FF),
    onPrimaryContainer = Color(0xFF001B3D),
    secondary = JelajahTeal,
    onSecondary = LightSurface,
    secondaryContainer = Color(0xFFB5EFE8),
    onSecondaryContainer = Color(0xFF00201D),
    tertiary = JelajahOrange,
    onTertiary = LightSurface,
    background = LightBackground,
    onBackground = TextPrimary,
    surface = LightSurface,
    onSurface = TextPrimary,
    surfaceVariant = Color(0xFFE9F0F5),
    onSurfaceVariant = TextSecondary,
    outline = BorderLight
)

private val JelajahShapes = Shapes(
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp)
)

@Composable
fun JelajahKuTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) JelajahDarkColorScheme else JelajahLightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = JelajahShapes,
        content = content
    )
}