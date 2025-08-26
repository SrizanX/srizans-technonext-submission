package com.srizan.technonextcodingassessment.designsystem.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorSchemeb = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorSchemeb = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)




private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF3F51B5), // Indigo Blue
    onPrimary = Color(0xFFFFFFFF),
    secondary = Color(0xFF26A69A), // Teal
    onSecondary = Color(0xFFFFFFFF),
    surface = Color(0xFFF5F7FA), // Off-White
    onSurface = Color(0xFF212121), // Dark Gray
    error = Color(0xFFD32F2F), // Red
    onError = Color(0xFFFFFFFF),
    // Success is custom, not in Material3 ColorScheme; handle in components
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF8F9BFF), // Light Indigo
    onPrimary = Color(0xFFFFFFFF),
    secondary = Color(0xFF4DB6AC), // Light Teal
    onSecondary = Color(0xFFFFFFFF),
    surface = Color(0xFF121212), // Dark Gray
    onSurface = Color(0xFFE0E0E0), // Off-White
    error = Color(0xFFD32F2F),
    onError = Color(0xFFFFFFFF),
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
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