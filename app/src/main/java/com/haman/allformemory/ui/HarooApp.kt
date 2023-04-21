package com.haman.allformemory.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.core.designsystem.components.HarooSnackbar
import com.core.designsystem.theme.HarooTheme
import com.haman.allformemory.navigation.HarooNavHost

@Composable
fun HarooApp(
    backgroundColor: List<Color> = HarooTheme.colors.interactiveBackground,
    harooAppState: HarooAppState = rememberHarooAppState()
) {
    val appState = rememberHarooAppState()
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                drawRect(brush = Brush.linearGradient(backgroundColor))
            }
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding(),
        backgroundColor = Color.Transparent,
        scaffoldState = appState.scaffoldState,
        snackbarHost = {
            SnackbarHost(
                hostState = it,
                modifier = Modifier.systemBarsPadding(),
                snackbar = { snackbarData -> HarooSnackbar(snackbarData = snackbarData) }
            )
        }
    ) {
        HarooNavHost(
            modifier = Modifier.padding(it),
            navController = harooAppState.navController
        )
    }
}