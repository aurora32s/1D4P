package com.haman.allformemory.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.haman.allformemory.navigation.HarooNavHost

@Composable
fun HarooApp(
    harooAppState: HarooAppState = rememberHarooAppState()
) {
    Scaffold() {
        HarooNavHost(
            modifier = Modifier.padding(it),
            navController = harooAppState.navController
        )
    }
}