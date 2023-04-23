package com.haman.allformemory

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.core.designsystem.theme.AllForMemoryTheme
import com.haman.allformemory.ui.HarooApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        checkPermission()
        setContent {
            AllForMemoryTheme {
                val externalStoragePermissionGranted = mainViewModel.isExternalStoragePermissionGranted.collectAsState()
                val isNeedPermissionRationale = mainViewModel.isNeedPermissionRationale.collectAsState()
                HarooApp(
                    externalStoragePermissionGranted = externalStoragePermissionGranted.value,
                    isNeedPermissionRationale = isNeedPermissionRationale.value,
                    onGrantPermission = ::requestPermission
                )
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) { // 권한 승인
                mainViewModel.externalStoragePermissionGrant()
            } else { // 권한 거부
                // TODO 권한 거부 시, 동작
            }
        }

    private val externalStoragePermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        Manifest.permission.READ_MEDIA_IMAGES
    else Manifest.permission.READ_EXTERNAL_STORAGE

    private fun requestPermission() {
        requestPermissionLauncher.launch(externalStoragePermission)
    }

    private fun checkPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                externalStoragePermission
            ) == PackageManager.PERMISSION_GRANTED -> mainViewModel.externalStoragePermissionGrant()

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                externalStoragePermission
            ) -> {
                mainViewModel.needPermissionRationale()
            }

            else -> {
                requestPermission()
            }
        }
    }
}