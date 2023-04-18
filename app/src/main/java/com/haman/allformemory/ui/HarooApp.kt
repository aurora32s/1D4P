package com.haman.allformemory.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.core.designsystem.theme.HarooTheme
import com.haman.allformemory.navigation.HarooNavHost

@Composable
fun HarooApp(
    backgroundColor: List<Color> = HarooTheme.colors.interactiveBackground,
    harooAppState: HarooAppState = rememberHarooAppState()
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
            .drawBehind {
                drawRect(brush = Brush.linearGradient(backgroundColor))
            },
        backgroundColor = Color.Transparent
    ) {
        HarooNavHost(
            modifier = Modifier.padding(it),
            navController = harooAppState.navController
        )
    }
}