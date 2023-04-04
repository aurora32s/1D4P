package com.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = HarooColors(
    gradient4_1 = listOf(PinkA400, DeepBlue900, Black, DeepBlue800),
    brand = PinkA400,
    text = White,
    iconPrimary = White,
    isDark = true
)

private val LightColorScheme = HarooColors(
    gradient4_1 = listOf(PinkA400, DeepBlue900, Black, DeepBlue800),
    brand = PinkA400,
    text = White,
    iconPrimary = White,
    isDark = false
)

@Composable
fun AllForMemoryTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme

    ProvideHarooColors(colors = colors) {
        MaterialTheme(
            colors = debugColors(darkTheme),
            shapes = Shapes,
            typography = Typography,
            content = content
        )
    }
}

object HarooTheme {
    val colors: HarooColors
        @Composable get() = LocalHarooColors.current
}

@Composable
fun ProvideHarooColors(
    colors: HarooColors,
    content: @Composable () -> Unit
) {
    val colorPalette = remember { colors.copy() }
    colorPalette.update(colors)

    CompositionLocalProvider(
        LocalHarooColors provides colorPalette,
        content = content
    )
}

@Stable
class HarooColors(
    gradient4_1: List<Color>,
    brand: Color,
    interactiveBackground: List<Color> = gradient4_1,
    text: Color,
    iconPrimary: Color = brand,
    isDark: Boolean
) {
    var gradient4_1 by mutableStateOf(gradient4_1)
        private set
    var brand by mutableStateOf(brand)
        private set
    var interactiveBackground by mutableStateOf(interactiveBackground)
        private set
    var text by mutableStateOf(text)
        private set
    var iconPrimary by mutableStateOf(iconPrimary)
        private set
    var isDark by mutableStateOf(isDark)
        private set

    fun update(other: HarooColors) {
        this.gradient4_1 = other.gradient4_1
        this.brand = other.brand
        this.interactiveBackground = other.interactiveBackground
        this.text = other.text
        this.iconPrimary = other.iconPrimary
        this.isDark = other.isDark
    }

    fun copy() = HarooColors(
        gradient4_1 = gradient4_1,
        brand = brand,
        interactiveBackground = interactiveBackground,
        text = text,
        iconPrimary = iconPrimary,
        isDark = isDark
    )
}

private val LocalHarooColors = staticCompositionLocalOf<HarooColors> {
    error("No HarooColorPalette provider")
}

fun debugColors(
    darkTheme: Boolean,
    debugColor: Color = Color.Green
) = Colors(
    primary = debugColor,
    primaryVariant = debugColor,
    secondary = debugColor,
    secondaryVariant = debugColor,
    background = debugColor,
    surface = debugColor,
    error = debugColor,
    onPrimary = debugColor,
    onSecondary = debugColor,
    onBackground = debugColor,
    onSurface = debugColor,
    onError = debugColor,
    isLight = !darkTheme
)