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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.core.designsystem.components.HarooSnackbar
import com.core.designsystem.theme.HarooTheme
import com.core.ui.manager.PermissionManager
import com.core.ui.manager.externalStoragePermission
import com.core.ui.setting.PermissionRational
import com.haman.allformemory.navigation.HarooNavHost

@Composable
fun HarooApp(
    permissionManager: PermissionManager,
    backgroundColor: List<Color> = HarooTheme.colors.interactiveBackground,
    appState: HarooAppState = rememberHarooAppState(),
    onFinishApp: () -> Unit
) {
    val isNeedPermissionRational = permissionManager.isNeedPermissionRationale.collectAsState()
    val ableBasePermission = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        permissionManager.requestPermission(
            listOf(externalStoragePermission)
        ) { isGranted ->
            if (isGranted) ableBasePermission.value = true
            else onFinishApp()
        }
    }

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
        if (ableBasePermission.value) {
            HarooNavHost(
                modifier = Modifier.padding(it),
                navController = appState.navController
            )
        }
        if (isNeedPermissionRational.value.isNotEmpty()) {
            PermissionRational(
                modifier = Modifier.fillMaxSize(),
                permissions = isNeedPermissionRational.value,
                onClickGrant = permissionManager::responseGrantRational,
                onClickDeny = permissionManager::responseDenyRational
            )
        }
    }
}