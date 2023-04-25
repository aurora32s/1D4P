package com.core.ui.manager

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.core.content.ContextCompat
import com.core.ui.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PermissionManager(
    private val context: Context
) {
    // permission rationale 정보 , empty 인 경우 hide
    private val _isNeedPermissionRationale = MutableStateFlow<List<Permission>>(emptyList())
    val isNeedPermissionRationale: StateFlow<List<Permission>> = _isNeedPermissionRationale.asStateFlow()

    private var currentRequestCallback: (() -> Unit)? = null

    // permission 요청
    fun requestPermission(permission: List<String>, cb: () -> Unit) {
        // 1. Grant 되지 않는 permission 들만 filtering
        val notGrantedPermissions = permission.filterNotGranted()
        // 2. 모든 Permission 이 이미 승인 되어 있는 경우 에는 바로 cb 호출
        if (notGrantedPermissions.isEmpty()) cb()
        // 3. 그러지 않을 경우, Rational show 요청
        currentRequestCallback = cb
        _isNeedPermissionRationale.value = permission.map {
            check(Permissions[it] != null)
            Permissions[it]!!
        }
    }

    private fun List<String>.filterNotGranted(): List<String> {
        return filterNot { permission ->
            ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    /**
     * 권한 설명이 들어간 Rational 에서 승인 / 거부 버튼 클릭
     * @param grantOrDeny true 인 경우 grant, false 인 경우 deny
     */
    fun responseRational(grantOrDeny: Boolean) {
        if (grantOrDeny) {
            val permissions = _isNeedPermissionRationale.value.map { it.permission }.toTypedArray()
            _isNeedPermissionRationale.value = emptyList()
            requestPermissionLauncher?.launch(permissions)
        } else currentRequestCallback?.invoke()
    }

    private val requestPermissionLauncher =
        (context as? ComponentActivity)?.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { results ->
            currentRequestCallback?.invoke()
        }
}

val externalStoragePermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
    Manifest.permission.READ_MEDIA_IMAGES
else Manifest.permission.READ_EXTERNAL_STORAGE

/**
 * 앱에 필요한 권한 정보
 */
class Permission(
    val permission: String, // Permission Manifest
    @StringRes val title: Int, // String name resource id
    @StringRes val description: Int, // Permission description resource id
    val icon: ImageVector, // permission icon
)

private val Permissions = mapOf(
    externalStoragePermission to Permission(
        permission = externalStoragePermission,
        title = R.string.external_storage_title,
        description = R.string.external_storage_desc,
        icon = Icons.Default.Image
    )
)
