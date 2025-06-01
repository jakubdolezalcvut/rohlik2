package cz.rohlik.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val JediLightColorScheme = lightColorScheme(
    primary = Color(0xFF3D8EFF),       // Jedi blue
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD0E4FF),

    secondary = Color(0xFF4CAF50),     // green (Yoda / nature)
    onSecondary = Color.White,

    background = Color(0xFFF8F5E3),    // light sand (Tatooine)
    onBackground = Color(0xFF1B1B1B),

    surface = Color(0xFFE6F2FF),
    onSurface = Color(0xFF111111),

    error = Color(0xFFB00020),
    onError = Color.White,
)

val SithDarkColorScheme = darkColorScheme(
    primary = Color(0xFFB71C1C),       // Sith red
    onPrimary = Color.White,
    primaryContainer = Color(0xFF7F0000),

    secondary = Color(0xFF616161),     // metallic gray (Empire tech)
    onSecondary = Color.White,

    background = Color(0xFF0D0D0D),    // near black
    onBackground = Color(0xFFE0E0E0),

    surface = Color(0xFF1A1A1A),
    onSurface = Color.White,

    error = Color(0xFFFF8A80),
    onError = Color.Black,
)

@Composable
fun RohlikTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> SithDarkColorScheme
        else -> JediLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = RohlikTypography,
        content = content
    )
}
