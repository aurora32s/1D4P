package com.haman.allformemory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.core.designsystem.theme.AllForMemoryTheme
import com.core.ui.manager.PermissionManager
import com.haman.allformemory.ui.HarooApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val permissionManager = PermissionManager(this)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            AllForMemoryTheme {
                HarooApp(permissionManager)
            }
        }
    }
}