package com.core.designsystem.theme

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = HarooColors(
    gradient4_1 = listOf(PinkA400, DeepBlue900, Black, DeepBlue800),
    brand = PinkA400,
    uiBackground = White,
    uiBorder = White,
    text = White,
    iconPrimary = White,
    isDark = true
)

private val LightColorScheme = HarooColors(
    gradient4_1 = listOf(PinkA400, DeepBlue900, Black, DeepBlue800),
    brand = PinkA400,
    uiBackground = White,
    uiBorder = White,
    text = White,
    iconPrimary = White,
    isDark = false
)

@Composable
fun AllForMemoryTheme(
    context: Context = LocalContext.current,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme

    SideEffect {
        val window = (context as Activity).window
        window.statusBarColor = 0
    }

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
    uiBackground: Color,
    uiBorder: Color,
    interactiveBackground: List<Color> = gradient4_1,
    text: Color,
    iconPrimary: Color = brand,
    snackBarBackground: Color = SnackBarBackground,
    dim: Color = Dim,
    isDark: Boolean
) {
    var gradient4_1 by mutableStateOf(gradient4_1)
        private set
    var brand by mutableStateOf(brand)
        private set
    var uiBackground by mutableStateOf(uiBackground)
        private set
    var uiBorder by mutableStateOf(uiBorder)
        private set
    var interactiveBackground by mutableStateOf(interactiveBackground)
        private set
    var text by mutableStateOf(text)
        private set
    var iconPrimary by mutableStateOf(iconPrimary)
        private set
    var snackBarBackground by mutableStateOf(snackBarBackground)
        private set
    var dim by mutableStateOf(dim)
        private set
    var isDark by mutableStateOf(isDark)
        private set

    fun update(other: HarooColors) {
        this.gradient4_1 = other.gradient4_1
        this.brand = other.brand
        this.uiBackground = other.uiBackground
        this.uiBorder = other.uiBorder
        this.interactiveBackground = other.interactiveBackground
        this.text = other.text
        this.iconPrimary = other.iconPrimary
        this.snackBarBackground = other.snackBarBackground
        this.dim = other.dim
        this.isDark = other.isDark
    }

    fun copy() = HarooColors(
        gradient4_1 = gradient4_1,
        brand = brand,
        uiBackground = uiBackground,
        uiBorder = uiBorder,
        interactiveBackground = interactiveBackground,
        text = text,
        iconPrimary = iconPrimary,
        snackBarBackground = snackBarBackground,
        dim = dim,
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