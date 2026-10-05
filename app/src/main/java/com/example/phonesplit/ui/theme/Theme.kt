package com.example.phonesplit.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

// Dual Theming: Video Player Dark Theme with Neon Blue Accents
private val VideoPlayerDarkColorScheme = darkColorScheme(
    primary = NeonBluePrimary,
    secondary = NeonBlueSecondary,
    tertiary = NeonBlueTertiary,
    background = DarkCharcoalBg,
    surface = DarkCharcoalSurface,
    surfaceVariant = DarkCharcoalSurfaceVariant,
    onBackground = DarkOnSurface,
    onSurface = DarkOnSurface,
    onPrimary = DarkOnPrimary,
    onSecondary = DarkOnPrimary,
    onTertiary = DarkOnPrimary
)

// Dual Theming: Chat / Messaging Light Theme with Pastel Orange Accents
private val ChatMessagingLightColorScheme = lightColorScheme(
    primary = PastelOrangePrimary,
    secondary = PastelOrangeSecondary,
    tertiary = PastelOrangeTertiary,
    background = LightOffWhiteBg,
    surface = LightOffWhiteSurface,
    surfaceVariant = LightOffWhiteSurfaceVariant,
    onBackground = LightOnSurface,
    onSurface = LightOnSurface,
    onPrimary = LightOnPrimary,
    onSecondary = LightOnPrimary,
    onTertiary = LightOnPrimary
)

@Composable
fun PhonesplitTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
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

@Composable
fun VideoPlayerTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = VideoPlayerDarkColorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun ChatMessagingTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = ChatMessagingLightColorScheme,
        typography = Typography,
        content = content
    )
}
